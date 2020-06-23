package com.rygf.config;

import com.rygf.security.CustomAccessDeniedHandler;
import com.rygf.security.CustomUserDetailsService;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

@AllArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    private CustomUserDetailsService customUserDetailsService;
    private DataSource dataSource;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
    
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    
    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
    
    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setCreateTableOnStartup(false);
        repo.setDataSource(dataSource);
        return repo;
    }
    
//    @Bean
//    public PersistentTokenBasedRememberMeServices rememberMeAuthenticationProvider() {
//        PersistentTokenBasedRememberMeServices services =
//            new PersistentTokenBasedRememberMeServices("rgyfKey", customUserDetailsService, jdbcTokenRepository());
//        return services;
//    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        
        http.formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .and()
            .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler());
        
        http
            .logout()
            .logoutSuccessUrl("/");
        
        http.rememberMe()
            .rememberMeCookieName("rygfKey")
            .tokenValiditySeconds(86400 * 3)
            .userDetailsService(customUserDetailsService)
            .tokenRepository(jdbcTokenRepository()); //86400 = 1 day
        
        http.authorizeRequests()
            .antMatchers("/register").permitAll()
            .antMatchers("/login").permitAll()
            .antMatchers("/dashboard/**").hasAnyRole("ADMIN", "MANAGER")
            .anyRequest().permitAll();
    }
}
