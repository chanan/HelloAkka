package queries;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.google.common.util.concurrent.ListenableFuture;
import models.mapper.Course;
import models.mapper.Student;

@Accessor
public interface StudentAccessor {
    @Query("SELECT * FROM helloakka.students WHERE course_id = :courseId AND student_id = :studentId")
    public ListenableFuture<Student> getStudent(@Param("courseId") String courseId, @Param("studentId") String studentId);

    @Query("SELECT * FROM helloakka.students WHERE course_id = :courseId")
    public ListenableFuture<Result<Student>> getAll(@Param("courseId") String courseId);
}