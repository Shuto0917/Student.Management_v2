package raisetech.student.management.data;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourse {
    private String studentId;
    private String courseName;
    private LocalDateTime courseStartAt;
    private LocalDateTime courseEndAt;

    public StudentCourse(String studentId, String courseName, LocalDateTime courseStartAt, LocalDateTime courseEndAt) {
        this.studentId = studentId;
        this.courseName = courseName;
        this.courseStartAt = courseStartAt;
        this.courseEndAt = courseEndAt;
    }
    public StudentCourse() {
    }
}


