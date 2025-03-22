package raisetech.student.management.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.StudentNotFoundException;
import raisetech.student.management.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。
 * 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

    private final StudentRepository repository;
    private final StudentConverter converter;

    @Autowired
    public StudentService(StudentRepository repository, StudentConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    /**
     * 地域・年齢・性別で受講生を検索する
     *
     * @param region 地域 (null可)
     * @param age 年齢 (null可)
     * @param gender 性別 (null可)
     * @return 検索条件に一致する受講生リスト
     */
    public List<Student> searchStudentsByCriteria(String region, Integer age, String gender) {
        return repository.searchStudentsByCriteria(region, age, gender);
    }

    /**
     * 受講生詳細の一覧検索を行う（全件検索）
     */
    public List<StudentDetail> searchStudentList() {
        List<Student> studentList = repository.search();
        List<StudentCourse> studentCourseList = repository.searchStudentCoursesList();
        return converter.convertStudentDetails(studentList, studentCourseList);
    }

    /**
     * 受講生詳細検索（ID指定）
     */
    public StudentDetail searchStudent(String id) {
        Student student = repository.searchStudent(id);
        if (student == null) {
            throw new StudentNotFoundException("Student with id " + id + " not found");
        }
        List<StudentCourse> studentsCourse = repository.searchStudentCourse(student.getId());
        return new StudentDetail(student, studentsCourse);
    }

    /**
     * 受講生の登録
     */
    @Transactional
    public StudentDetail registerStudent(StudentDetail studentDetail) {
        Student student = studentDetail.getStudent();
        repository.registerStudent(student);
        studentDetail.getStudentCourseList().forEach(studentCourse -> {
            initStudentsCourse(studentCourse, student);
            repository.registerStudentCourse(studentCourse);
        });
        return studentDetail;
    }

    /**
     * 受講生コース情報の初期設定
     */
    private static void initStudentsCourse(StudentCourse studentCourse, Student student) {
        LocalDateTime now = LocalDateTime.now();
        studentCourse.setStudentId(student.getId());
        studentCourse.setCourseStartAt(now);
        studentCourse.setCourseEndAt(now.plusYears(1));
    }

    /**
     * 受講生情報の更新
     */
    @Transactional
    public void updateStudent(StudentDetail studentDetail) {
        repository.updateStudent(studentDetail.getStudent());
        studentDetail.getStudentCourseList()
                .forEach(studentCourse -> repository.updateStudentCourse(studentCourse));
    }
}
