package starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(ADMIN)
                .password(ADMIN)
                .roles(ADMIN)
                .and()
                .withUser(USER)
                .password(USER)
                .roles(USER);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/meetups/{\\d+}").hasRole(USER)
                .antMatchers("/meetups/{\\d+}/packBirras").hasRole(ADMIN)
                .antMatchers("/meetups/{\\d+}/temperature").hasAnyRole(ADMIN, USER)
                .antMatchers("/meetups/**").hasRole(ADMIN)
                .antMatchers("/**").permitAll()
                .and().formLogin()
                .and().csrf().disable();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
