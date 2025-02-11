package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @Mock
    private StudentConverter converter;

    private StudentService sut;

    @BeforeEach
    void before() {
        sut = new StudentService(repository, converter);

    }

    @Test
        void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {

            List<Student> studentList = new ArrayList<>();
            List<StudentCourse> studentCourseList = new ArrayList<>();
            List<StudentDetail> expectedStudentDetails = new ArrayList<>();

            when(repository.search()).thenReturn(studentList);
            when(repository.searchStudentCoursesList()).thenReturn(studentCourseList);
            when(converter.convertStudentDetails(studentList, studentCourseList)).thenReturn(expectedStudentDetails);

            List<StudentDetail> actualStudentDetails = sut.searchStudentList();

            assertEquals(expectedStudentDetails, actualStudentDetails);

            verify(repository, times(1)).search();
            verify(repository, times(1)).searchStudentCoursesList();
            verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
        }

    @Test
    void 受講生詳細の検索_リポジトリの処理が適切に呼び出せていること() {
        String studentId = "1";
        Student student = new Student();
        student.setId(studentId);
        student.setFullName("Test User");

        List<StudentCourse> studentCourses = new ArrayList<>();
        StudentCourse course = new StudentCourse();
        course.setStudentId(studentId);
        course.setCourseName("Javaコース");
        studentCourses.add(course);

        when(repository.searchStudent(studentId)).thenReturn(student);
        when(repository.searchStudentCourse(studentId)).thenReturn(studentCourses);

        StudentDetail result = sut.searchStudent(studentId);

        assertNotNull(result);
        assertEquals(studentId, result.getStudent().getId());
        assertEquals("Test User", result.getStudent().getFullName());
        assertEquals(1, result.getStudentCourseList().size());
        assertEquals("Javaコース", result.getStudentCourseList().get(0).getCourseName());

        verify(repository, times(1)).searchStudent(studentId);
        verify(repository, times(1)).searchStudentCourse(studentId);
    }
    @Test
    void 受講生詳細の検索_IDに紐づく受講生が存在しない場合は例外をスローする() {
        String studentId = "999";

        when(repository.searchStudent(studentId)).thenReturn(null);

        StudentService.StudentNotFoundException thrown = assertThrows(StudentService.StudentNotFoundException.class, () -> {
            sut.searchStudent(studentId);
        });

        assertEquals("Student with id 999 not found", thrown.getMessage());

        verify(repository, times(1)).searchStudent(studentId);
        verify(repository, never()).searchStudentCourse(anyString());
    }

    @Test
    void 受講生詳細の登録_リポジトリの処理が適切に呼び出せていること() {
        Student student = new Student();
        student.setId("1");
        student.setFullName("Test User");

        StudentCourse course1 = new StudentCourse();
        course1.setCourseName("Javaコース");

        StudentCourse course2 = new StudentCourse();
        course2.setCourseName("Spring Framework");

        List<StudentCourse> studentCourses = new ArrayList<>();
        studentCourses.add(course1);
        studentCourses.add(course2);

        StudentDetail studentDetail = new StudentDetail(student, studentCourses);

        doNothing().when(repository).registerStudent(any(Student.class));
        doNothing().when(repository).registerStudentCourse(any(StudentCourse.class));

        StudentDetail result = sut.registerStudent(studentDetail);

        assertNotNull(result);
        assertEquals(student.getId(), result.getStudent().getId());
        assertEquals(student.getFullName(), result.getStudent().getFullName());
        assertEquals(2, result.getStudentCourseList().size());
        assertEquals("Javaコース", result.getStudentCourseList().get(0).getCourseName());
        assertEquals("Spring Framework", result.getStudentCourseList().get(1).getCourseName());

        verify(repository, times(1)).registerStudent(student);
        verify(repository, times(2)).registerStudentCourse(any(StudentCourse.class));

        assertEquals("1", studentCourses.get(0).getStudentId());
        assertEquals("1", studentCourses.get(1).getStudentId());
    }
    @Test
    void 受講生詳細の登録_受講生情報の登録に失敗した場合() {
        Student student = new Student();
        student.setId("1");
        student.setFullName("Test User");

        StudentCourse course1 = new StudentCourse();
        course1.setCourseName("Javaコース");

        StudentCourse course2 = new StudentCourse();
        course2.setCourseName("Spring Framework");

        List<StudentCourse> studentCourses = new ArrayList<>();
        studentCourses.add(course1);
        studentCourses.add(course2);

        StudentDetail studentDetail = new StudentDetail(student, studentCourses);

        doThrow(new RuntimeException("Failed to register student")).when(repository).registerStudent(any(Student.class));

        assertThrows(RuntimeException.class, () -> sut.registerStudent(studentDetail));

        verify(repository, times(1)).registerStudent(student);
        verify(repository, never()).registerStudentCourse(any(StudentCourse.class));
    }

    @Test
    void 受講生詳細の更新＿リポジトリの処理が適切に呼び出せていること() {
        Student student = new Student();
        StudentCourse studentsCourses = new StudentCourse();
        List<StudentCourse> studentsCoursesList = List.of(studentsCourses);
        StudentDetail studentDetail = new StudentDetail(student, studentsCoursesList);

        sut.updateStudent(studentDetail);

        Mockito.verify(repository, times(1)).updateStudent(student);
        Mockito.verify(repository, times(1)).updateStudentCourse(studentsCourses);
    }
    @Test
    void 受講生詳細の更新_受講生情報の更新に失敗した場合() {
        Student student = new Student();
        student.setId("1");
        student.setFullName("Test User");

        StudentCourse studentsCourses = new StudentCourse();
        studentsCourses.setStudentId("1");
        List<StudentCourse> studentsCoursesList = List.of(studentsCourses);
        StudentDetail studentDetail = new StudentDetail(student, studentsCoursesList);

        doThrow(new RuntimeException("Failed to update student")).when(repository).updateStudent(any(Student.class));

        assertThrows(RuntimeException.class, () -> sut.updateStudent(studentDetail));

        verify(repository, times(1)).updateStudent(student);
        verify(repository, never()).updateStudentCourse(any(StudentCourse.class));
    }
}