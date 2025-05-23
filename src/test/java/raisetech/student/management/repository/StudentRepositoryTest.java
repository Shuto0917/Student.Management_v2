package raisetech.student.management.repository;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MybatisTest
class StudentRepositoryTest {


    @Autowired
    private StudentRepository sut;

    @Test
    void 受講生の全件検索が行えること() {
        List<Student> actual = sut.search();
        assertThat(actual.size()).isEqualTo(5);
    }

    @Test
    void 受講生の検索が行えること() {
        String searchId = "1";

        Student actual = sut.searchStudent(searchId);
        assertThat(actual).isNotNull();

        Student expected = new Student(
                "1", "田中太郎", "タナカタロウ", "タロウ",
                "abc@example.com", "東京都", 25, "男性",
                null, false
        );

        System.out.println("Expected: " + expected);
        System.out.println("Actual  : " + actual);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 受講生コース情報の全件検索が行えること() {
        List<StudentCourse> actual = sut.searchStudentCoursesList();

        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(6);

        assertThat(Integer.parseInt(actual.get(0).getStudentId())).isEqualTo(1);
        assertThat(actual.get(0).getCourseName()).isEqualTo("Java");
    }

    @Test
    void 受講生IDに紐づく受講生コース情報の検索が行えること() {
        List<StudentCourse> actual = sut.searchStudentCourse("1");
        assertThat(actual.size()).isEqualTo(1);

        assertThat(actual.getFirst().getStudentId()).isEqualTo("1");
        assertThat(actual.getFirst().getCourseName()).isEqualTo("Java");
        assertThat(actual.getFirst().getCourseStartAt()).isEqualTo(LocalDateTime.of(2024, 4, 1, 0, 0,0));
        assertThat(actual.getFirst().getCourseEndAt()).isEqualTo(LocalDateTime.of(2025, 3, 31, 0, 0,0));
    }

    @Test
    void 受講生の登録が行われること() {
        Student student = new Student(
                null, "江並公史", "エナミコウジ", "エナミ",
                "test@example.com", "奈良県", 36, "男性", "", false
        );

        sut.registerStudent(student);

        List<Student> actual = sut.search();

        assertThat(actual.size()).isEqualTo(6);
    }

    @Test
    void 受講生コース情報の登録が行われること() {
        StudentCourse studentCourse = new StudentCourse(
                "2",
                "Java",
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1)
        );

        sut.registerStudentCourse(studentCourse);

        List<StudentCourse> actual = sut.searchStudentCoursesList();

        assertThat(actual.size()).isEqualTo(7);

    }

    @Test
    void 受講生の更新が行えること() {
        Student actual = sut.searchStudent("1");
        assertThat(actual).isNotNull();

        Student updatedStudent = new Student(
                actual.getId(),
                actual.getFullName(),
                actual.getFurigana(),
                actual.getNickname(),
                actual.getEmail(),
                actual.getRegion(),
                actual.getAge(),
                actual.getGender(),
                "更新しました。",
                actual.getDeleted()
        );

        sut.updateStudent(updatedStudent);

        Student updateActual = sut.searchStudent("1");
        assertThat(updateActual.getRemark()).isEqualTo("更新しました。");
    }

    @Test
    void 受講生コース情報のコース名の更新が行えること() {
        List<StudentCourse> actual = sut.searchStudentCourse("1");
        StudentCourse studentCourse1 = actual.get(0);
        studentCourse1.setCourseName("Java");
        sut.updateStudentCourse(studentCourse1);
        List<StudentCourse> updateactual = sut.searchStudentCourse("1");
        StudentCourse updatestudentCourse1 = updateactual.get(0);
        assertThat(updatestudentCourse1.getCourseName()).isEqualTo("Java");
    }

    @Test
    void 地域_年齢_性別で受講生を検索できること() {
        String region = "東京都";
        Integer age = 25;
        String gender = "男性";

        List<Student> students = sut.searchStudentsByCriteria(region, age, gender);

        assertThat(students).hasSize(1);
        assertThat(students.get(0).getFullName()).isEqualTo("田中太郎");
    }

    @Test
    void 地域_年齢_性別で受講生を検索し該当する受講生がいない場合は空のリストを返す() {
        String region = "Kyoto";
        Integer age = 30;
        String gender = "Male";

        List<Student> students = sut.searchStudentsByCriteria(region, age, gender);

        assertThat(students).isEmpty();
    }

    @Test
    void 地域_年齢_性別で条件を部分指定して検索できること() {
        String region = "東京都";
        Integer age = 25;
        String gender = "男性";

        List<Student> students = sut.searchStudentsByCriteria(region, age, gender);

        assertThat(students).hasSize(1);
        assertThat(students).extracting(Student::getFullName)
                .containsExactlyInAnyOrder("田中太郎");
    }

    @Test
    void 申し込み状況の検索が行えること() {
        String status = "仮申込";

        List<StudentCourse> result = sut.searchCoursesByStatus(status);

        assertThat(result).isNotEmpty();

        assertThat(result).allMatch(course -> status.equals(course.getStatus()));

        StudentCourse course = result.get(0);
        assertNotNull(course.getCourseName());
        assertEquals("仮申込", course.getStatus());
    }
}