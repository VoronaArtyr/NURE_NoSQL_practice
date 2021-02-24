package ua.nure.style.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JdbcUserDetailsService implements UserDetailsService {

    private IMyDao dao = new DAOFactory(new ConnectionFactory()).getDao(DAOType.MYSQL);

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        var user = this.dao.getUserByEmail(s).get();
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().toString().toUpperCase())
                .build();
    }
}
