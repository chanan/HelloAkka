package queries;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.google.common.util.concurrent.ListenableFuture;
import models.mapper.Course;

@Accessor
public interface CourseAccessor {
    @Query("SELECT * FROM helloakka.courses WHERE course_id = :courseId")
    public ListenableFuture<Course> getCourse(@Param("courseId") String courseId);
}