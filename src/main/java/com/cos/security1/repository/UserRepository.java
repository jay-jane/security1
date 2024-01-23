package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// CRUD 함수를 JpaRepository가 갖고 있음
// JpaRepository를 상속했기 때문에 @Repository 어노테이션이 없어도 IoC가 됨.
public interface UserRepository extends JpaRepository<User, Long> {

    //findBy 규칙 > Username 문법
    //select * from user where username = ?;
    public User findByUsername(String username); //jpa query method


//    public List<User> findAllByUsernameAndPassword(String username, Stirng ps);
}
