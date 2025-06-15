package raisetech.student.management.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.service.StudentService;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService service;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
        mockMvc.perform(get("/students/studentsList"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(service, times(1)).searchStudentList();
    }

    @Test
    void 受講生詳細の検索が実行できて空で返ってくること() throws Exception {
        String id = "999";
        mockMvc.perform(get("/students/{id}", id))
                .andExpect(status().isOk());

        verify(service, times(1)).searchStudent(id);
    }

    @Test
    void 受講生詳細の登録が実行できて空で返ってくること() throws Exception {
        mockMvc.perform(post("/students/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                    "student": {
                                        "fullName": "江並康介",
                                        "furigana": "エナミ",
                                        "nickname": "コウジ",
                                        "email": "test@example.com",
                                        "region": "奈良",
                                        "age": "36",
                                        "gender": "男性",
                                        "remark": ""
                                    },
                                    "studentCourseList": [
                                        {
                                            "courseName": "Javaコース"
                                        }
                                    ]
                                }
                                """
                        ))
                .andExpect(status().isOk());

        verify(service, times(1)).registerStudent(any());
    }

    @Test
    void 受講生詳細の更新が実行できて空で返ってくること() throws Exception {
        mockMvc.perform(put("/students/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                    "student": {
                                        "id": "12",
                                        "fullName": "江並康介",
                                        "furigana": "エナミ",
                                        "nickname": "コウジ",
                                        "email": "test@example.com",
                                        "region": "奈良",
                                        "age": "36",
                                        "gender": "男性",
                                        "remark": ""
                                    },
                                    "studentCourseList": [
                                        {
                                            "id": "8",
                                            "studentId": "12",
                                            "courseName": "Javaコース",
                                            "courseStartAt": "2024-12-29T00:00:00",
                                            "courseEndAt": "2025-12-29T00:00:00"
                                        }
                                    ]
                                }
                                """
                        ))
                .andExpect(status().isOk());

        verify(service, times(1)).updateStudent(any());
    }

    /*
    @Test
    void 受講生詳細の例外APIが実行できてステータスが400で返ってくること() throws Exception {
        mockMvc.perform(get("/exception"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("このAPIは現在利用できません。古いURLとなっています。"));
    }
    */

    @Test
    void 受講生詳細の受講生で適切な値を入力したときに入力チェックに異常が発生しないこと() {
        Student student = new Student(
                "1", "江並公史", "エナミコウジ", "エナミ",
                "test@example.com", "奈良県", 0, "男性", null, false
        );

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void 受講生詳細の受講生でIDに数字以外を用いた時に入力チェックに掛かること() {
        Student student = new Student(
                "あ", "江並公史", "エナミコウジ", "エナミ",
                "test@example.com", "奈良県", 0, "男性", null, false
        );

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations).extracting("message")
                .containsOnly("数字のみ入力するようにしてください。");
    }

    @Test
    void 地域_年齢_性別で受講生が検索できて空で返ってくること() throws Exception {
        Student student1 = new Student("1", "田中太郎", "タナカタロウ", "タロウ",
                "abc@example.com", "Tokyo", 25, "Male", null, false);
        Student student2 = new Student("2", "佐藤健一", "サトウケンイチ", "ケンイチ",
                "ghi@example.com", "Tokyo", 25, "Male", null, false);
        List<Student> expectedStudents = List.of(student1, student2);

        when(service.searchStudentsByCriteria("Tokyo", 25, "Male")).thenReturn(expectedStudents);

        mockMvc.perform(get("/students/search")
                        .param("region", "Tokyo")
                        .param("age", "25")
                        .param("gender", "Male"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    [
                        {"id":"1","fullName":"田中太郎","region":"Tokyo","age":25,"gender":"Male"},
                        {"id":"2","fullName":"佐藤健一","region":"Tokyo","age":25,"gender":"Male"}
                    ]
                """));

        verify(service, times(1)).searchStudentsByCriteria("Tokyo", 25, "Male");
    }

    @Test
    void 地域_年齢_性別で受講生を検索_該当する受講生がいない場合は空のリストを返す() throws Exception {
        when(service.searchStudentsByCriteria("Osaka", 30, "Female")).thenReturn(List.of());

        mockMvc.perform(get("/students/search")
                        .param("region", "Osaka")
                        .param("age", "30")
                        .param("gender", "Female"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(service, times(1)).searchStudentsByCriteria("Osaka", 30, "Female");
    }

    @Test
    void 地域_年齢_性別で受講生を検索_性別のみ指定した場合でも検索が実行できて空で返ってくること() throws Exception {
        Student student1 = new Student("1", "田中太郎", "タナカタロウ", "タロウ",
                "abc@example.com", "Tokyo", 25, "Male", null, false);
        Student student2 = new Student("2", "佐藤健一", "サトウケンイチ", "ケンイチ",
                "ghi@example.com", "Osaka", 30, "Male", null, false);
        List<Student> expectedStudents = List.of(student1, student2);

        when(service.searchStudentsByCriteria(null, null, "Male")).thenReturn(expectedStudents);

        mockMvc.perform(get("/students/search")
                        .param("gender", "Male"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    [
                        {"id":"1","fullName":"田中太郎","region":"Tokyo","age":25,"gender":"Male"},
                        {"id":"2","fullName":"佐藤健一","region":"Osaka","age":30,"gender":"Male"}
                    ]
                """));

        verify(service, times(1)).searchStudentsByCriteria(null, null, "Male");
    }

    @Test
    void 申込状況が検索ができて結果が返ってくること() throws Exception {
        StudentCourse course1 = StudentCourse.builder()
                .id(1)
                .studentId("1")
                .courseName("Java")
                .status("仮申込")
                .build();

        List<StudentCourse> courseList = List.of(course1);

        when(service.searchCoursesByStatus("仮申込")).thenReturn(courseList);

        mockMvc.perform(get("/students/courses/search")
                        .param("status", "仮申込"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    [
                        {"id":1,"studentId":"1","courseName":"Java","status":"仮申込"}
                    ]
                """));

        verify(service, times(1)).searchCoursesByStatus("仮申込");
    }

    @Test
    void 存在しない申込状況を指定した場合は空のリストが返ること() throws Exception {
        when(service.searchCoursesByStatus("仮申込")).thenReturn(List.of());

        mockMvc.perform(get("/students/courses/search")
                        .param("status", "仮申込"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(service, times(1)).searchCoursesByStatus("仮申込");
    }
}