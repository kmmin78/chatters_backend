package com.kms.chatters.config;

import com.kms.chatters.auth.service.UserDetailsServiceImpl;
import com.kms.chatters.common.entrypoint.AuthEntryPointJwt;
import com.kms.chatters.common.filter.AuthTokenFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
      return new AuthTokenFilter();
    }

    @Override
    public void configure(WebSecurity ws) throws Exception {
        // static file ingnore
        ws.ignoring().antMatchers("/template/static/**", "/css/**", "/js/**", "/img/**", "/lib/**");
    }

    @Override
    public void configure(
      AuthenticationManagerBuilder authenticationManagerBuilder
    ) throws Exception {
      authenticationManagerBuilder
      .userDetailsService(userDetailsService)
      .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
        .cors()
        .and()
          .csrf().disable()
          .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
        .and()
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
          .authorizeRequests()
          .antMatchers(HttpMethod.POST, "/login").permitAll()
          .antMatchers(HttpMethod.OPTIONS, "/login").permitAll()
          .antMatchers(HttpMethod.GET, "/test").permitAll()
          .antMatchers("/api/**").hasRole("ADMIN")
          .anyRequest().authenticated()
        ;
      
      http
          .addFilterBefore(
            authenticationJwtTokenFilter()
          , UsernamePasswordAuthenticationFilter.class
          );
    }




    // @Override
    // protected void configure(HttpSecurity http) throws Exception {

    //     http.authorizeRequests()
    //         .antMatchers("/login").permitAll()
    //         .antMatchers("/test").permitAll()
    //         .antMatchers("/chat/**").hasRole("USER")
    //         .anyRequest().authenticated()
    //         .and()
    //             .formLogin()
    //             .loginPage("/login")
    //             .loginProcessingUrl("/loginProcess")
    //             .failureUrl("/login?state=failed")
    //             .defaultSuccessUrl("/")
    //             .permitAll()
    //         .and()
    //             .logout()
    //             .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
    //             .logoutSuccessUrl("/login?state=logout")
    //             .invalidateHttpSession(true)
    //         ;
    // }

}