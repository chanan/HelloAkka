package actors.gradebook.courses;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import models.ResponseBase;
import models.courses.Queries;
import models.statuses.ActorStatus;
import models.statuses.StatusCreator;
import play.Logger;
import play.libs.Akka;
import queries.CourseAccessor;
import queries.StudentAccessor;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import viewmodels.courses.Course;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static akka.actor.SupervisorStrategy.stop;
import static akka.pattern.Patterns.pipe;
import static infrastructure.RichMapperFuture.wrapAndMap;

public class CourseUsingMapperActor extends AbstractActor {
    private final Cluster cluster = Cluster.builder().addContactPoint("localhost").build();
    private final Session session = cluster.connect("gradebook");

    private final String courseId;

    public CourseUsingMapperActor(String courseId) {
        this.courseId = courseId;

        receive(
                ReceiveBuilder.match(Queries.CourseRequest.class, request -> {
                    final ActorRef coordinator = context().actorOf(Props.create(CoordinatorActor.class, session));
                    coordinator.forward(request, context());
                }).build()
        );
    }

    public static class CoordinatorActor extends AbstractActor {
        private Optional<CourseDataResponse> courseDataResponse = Optional.empty();
        private Optional<StudentsDataResponse> studentsDataResponse = Optional.empty();
        private ActorRef sender;
        private Queries.CourseRequest request;

        private SupervisorStrategy strategy =
                new AllForOneStrategy(10, Duration.create("1 minute"), DeciderBuilder.
                        match(IllegalAccessException.class, e -> {
                            Logger.error("IllegalAccessException caught in Supervisor of CoordinatorActor", e);
                            context().unbecome();
                            self().tell(request, sender);
                            return stop();
                        }).
                        match(IllegalArgumentException.class, e -> {
                            Logger.error("IllegalArgumentException caught in Supervisor of CoordinatorActor", e);
                            final StatusCreator status = ActorStatus.Create().setFailure(e, request);
                            sender.tell(status, self());
                            context().stop(self());
                            return stop();
                        }).build());

        @Override
        public SupervisorStrategy supervisorStrategy() {
            return strategy;
        }

        public CoordinatorActor(Session session) {
            receive(
                    ReceiveBuilder.match(Queries.CourseRequest.class, request -> {
                        this.sender = sender();
                        this.request = request;
                        final ActorRef course = context().actorOf(Props.create(DataWorker.class, session), "course_worker");
                        final ActorRef students = context().actorOf(Props.create(DataWorker.class, session), "students_worker");
                        course.tell(new CourseDataRequest(request), self());
                        students.tell(new StudentsDataRequest(request), self());
                        context().become(
                                ReceiveBuilder.match(CourseDataResponse.class, req -> {
                                    courseDataResponse = Optional.of(req);
                                    createViewModel();
                                }).match(StudentsDataResponse.class, req -> {
                                    studentsDataResponse = Optional.of(req);
                                    createViewModel();
                                }).matchAny(o -> Logger.warn("Inner: " + o.getClass().getName())).build()
                        );
                    }).matchAny(o -> Logger.warn("Outer: " + o.getClass().getName())).build()
            );
        }

        private void createViewModel() {
            if(courseDataResponse.isPresent() && studentsDataResponse.isPresent()) {
                final Course viewmodel = new Course(request.course, courseDataResponse.get().name, studentsDataResponse.get().students);
                final ResponseBase response = new ResponseBase(request, viewmodel);
                final StatusCreator status = ActorStatus.Create().setSuccess(response, request);
                sender.tell(status, self());
                context().stop(self());
            }
        }
    }

    public static class DataWorker extends AbstractActor {
        private static Random rnd = new Random();

        public DataWorker(Session session) {
            //final BoundStatement selectCourse = new BoundStatement(session.prepare("SELECT name FROM helloakka.courses WHERE course_id = ?;"));
            //final BoundStatement selectStudents = new BoundStatement(session.prepare("SELECT student_id, name FROM helloakka.students WHERE course_id = ?;"));

            receive(
                    ReceiveBuilder.match(CourseDataRequest.class, request -> {
                        if(request.request.course.equals("error")) throw new IllegalArgumentException("Bad course name");
                        final MappingManager manager = new MappingManager(session);
                        final CourseAccessor courseAccessor = manager.createAccessor(CourseAccessor.class);
                        final Future<CourseDataResponse> mapped = wrapAndMap(courseAccessor.getCourse(request.request.course), course ->
                            new CourseDataResponse(request, course.getName())
                        );
                        pipe(mapped, Akka.system().dispatcher()).to(sender());
                        context().stop(self());
                    }).match(StudentsDataRequest.class, request -> {
                        Boolean x = rnd.nextBoolean();
                        //if(x) throw new IllegalAccessException("Boom");
                        //if(!x) throw new IllegalAccessException("Boom");
                        final MappingManager manager = new MappingManager(session);
                        final StudentAccessor studentAccessor = manager.createAccessor(StudentAccessor.class);

                        final Future<StudentsDataResponse> mapped = wrapAndMap(studentAccessor.getAll(request.request.course), students -> {
                            final List<Course.Student> list = students.all().stream().map(student -> new Course.Student(student.getStudentId(), student.getName())).collect(Collectors.toList());
                            return new StudentsDataResponse(request, list);
                        });
                        pipe(mapped, Akka.system().dispatcher()).to(sender());
                        context().stop(self());
                    }).build()
            );
        }
    }

    public static class CourseDataRequest {
        public final Queries.CourseRequest request;

        public CourseDataRequest(Queries.CourseRequest request) {
            this.request = request;
        }
    }

    public static class CourseDataResponse {
        public final CourseDataRequest request;
        public final String name;

        public CourseDataResponse(CourseDataRequest request, String name) {
            this.request = request;
            this.name = name;
        }
    }

    public static class StudentsDataRequest {
        public final Queries.CourseRequest request;

        public StudentsDataRequest(Queries.CourseRequest request) {
            this.request = request;
        }
    }

    public static class StudentsDataResponse {
        public final StudentsDataRequest request;
        public final List<Course.Student> students;

        public StudentsDataResponse(StudentsDataRequest request, List<Course.Student> students) {
            this.request = request;
            this.students = students;
        }
    }
}
