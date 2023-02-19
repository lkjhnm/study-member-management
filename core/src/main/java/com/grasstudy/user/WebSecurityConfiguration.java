package com.grasstudy.user;

import com.grasstudy.common.security.reactive.JwtAuthenticationConverter;
import com.grasstudy.common.security.reactive.JwtAuthenticationManager;
import com.grasstudy.common.session.PkiBasedJwtValidator;
import com.grasstudy.common.session.PkiBasedValidator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.util.matcher.AndServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

	private static final Map<String, HttpMethod> EXCEPT_URIS = Map.of(
			"/user/check/{userId}", HttpMethod.GET,
			"/user", HttpMethod.POST,
			"/session/sign-in", HttpMethod.POST,
			"/session/refresh", HttpMethod.POST
	);

	@Bean
	SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http,
	                                           WebFilter jwtAuthenticationFilter) {
		return http
				.httpBasic().disable()
				.formLogin().disable()
				.csrf().disable()
				.addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.build();
	}

	@Bean
	WebFilter jwtAuthenticationFilter(ReactiveAuthenticationManager authenticationManager) {
		AuthenticationWebFilter webFilter = new AuthenticationWebFilter(authenticationManager);
		webFilter.setRequiresAuthenticationMatcher(negatedMatcher(EXCEPT_URIS));
		webFilter.setServerAuthenticationConverter(new JwtAuthenticationConverter());
		webFilter.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(authenticationEntryPoint()));
		return webFilter;
	}

	private static ServerWebExchangeMatcher negatedMatcher(Map<String, HttpMethod> patterns) {
		return new AndServerWebExchangeMatcher(
				patterns.entrySet().stream()
				        .map(v -> new PathPatternParserServerWebExchangeMatcher(v.getKey(), v.getValue()))
				        .map(NegatedServerWebExchangeMatcher::new)
				        .collect(Collectors.toList()));
	}

	@Bean
	ReactiveAuthenticationManager authenticationManager(PkiBasedValidator<Claims> jwtValidator) {
		return new JwtAuthenticationManager(jwtValidator);
	}

	@Bean
	PkiBasedValidator<Claims> jwtValidator() {
		return new PkiBasedJwtValidator();
	}

	private ServerAuthenticationEntryPoint authenticationEntryPoint() {
		return (exchange, ex) -> Mono.fromRunnable(() -> {
			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
		});
	}
}
