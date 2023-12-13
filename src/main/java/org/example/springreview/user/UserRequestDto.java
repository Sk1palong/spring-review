package org.example.springreview.user;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    @Pattern(regexp = "^[a-zA-Z0-9]{3,}$", message = "username은 알파벳 대소문자(a~z, A~Z), 숫자(0~9) 3자 이상으로 구성되어야 합니다.")
    private String username;

    @Size(min = 3, message = "비밀번호는 4자 이상으로 구성되어야 합니다.")
    private String password;

    private String passwordConfirm;
}
