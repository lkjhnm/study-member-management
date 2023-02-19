package com.grasstudy.user.controller;

import com.grasstudy.common.security.JwtAuthentication;
import com.grasstudy.user.WithMockUser;
import io.jsonwebtoken.impl.DefaultClaims;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;

class ControllerTestExecutionListener implements TestExecutionListener {

	@Override
	public void beforeTestExecution(TestContext testContext) throws Exception {
		WithMockUser annotation = AnnotationUtils.findAnnotation(testContext.getTestMethod(), WithMockUser.class);
		Optional.ofNullable(annotation).ifPresentOrElse(annot -> {
			JwtAuthentication authentication = new JwtAuthentication("mock-token");
			authentication.setClaims(new DefaultClaims(Map.of("userId", annot.userId())));
			authentication.setAuthenticated(true);
			applyMockAuthentication(testContext, ongoingStubbing ->
					ongoingStubbing.thenReturn(Mono.just(authentication)));
		}, () -> applyMockAuthentication(testContext, ongoingStubbing ->
				ongoingStubbing.thenReturn(Mono.error(new AuthenticationServiceException("UnAuthorize")))));
	}

	private void applyMockAuthentication(TestContext testContext, Consumer<OngoingStubbing<Mono<?>>> mockAction) {
		ReactiveAuthenticationManager mockAuthenticationManager =
				testContext.getApplicationContext().getBean(ReactiveAuthenticationManager.class);
		mockAction.accept(Mockito.when(mockAuthenticationManager.authenticate(any())));
	}
}
