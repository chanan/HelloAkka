package models.mapper;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "helloakka", name = "students")
public class Student {
    @PartitionKey
    @Column(name = "course_id")
    private String courseId;
    @ClusteringColumn
    @Column(name = "student_id")
    private String studentId;
    private String name;

    public Student() {
    }

    public Student(String courseId, String studentId, String name) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.name = name;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (!courseId.equals(student.courseId)) return false;
        if (!name.equals(student.name)) return false;
        if (!studentId.equals(student.studentId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = courseId.hashCode();
        result = 31 * result + studentId.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
