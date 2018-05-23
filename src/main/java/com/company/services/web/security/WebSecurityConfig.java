package com.company.services.web.security;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().requireCsrfProtectionMatcher(new RequestMatcher() {
			private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS|POST|PUT)$");
			private RegexRequestMatcher apiMatcher = new RegexRequestMatcher("/api/.*", null);

			@Override
			public boolean matches(HttpServletRequest request) {
				// No CSRF due to allowedMethod
				if(allowedMethods.matcher(request.getMethod()).matches())
					return false;

				// No CSRF due to api call
				if(apiMatcher.matches(request))
					return false;

				return true;
			}
		});

	}

}