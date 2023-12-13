package com.guille.WebSalud20;

import com.guille.WebSalud20.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioServicio).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/panelAdmin/*").hasRole("ADMIN")
                .antMatchers("/historia_clinica/*").hasAnyRole("PACIENTE","PROFESIONAL")
                .antMatchers("/paciente/*").hasAnyRole("ADMIN", "USER","PACIENTE")
                .antMatchers("/profesional/*").hasAnyRole("ADMIN", "PROFESIONAL")
                .antMatchers("/css/*", "/js/*", "/img/*", "/**", "/portal/*","/util/*").permitAll()
                .and()
                .formLogin()
                .loginPage("/portal/login")
                .loginProcessingUrl("/logincheck")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .permitAll()
                .and().logout()
                .logoutUrl("/portal/logout")
                .logoutSuccessUrl("/")
                .permitAll()

                .and().csrf()
                .disable();
    }


}
