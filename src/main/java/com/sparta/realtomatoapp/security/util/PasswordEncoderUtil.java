package com.sparta.realtomatoapp.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderUtil {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 비밀번호를 암호화합니다.
     *
     * @param rawPassword 암호화할 원본 비밀번호
     * @return 암호화된 비밀번호
     */
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 원본 비밀번호와 암호화된 비밀번호가 일치하는지 확인합니다.
     *
     * @param rawPassword 원본 비밀번호
     * @param encodedPassword 암호화된 비밀번호
     * @return 비밀번호가 일치하면 true, 그렇지 않으면 false
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
