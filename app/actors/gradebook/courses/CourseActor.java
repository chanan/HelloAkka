package actors.gradebook.courses;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import models.courses.Queries;
import play.libs.Akka;
import scala.concurrent.Future;
import viewmodels.courses.Course;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static akka.pattern.Patterns.pipe;
import static infrastructure.RichResultSetFuture.wrapAndMap;

public class CourseActor extends AbstractActor {
    private final Cluster cluster = Cluster.builder().addContactPoint("localhost").build();
    private final Session session = cluster.connect("gradebook");

    private final String courseId;

    public CourseActor(String courseId) {
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

        public CoordinatorActor(Session session) {
            receive(
                    ReceiveBuilder.match(Queries.CourseRequest.class, request -> {
                        sender = sender();
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
                                }).build()
                        );
                    }).build()
            );
        }

        private void createViewModel() {
            if(courseDataResponse.isPresent() && studentsDataResponse.isPresent()) {
                final Course viewmodel = new Course(null, request.course, courseDataResponse.get().name, studentsDataResponse.get().students);
                final Queries.CourseResponse response = new Queries.CourseResponse(request, viewmodel);
                sender.tell(response, self());
                context().stop(self());
            }
        }
    }

    public static class DataWorker extends AbstractActor {
        public DataWorker(Session session) {
            final BoundStatement selectCourse = new BoundStatement(session.prepare("SELECT name FROM helloakka.courses WHERE course_id = ?;"));
            final BoundStatement selectStudents = new BoundStatement(session.prepare("SELECT student_id, name FROM helloakka.students WHERE course_id = ?;"));

            receive(
                    ReceiveBuilder.match(CourseDataRequest.class, request -> {
                        selectCourse.setString("course_id", request.request.course);
                        final Future<CourseDataResponse> mapped = wrapAndMap(session.executeAsync(selectCourse), rs -> {
                            final String name = rs.one().getString("name");
                            return new CourseDataResponse(request, name);
                        });
                        pipe(mapped, Akka.system().dispatcher()).to(sender());
                        context().stop(self());
                    }).match(StudentsDataRequest.class, request -> {
                        selectStudents.setString("course_id", request.request.course);
                        final Future<StudentsDataResponse> mapped = wrapAndMap(session.executeAsync(selectStudents), rs -> {
                            final List<Course.Student> list = rs.all().stream().map(r -> new Course.Student(r.getString("student_id"), r.getString("name"))).collect(Collectors.toList());
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
