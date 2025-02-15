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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import raisetech.student.management.data.Student;
import raisetech.student.management.service.StudentService;

import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService service;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
        mockMvc.perform(get("/studentsList"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(service, times(1)).searchStudentList();
    }

    @Test
    void 受講生詳細の検索が実行できて空で返ってくること() throws Exception {
        String id = "999";
        mockMvc.perform(get("/Student/{id}", id))
                .andExpect(status().isOk());

        verify(service, times(1)).searchStudent(id);
    }

    @Test
    void 受講生詳細の登録が実行できて空で返ってくること()
            throws Exception {
        mockMvc.perform(post("/registerStudent").contentType(MediaType.APPLICATION_JSON).content(
                        """
                           {
                              "student":{
                                  "fullName" : "江並康介",
                                  "furigana" : "エナミ",
                                  "nickname" : "コウジ",
                                  "email" : "test@example.com",
                                  "region" : "奈良",
                                  "age" : "36",
                                  "gender" : "男性",
                                  "remark" : ""
                                  },
                                  "studentCourseList" : [
                                  {
                                       "courseName" : "Javaコース"
                                       }
                                       ]
                                       }
                                """
                ))
                .andExpect(status().isOk());

        verify(service, times(1)).registerStudent(any());
    }

    @Test
    void 受講生詳細の更新が実行できて空で返ってくること()
            throws Exception {
        mockMvc.perform(put("/updateStudent").contentType(MediaType.APPLICATION_JSON).content(
                        """
                           {
                              "student":{
                                  "id" : "12",
                                  "fullName" : "江並康介",
                                  "furigana" : "エナミ",
                                  "nickname" : "コウジ",
                                  "email" : "test@example.com",
                                  "region" : "奈良",
                                  "age" : "36",
                                  "gender" : "男性",
                                  "remark" : ""
                                  },
                                  "studentCourseList" : [
                                  {
                                       "id": "8",
                                       "studentId": "12",
                                       "courseName" : "Javaコース",
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

    @Test
    void 受講生詳細の例外APIが実行できてステータスが400で返ってくること() throws Exception {
        mockMvc.perform(get("/exception"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("このAPIは現在利用できません。古いURLとなっています。"));
    }

    @Test
    void 受講生詳細の受講生で適切な値を入力したときに入力チェックに異常が発生しないこと() {
        Student student = new Student();
        student.setId("1");
        student.setFullName("江並公史");
        student.setFurigana("エナミコウジ");
        student.setNickname("エナミ");
        student.setEmail("test@example.com");
        student.setRegion("奈良県");
        student.setGender("男性");

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void 受講生詳細の受講生でIDに数字以外を用いた時に入力チェックに掛かること() {
        Student student = new Student();
        student.setId("テストです。");
        student.setFullName("江並公史");
        student.setFurigana("エナミコウジ");
        student.setNickname("エナミ");
        student.setEmail("test@example.com");
        student.setRegion("奈良県");
        student.setGender("男性");

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations).extracting("message")
                .containsOnly("数字のみ入力するようにしてください。");
    }
}