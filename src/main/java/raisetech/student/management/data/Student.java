package raisetech.student.management.data;

import lombok.Getter;
import lombok.Setter;

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
