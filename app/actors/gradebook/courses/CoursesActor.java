package actors.gradebook.courses;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import models.courses.Queries;

public class CoursesActor extends AbstractActor {
    public CoursesActor() {
        receive(
                ReceiveBuilder.match(Queries.CourseRequest.class, request -> {
                    final ActorRef course = getCourse(request.course);
                    course.forward(request, context());
                }).build()
        );
    }

    private ActorRef getCourse(String courseId) {
        final ActorRef course = getContext().getChild("course_" + courseId);
        if(course != null) return course;
        //return getContext().actorOf(Props.create(CourseActor.class, courseId), "course_" + courseId);
        return getContext().actorOf(Props.create(CourseWithStatusActor.class, courseId), "course_" + courseId);
        //return getContext().actorOf(Props.create(FaultTolerantCourseActor.class, courseId), "course_" + courseId);
    }
}
