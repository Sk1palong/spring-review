package org.example.springreview.user;

import lombok.RequiredArgsConstructor;
import org.example.springreview.exception.CustomException;
import org.example.springreview.exception.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(UserRequestDto requestDto){
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String passwordConfirm = requestDto.getPasswordConfirm();
        String encodedPassword = passwordEncoder.encode(password);

        for (int i = 0; i < username.length(); i++) {
            if (username.substring(i).startsWith(password)) {
                throw new CustomException(ErrorCode.ID_PW_SAME);
            }
        }

        if (!password.equals(passwordConfirm)) {
            throw new CustomException(ErrorCode.WRONG_PASSWORDCONFIRM_ERROR);
        }

        checkUsername(username);

        User user = new User(username, encodedPassword);

        userRepository.save(user);
    }
    public void checkUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER);
        }
    }
}
