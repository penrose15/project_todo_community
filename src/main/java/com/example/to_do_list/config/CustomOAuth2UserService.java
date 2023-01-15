//package com.example.to_do_list.config;
//
//import com.example.to_do_list.domain.Users;
//import com.example.to_do_list.domain.role.Role;
//import com.example.to_do_list.repository.UsersRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.security.AuthProvider;
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Service
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//
//    private final UsersRepository usersRepository;
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//
//        return process(userRequest, oAuth2User);
//    }
//
//    private OAuth2User process(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
//
//        OAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
//
//        if(userInfo.getEmail().isEmpty()) {
//            throw new IllegalArgumentException("email not found");
//        }
//        System.out.println(userInfo.getEmail());
//        Optional<Users> userOptional = usersRepository.findByEmail(userInfo.getEmail());
//        Users user;
//
//        // 이미 가입된 경우
//        // 가입되지 않은 경우
//        user = userOptional.orElseGet(() -> createUser(userInfo));
//
//        return CustomUserDetails.create(user, oAuth2User.getAttributes());
//    }
//
//    private Users createUser(OAuth2UserInfo userInfo) {
//        Users users = Users.builder()
//                .email(userInfo.getEmail())
//                .profile(userInfo.getProfile())
//                .role(Role.USER)
//                .username(userInfo.getName())
//                .build();
//        return usersRepository.save(users);
//    }
//}