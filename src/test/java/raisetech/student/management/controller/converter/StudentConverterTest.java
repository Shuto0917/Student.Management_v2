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
                "1",
                "1",
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1)
        );


        List<Student> studentList = List.of(student);
        List<StudentCourse> studentCourseList = List.of(studentCourse);

        List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

        assertThat(actual.get(0).getStudent()).isEqualTo(student);
        assertThat(actual.get(0).getStudentCourseList()).isEqualTo(studentCourseList);
    }

    @Test
    void 受講生のリストと受講生コース情報のリストを渡したときに紐づかない受講生コース情報は除外されること() {
        Student student = createStudent();

        StudentCourse studentCourse = new StudentCourse(
                99,
                "999",
                "Javaコース",
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                null
        );

        List<Student> studentList = List.of(student);
        List<StudentCourse> studentCourseList = List.of(studentCourse);

        List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getStudent()).isEqualTo(student);
        assertThat(actual.get(0).getStudentCourseList()).isEmpty();
    }

    private static Student createStudent() {
        return new Student("1", "江並公史", "エナミコウジ", "エナミ",
                "test@example.com", "奈良県", 36, "男性", "", false);
    }
}