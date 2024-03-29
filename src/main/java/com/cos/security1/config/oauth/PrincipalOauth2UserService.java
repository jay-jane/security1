package com.cos.security1.config.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

//해당 메서드 종료 시 @AuthenticationPrincipal 어노테이션이 생성됨
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override //소셜(구글) 로그인 진행 후 후처리 되는 메서드 - 구글로부터 받은 userRequest 데이터 처리
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("ClientRegistration : " + userRequest.getClientRegistration()); //registrationId로 어떤 oauth로 로그인했는지 판별
        System.out.println("RegistrationId : " + userRequest.getClientRegistration().getRegistrationId());
        System.out.println("AccessToken : " + userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        //구글 로그인 버튼 클릭 > 구글 로그인 창 > 로그인 완료 > code 리턴(oauth2라이브러리) > AccessToken 요청
        //userRequest 정보 받음 > loadUser 메서드 호출 > 구글로부터 회원 프로필 받음
        System.out.println("Attributes : " + oAuth2User.getAttributes());
        //회원가입 진행
        String provider = userRequest.getClientRegistration().getClientId(); //google
        String providerId = oAuth2User.getAttribute("sub"); //104490113872731533198
        String username = provider + "_" + providerId; //google_104490113872731533198
        String password = bCryptPasswordEncoder.encode("안녕하세요"); //의미없음
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            System.out.println("구글 로그인이 최초입니다");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        } else {
            System.out.println("구글 로그인을 이미 한 적이 있습니다");
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes()); //PrincipalDetails 객체를 리턴 받기 위해 오버라이드 한 것
    }
}
