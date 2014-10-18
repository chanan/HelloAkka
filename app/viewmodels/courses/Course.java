package viewmodels.courses;

import models.RequestBase;
import viewmodels.ViewModelBase;

import java.util.List;

public class Course extends ViewModelBase {
    public final String courseId;
    public final String name;
    public final List<Student> students;

    public Course(RequestBase request, String courseId, String name, List<Student> students) {
        super(request);
        this.courseId = courseId;
        this.name = name;
        this.students = students;
    }

    public static class Student {
        public final String studentId;
        public final String courseId;

        public Student(String studentId, String courseId) {
            this.studentId = studentId;
            this.courseId = courseId;
        }
    }
}
