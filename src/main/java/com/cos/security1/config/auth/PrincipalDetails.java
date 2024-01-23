package com.cos.security1.config.auth;


import com.cos.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

//시큐리티가 /login 주소 요청을 가로채서 로그인을 진행시킨다.
//진행이 완료되면 시큐리티 session을 만들어 줌(Security ContextHolder)
//오브젝트 > Authentication 타입 객체
//Authentication 안에 User 정보가 있어야 함 > User 오브젝트의 타입 > UserDetails 타입 객체
//Security Session > Authentication > UserDetails(PrincipalDetails)
public class PrincipalDetails implements UserDetails {

    private User user; //컴포지션

    public PrincipalDetails(User user) {
        this.user = user;
    }

    @Override //해당 User의 권한을 리턴하는 곳
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        //* 휴면 계정 처리 방법
        //내 사이트에서 1년 동안 로그인을 안 할 시 휴먼 처리를 하기로 했을 때
        //테이블에서 로그인데이트 값을 받아와서
        //현재 시간 - 로그인 시간 = 1년을 초과하면 return false로 변경
        return true;
    }
}
