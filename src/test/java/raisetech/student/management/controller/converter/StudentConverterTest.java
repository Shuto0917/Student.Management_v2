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
        StudentCourse studentCourse = createStudentCourse("1");

        List<Student> studentList = List.of(student);
        List<StudentCourse> studentCourseList = List.of(studentCourse);

        List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

        assertThat(actual.get(0).getStudent()).isEqualTo(student);
        assertThat(actual.get(0).getStudentCourseList()).isEqualTo(studentCourseList);
    }

    @Test
    void 受講生のリストと受講生コース情報のリストを渡したときに紐づかない受講生コース情報は除外されること() {
        Student student = createStudent();

        StudentCourse studentCourse = createStudentCourse("2");

        List<Student> studentList = List.of(student);
        List<StudentCourse> studentCourseList = List.of(studentCourse);

        List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

        assertThat(actual.get(0).getStudent()).isEqualTo(student);
        assertThat(actual.get(0).getStudentCourseList()).isEmpty();
    }

    private Student createStudent() {
        Student student = new Student();
        student.setId("1");
        student.setFullName("江並公史");
        student.setFurigana("エナミコウジ");
        student.setNickname("エナミ");
        student.setEmail("test@example.com");
        student.setRegion("奈良県");
        student.setAge(36);
        student.setGender("男性");
        student.setRemark("");
        student.setDeleted(false);
        return student;
    }

    private StudentCourse createStudentCourse(String number) {
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setId("1");
        studentCourse.setStudentId(number);
        studentCourse.setCourseName("AWSコース");
        studentCourse.setCourseStartAt(LocalDateTime.now());
        studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));
        return studentCourse;
    }
}