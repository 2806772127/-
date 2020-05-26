package com.fb.goldencudgel.auditDecisionSystem.security;

import com.fb.goldencudgel.auditDecisionSystem.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

     @Override
     public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
     {
//         if (user == null){
//             throw new UsernameNotFoundException("用户不存在");
//         }
         List<SimpleGrantedAuthority> authorities = new ArrayList<>();        //对应的权限添加
          authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
          User user = new User();
          return user;
     }

}
