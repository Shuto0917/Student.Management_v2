package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Schema(description = "受講生")
@Getter
@Setter
public class Student {

    @Pattern(regexp = "^[0-9]+$", message = "数字のみ入力するようにしてください。")
    private String id;

    private String fullName;
    private String furigana;
    private String nickname;
    private String email;
    private String region;
    private Integer age;
    private String gender;
    private String remark;

    @Getter
    private Boolean deleted;

    public Student(String id, String fullName, String furigana, String nickname,
                   String email, String region, Integer age, String gender,
                   String remark, Boolean deleted) {
        this.id = id;
        this.fullName = fullName;
        this.furigana = furigana;
        this.nickname = nickname;
        this.email = email;
        this.region = region;
        this.age = age;
        this.gender = gender;
        this.remark = remark;
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) &&
                Objects.equals(fullName, student.fullName) &&
                Objects.equals(furigana, student.furigana) &&
                Objects.equals(nickname, student.nickname) &&
                Objects.equals(email, student.email) &&
                Objects.equals(region, student.region) &&
                Objects.equals(age, student.age) &&
                Objects.equals(gender, student.gender) &&
                Objects.equals(remark, student.remark) &&
                Boolean.TRUE.equals(deleted) == Boolean.TRUE.equals(student.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, furigana, nickname, email, region, age, gender, remark, deleted);
    }
}