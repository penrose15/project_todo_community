package com.example.to_do_list.service;

import com.example.to_do_list.common.redis.redisTemplateRepository;
import com.example.to_do_list.dto.email.EmailMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final redisTemplateRepository redisTemplateRepository;

    public String getTmpPassword() {
        char[] charset = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String tmpPassword = "";
        for(int i = 0; i<10; i++) {
            tmpPassword += charset[(int) (Math.random() * charset.length)];

        }
        return tmpPassword;
    }

    public EmailMessageDto Message(String email, String tmpPassword) {
        String[] to = new String[]{email};

        EmailMessageDto emailMessageDto = EmailMessageDto.builder()
                .to(to)
                .subject("안녕하세요 " + email + " 님")
                .text("임시비밀번호는 : " + tmpPassword + " 입니다.")
                .build();

        return emailMessageDto;
    }

    public void saveAuthCode(String email, String authCode) {
        redisTemplateRepository.saveAuthCode(email, authCode);
    }

    public boolean verifyAuthCode(String email, String authCode) {
        String code;
        try {
            code = redisTemplateRepository.findByEmail(email);
        } catch (Exception e) {
            return false;
        }
        if(!code.equals(authCode)) {
            return false;
        }
        return true;
    }


}
