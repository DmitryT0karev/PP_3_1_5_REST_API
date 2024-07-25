package ru.kata.spring.boot_security.REST.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kata.spring.boot_security.REST.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SuccessUserHandler successUserHandler;

    private UserService userService;

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
//                .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
//                .antMatchers("/api/admin/**").hasAnyAuthority("ADMIN")
//                .antMatchers("/user/**").hasAnyAuthority("USER", "ADMIN")
//                .antMatchers("/api/user/**").hasAnyAuthority("USER", "ADMIN")
//                .anyRequest().authenticated()
                .antMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.GET, "/user/**").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.POST, "/admin").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/admin/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/admin/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/admin/**").hasAnyAuthority("ADMIN")
                .and()
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .usernameParameter("email")
                .successHandler(successUserHandler)
                .failureUrl("/auth/login?error")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login")
                .permitAll();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}