package com.duck8823;

import org.springframework.beans.factory.BeanCreationNotAllowedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by maeda on 2015/12/12.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${admin.id}")
	private String id;

	@Value("${admin.password}")
	private String password;

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		// インメモリ認証（idとpasswordはapplication.propertiesに記述）
		auth.inMemoryAuthentication().withUser(id).password(password).roles("ADMIN");
	}

	@Override
	protected void configure(HttpSecurity web) throws Exception {
		web.authorizeRequests()
				.antMatchers("/", "/login/", "/h2-console/**","/fonts/**", "/twitter/**", "/facebook/**", "/line/**", "/slack/**", "/photo/**", "/css/**", "/js/**", "/img/**")
				.permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.csrf().requireCsrfProtectionMatcher(new AllExceptUrlsStartedWith("/line/callback")).and()
				.csrf().disable()
				.formLogin()
				.loginPage("/login/")
				.loginProcessingUrl("/login/")
				.defaultSuccessUrl("/manage/photo/upload/")
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

	private static class AllExceptUrlsStartedWith implements RequestMatcher {

		private static final String[] ALLOWED_METHODS =
				new String[] {"GET", "HEAD", "TRACE", "OPTIONS"};

		private final String[] allowedUrls;

		public AllExceptUrlsStartedWith(String... allowedUrls) {
			this.allowedUrls = allowedUrls;
		}

		@Override
		public boolean matches(HttpServletRequest request) {
			String method = request.getMethod();
			for (String allowedMethod : ALLOWED_METHODS) {
				if (allowedMethod.equals(method)) {
					return false;
				}
			}

			String uri = request.getRequestURI();
			for (String allowedUrl : allowedUrls) {
				if (uri.startsWith(allowedUrl)) {
					return false;
				}
			}

			return true;
		}

	}
}