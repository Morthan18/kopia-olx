package com.snokant.projekt.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
@Order(1)
@Configuration
public class BasicAuthConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    DataSource dataSource;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .httpBasic()
                .and()
                .authorizeRequests().antMatchers("/witam").permitAll()
                .antMatchers("/rest/user/testuj").hasAnyRole("ROLE_User","ROLE_Admin");
               // .and().logout();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("SELECT email,password,true FROM users where email=?")

                .authoritiesByUsernameQuery("SELECT users.email,roles.role_name FROM users INNER JOIN roles ON users.role_id=roles.role_id WHERE users.email=?");
    }
}
