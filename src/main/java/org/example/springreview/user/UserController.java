package org.example.springreview.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springreview.CommonResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<CommonResponseDto> signup(@Valid @RequestBody UserRequestDto requestDto, BindingResult bindingResult){
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for(FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
        }
        userService.signup(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new CommonResponseDto("회원가입 성공", HttpStatus.CREATED.value()));

    }

    @GetMapping("/users/{username}")
    public ResponseEntity<CommonResponseDto> checkUsername(@PathVariable String username) {
        userService.checkUsername(username);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new CommonResponseDto("사용가능한 닉네임입니다.", HttpStatus.OK.value()));
    }

}

