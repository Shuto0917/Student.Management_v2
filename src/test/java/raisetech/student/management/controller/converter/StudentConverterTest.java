package raisetech.student.management.controller.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StudentConverterTest {

    private StudentConverter sut;

    @BeforeEach
    void before() {
        sut = new StudentConverter();
    }

    @Test
    void 受講生のリストと受講生コース情報のリストを渡して受講生詳細のリストが作成できること() {
        Student student = createStudent();

        StudentCourse studentCourse = new StudentCourse(
                0,
                "1",
                "1",
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                null
        );

        List<Student> studentList = List.of(student);
        List<StudentCourse> studentCourseList = List.of(studentCourse);

        List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

        assertThat(actual.get(0).getStudent()).isEqualTo(student);
        assertThat(actual.get(0).getStudentCourseList().get(0).getStudentId()).isEqualTo("1");
        assertThat(actual.get(0).getStudentCourseList().get(0).getCourseName()).isEqualTo("1");
    }

    @Test
    void 受講生のリストと受講生コース情報のリストを渡したときに紐づかない受講生コース情報は除外されること() {
        Student student = createStudent();

        StudentCourse studentCourse = new StudentCourse(
                0,
                "2",
                "2",
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                null
        );

        List<Student> studentList = List.of(student);
        List<StudentCourse> studentCourseList = List.of(studentCourse);

        List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

        assertThat(actual.get(0).getStudent()).isEqualTo(student);
        assertThat(actual.get(0).getStudentCourseList()).isEmpty(); // 紐づかない → 空になる;
    }

    private static Student createStudent() {
        Student student = new Student("1", "江並公史", "エナミコウジ", "エナミ",
                "test@example.com", "奈良県", 36, "男性", "", false);
        return student;
    }
}