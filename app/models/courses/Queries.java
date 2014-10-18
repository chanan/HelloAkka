package models.courses;

import models.RequestBase;
import play.libs.F;
import play.mvc.Result;
import scala.concurrent.Promise;
import viewmodels.courses.Course;

public class Queries {
    public static class CourseRequest extends RequestBase {
        public final String course;

        public CourseRequest(int id, Promise<Result> promise, String course) {
            super(id, promise);
            this.course = course;
        }

        @Override
        public String toString() {
            return "CourseRequest{" +
                    "course='" + course + '\'' +
                    '}';
        }
    }

    public static class CourseResponse {
        public final CourseRequest request;
        public final Course response;

        public CourseResponse(CourseRequest request, Course response) {
            this.request = request;
            this.response = response;
        }
    }
}