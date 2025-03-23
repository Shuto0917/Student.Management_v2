package raisetech.student.management.data;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "受講生コース情報")
@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
public class StudentCourse {
    private int id;
    private String studentId;
    private String courseName;
    private LocalDateTime courseStartAt;
    private LocalDateTime courseEndAt;
    private String status;

    public StudentCourse(int id, String studentId, String courseName, LocalDateTime courseStartAt, LocalDateTime courseEndAt) {
        this.id = id;
        this.studentId = studentId;
        this.courseName = courseName;
        this.courseStartAt = courseStartAt;
        this.courseEndAt = courseEndAt;
    }

    public StudentCourse(String studentId, String courseName, LocalDateTime courseStartAt, LocalDateTime courseEndAt) {
        this.studentId = studentId;
        this.courseName = courseName;
        this.courseStartAt = courseStartAt;
        this.courseEndAt = courseEndAt;
    }

    public StudentCourse() {
    }
}
