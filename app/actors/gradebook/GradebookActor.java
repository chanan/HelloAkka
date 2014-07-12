package actors.gradebook;

import actors.gradebook.courses.CoursesActor;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import models.courses.Queries;

public class GradebookActor extends AbstractActor {
    public final ActorRef courses = context().actorOf(Props.create(CoursesActor.class), "courses");

    public GradebookActor() {
        receive(
                ReceiveBuilder.match(Queries.CourseRequest.class, request -> courses.forward(request, context())).build()
        );
    }
}
