package net.duck8823;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * Created by maeda on 2015/12/12.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${user.id}")
	private String id;

	@Value("${user.password}")
	private String password;

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		// インメモリ認証（idとpasswordはapplication.propertiesに記述）
		auth.inMemoryAuthentication().withUser(id).password(password).roles("ADMIN");
	}

	@Override
	protected void configure(HttpSecurity web) throws Exception {
		web.authorizeRequests()
				.antMatchers("/", "/twitter/**", "/photo/**", "/css/**", "/js/**", "/img/**")
				.permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.formLogin()
				.loginPage("/login/")
				.loginProcessingUrl("/login/")
				.defaultSuccessUrl("/upload/")
				.failureUrl("/login/")
				.usernameParameter("id")
				.passwordParameter("password")
				.permitAll()
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout/"))
				.logoutSuccessUrl("/")
				.deleteCookies("JSESSIONID")
				.invalidateHttpSession(true)
				.permitAll();
	}
}
