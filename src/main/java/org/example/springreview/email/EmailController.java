package org.example.springreview.email;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springreview.CommonResponseDto;
import org.example.springreview.exception.CustomException;
import org.example.springreview.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final MailSendService mailService;

    @PostMapping("/api/users/mail")
    public String mailSend(@RequestBody @Valid EmailRequestDto emailDto) {
        System.out.println("이메일 인증 이메일 :" + emailDto.getEmail());
        return mailService.joinEmail(emailDto.getEmail());
    }

    @PostMapping("/api/users/mail/auth")
    public ResponseEntity<CommonResponseDto> AuthCheck(@RequestBody @Valid EmailCheckDto emailCheckDto){
        Boolean Checked = mailService.CheckAuthNum(emailCheckDto.getEmail(),emailCheckDto.getAuthNum());
        if(Checked){
            return ResponseEntity.status(HttpStatus.OK.value()).body(new CommonResponseDto("인증이 완료되었습니다", HttpStatus.OK.value()));
        }
        else{
            throw new CustomException(ErrorCode.EMAIL_AUTH_FAIL);
        }
    }
}
