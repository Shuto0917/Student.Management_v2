package raisetech.student.management.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Size;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.Student;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです。
 */
@Validated
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * 受講生詳細の一覧検索
     */
    @Operation(summary = "一覧検索", description = "受講生の一覧を検索します。")
    @GetMapping("/list")
    public List<StudentDetail> getStudentList() {
        return studentService.searchStudentList();
    }

    /**
     * 受講生詳細の検索 (ID指定)
     */
    @Operation(summary = "受講生ID", description = "受講生のIDで検索をします。")
    @GetMapping("/{id}")
    public StudentDetail getStudent(@PathVariable String id) {
        return studentService.searchStudent(id);
    }

    /**
     * 受講生詳細の登録
     */
    @Operation(summary = "受講生登録", description = "受講生を登録します。")
    @PostMapping("/register")
    public ResponseEntity<StudentDetail> registerStudent(@RequestBody StudentDetail studentDetail) {
        StudentDetail responseStudentDetail = studentService.registerStudent(studentDetail);
        return ResponseEntity.ok(responseStudentDetail);
    }

    /**
     * 受講生詳細の更新
     */
    @Operation(summary = "受講生更新", description = "受講生情報の更新をします。")
    @PutMapping("/update")
    public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
        studentService.updateStudent(studentDetail);
        return ResponseEntity.ok("更新処理が成功しました。");
    }

    /**
     * 地域・年齢・性別で受講生を検索
     */
    @Operation(summary = "受講生検索", description = "地域、年齢、性別で受講生を検索します。")
    @GetMapping("/search")
    public List<Student> searchStudentsByCriteria(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String gender) {
        return studentService.searchStudentsByCriteria(region, age, gender);
    }

    /**
     * 例外処理: NotFoundException
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
