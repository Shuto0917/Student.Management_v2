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
    private final String id;
    private final String fullName;
    private final String furigana;
    private final String nickname;
    private final String email;
    private final String region;
    private final int age;
    private final String gender;
    private final String remark;
    private final boolean deleted;

    public Student(String id, String fullName, String furigana, String nickname,
                   String email, String region, int age, String gender,
                   String remark, boolean deleted) {
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
        return age == student.age &&
                deleted == student.deleted &&
                Objects.equals(id, student.id) &&
                Objects.equals(fullName, student.fullName) &&
                Objects.equals(furigana, student.furigana) &&
                Objects.equals(nickname, student.nickname) &&
                Objects.equals(email, student.email) &&
                Objects.equals(region, student.region) &&
                Objects.equals(gender, student.gender) &&
                Objects.equals(remark, student.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, furigana, nickname, email, region, age, gender, remark, deleted);
    }
}
