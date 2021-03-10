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

    private final UserDetailsSecurityService userDetailsSecurityService;

//    private final DataSource dataSource;

    @Autowired
    public SecurityConfig(UserDetailsSecurityService userDetailsSecurityService){
        this.userDetailsSecurityService = userDetailsSecurityService;
//        this.dataSource = dataSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/register").permitAll()
                    .antMatchers("/images/**").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/js/**").permitAll()
                    .antMatchers("/aroma-template/**").permitAll()
                    .antMatchers("/colorlib-addProduct/**").permitAll()
                    .antMatchers("/addProduct").hasRole("ADMIN")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll()
                    .defaultSuccessUrl("/home") // will redirect to home page after login
                    .and()
                .rememberMe()
                    .key("test")
//                    .rememberMeCookieName("remember-me-cookie")
                    .rememberMeParameter("remember_me")  // remember-me field name in form.
//                    .tokenRepository(this.persistentTokenRepository())
//                    .tokenValiditySeconds(1*24*60*60)
                  .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                    .deleteCookies("JSESSIONID");
//                .permitAll()

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public void globalConfig(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsSecurityService).passwordEncoder(bCryptPasswordEncoder());
    }

//    @Bean
//    public PersistentTokenRepository persistentTokenRepository() {
//        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
//        db.setDataSource(dataSource);
//
//        return db;
//    }

}
