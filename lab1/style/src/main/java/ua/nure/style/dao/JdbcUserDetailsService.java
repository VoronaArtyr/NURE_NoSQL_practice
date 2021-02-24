package ua.nure.style.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JdbcUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        var user = this.userDAO.getByEmail(s).get();
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().toString().toUpperCase())
                .build();
    }
}
