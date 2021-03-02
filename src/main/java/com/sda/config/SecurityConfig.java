package com.sda.config;

import com.sda.service.UserDetailsSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsSecurityService userDetailsSecurityService;

    @Autowired
    public SecurityConfig(UserDetailsSecurityService userDetailsSecurityService){
        this.userDetailsSecurityService = userDetailsSecurityService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/static/js/**").permitAll()
                .antMatchers("/aroma-template/**").permitAll()
                .antMatchers("/addProduct").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/home") // will redirect to home page after login
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .permitAll();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public void globalConfig(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsSecurityService).passwordEncoder(bCryptPasswordEncoder());
    }
}
