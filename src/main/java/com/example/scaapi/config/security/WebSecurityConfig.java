package com.example.scaapi.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    final UsuarioDetailsService userDetailsService;

    private final JwtService jwtService;

    public WebSecurityConfig(UsuarioDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtAuthFilter(jwtService, userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/usuarios/**").permitAll()
                .antMatchers("/swagger-ui.html/**").permitAll()
                .antMatchers("/api/v1/medicos/**").permitAll()
                .antMatchers("/api/v1/tecnicos/**").permitAll()
                .antMatchers("/api/v1/jogadores/**").permitAll()
                .antMatchers("/api/v1/campeonatos/**").permitAll()
                .antMatchers("/api/v1/estadios/**").permitAll()
                .antMatchers("/api/v1/times-adversarios/**").permitAll()
                .antMatchers("/api/v1/posicoes/**").permitAll()
                .antMatchers("/api/v1/partidas/**").permitAll()
                .antMatchers("/api/v1/temporadas/**").permitAll()
                .antMatchers("/api/v1/lesoes/**").permitAll()
                .antMatchers("/api/v1/lesoes-jogador/**").permitAll()
                .antMatchers("/api/v1/scouts-jogador/**").permitAll()
                .antMatchers("/api/v1/scouts-partida/**").permitAll()
                .antMatchers("/api/v1/relacionados/**").permitAll()
                .antMatchers("/api/v1/locais/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/usuarios/**").permitAll()
//                .antMatchers("/swagger-ui.html/**").hasRole("ADMIN")
//                .antMatchers(HttpMethod.GET, "/api/v1/medicos/**").hasRole("ADMIN")
//                .antMatchers("/api/v1/tecnicos/**").hasRole("ADMIN")
//                .antMatchers("/api/v1/jogadores/**").hasRole("ADMIN")
//                .antMatchers("/api/v1/campeonatos/**").hasRole("ADMIN")
//                .antMatchers("/api/v1/estadios/**").hasRole("ADMIN")
//                .antMatchers("/api/v1/times-adversarios/**").hasRole("ADMIN")
//                .antMatchers("/api/v1/posicoes/**").hasRole("ADMIN")
//                .antMatchers("/api/v1/partidas/**").hasRole("ADMIN")
//                .antMatchers("/api/v1/temporadas/**").hasRole("ADMIN")
//                .antMatchers("/api/v1/lesoes/**").hasAnyRole("ADMIN", "MEDICO")
//                .antMatchers("/api/v1/locais/**").hasAnyRole("ADMIN", "MEDICO")
//                .antMatchers("/api/v1/lesoes-jogador/**").hasAnyRole("ADMIN", "MEDICO")
//                .antMatchers("/api/v1/scouts-jogador/**").hasAnyRole("ADMIN", "TECNICO")
//                .antMatchers("/api/v1/scouts-partida/**").hasAnyRole("ADMIN", "TECNICO")
//                .antMatchers("/api/v1/relacionados/**").hasAnyRole("ADMIN", "TECNICO")
//                .anyRequest()
//                .authenticated()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
