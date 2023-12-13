package org.example.springreview.security;

import lombok.RequiredArgsConstructor;
import org.example.springreview.user.User;
import org.example.springreview.user.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsImpl getUserDetails(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("NotFound" + username));
        return new UserDetailsImpl(user);
    }
}
