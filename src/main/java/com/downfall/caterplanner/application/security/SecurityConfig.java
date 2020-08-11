package com.downfall.caterplanner.application.security;


import com.auth0.jwt.algorithms.Algorithm;
import com.downfall.caterplanner.application.security.filter.JwtAuthenticationFilter;
import com.downfall.caterplanner.application.security.jwt.JwtFactory;
import com.downfall.caterplanner.application.security.jwt.JwtVerifier;
import com.downfall.caterplanner.application.security.provider.JwtAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtAuthenticationProvider jwtAuthenticationProvider;

    private final Algorithm algorithm = Algorithm.HMAC256("12345678901234567890123456789012");

    @Bean
    public JwtFactory jwtFactory(){
        return new JwtFactory(algorithm);
    }

    @Bean
    public JwtVerifier jwtVerifier(){
        return new JwtVerifier(algorithm);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(jwtAuthenticationProvider);
    }

    protected JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception{

        OrRequestMatcher notAllowMatcher = new OrRequestMatcher(
                Arrays.asList("/auth/social/**", "/auth/refreshToken").stream().map(p -> new AntPathRequestMatcher(p)).collect(Collectors.toList()));

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(new RequestMatcher() {
            @Override
            public boolean matches(HttpServletRequest request) {
                return !notAllowMatcher.matches(request);
            }
        });

        filter.setAuthenticationManager(super.authenticationManagerBean());
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
