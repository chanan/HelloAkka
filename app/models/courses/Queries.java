package models.courses;

import models.RequestBase;
import viewmodels.courses.Course;

public class Queries {
    public static class CourseRequest extends RequestBase {
        public final String course;

        public CourseRequest(String course) {
            this.course = course;
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