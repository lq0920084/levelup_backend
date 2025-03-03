package com.sparta.levelup_backend.config;

import com.sparta.levelup_backend.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final AuthenticationConfiguration authenticationConfiguration;
	private final JwtUtils jwtUtils;
	private final FilterResponse filterResponse;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
		throws Exception {

		return configuration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {

		return new CustomAccessDeniedHandler(filterResponse);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.csrf((csrf) -> csrf.disable())
			.formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable());


		http.
			authorizeHttpRequests((auth) -> auth
				.requestMatchers("/", "/v2/home", "/v2/sign**", "/v2/oauth2sign**", "/v**/users/resetPassword**",
					"/resetPassword**")
				.permitAll()
				.requestMatchers("/v2/admin/**", "/v3/admin/**")
				.hasRole("ADMIN")
				.anyRequest()
				.authenticated());

		http.exceptionHandling(exceptionHandling ->
			exceptionHandling.accessDeniedHandler(accessDeniedHandler()));

		http.
			addFilterBefore(new JwtFilter(jwtUtils, filterResponse), UsernamePasswordAuthenticationFilter.class);

		http.sessionManagement((session) -> session
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();

	}
}
