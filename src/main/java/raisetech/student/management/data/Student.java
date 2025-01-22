package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
@Schema(description = "受講生")
@Getter
@Setter

public class Student {


    private String id;
    private String fullName;
    private String furigana;
    private String nickname;
    private String email;
    private String region;
    private int age;
    private String gender;
    private String remark;
    private boolean isDeleted;
}
