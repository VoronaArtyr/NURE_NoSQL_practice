package ua.nure.style.security;

import ua.nure.style.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    private final IMyDao dao = new DAOFactory(new ConnectionFactory()).getDao(DAOType.MYSQL);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .authorizeRequests()

                .antMatchers("/**").permitAll()
//                .antMatchers("/css/**").permitAll()
//                .antMatchers("/login").permitAll()
//                .antMatchers("/register").permitAll()


                .anyRequest().authenticated()

                .and()

                .formLogin()
                .defaultSuccessUrl("/user/", true)
                .and()
                .logout().logoutSuccessUrl("/").invalidateHttpSession(true);
    }


    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        return new JdbcUserDetailsService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.userAuthenticationProvider);
    }
}
