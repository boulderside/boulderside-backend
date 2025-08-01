package com.line7studio.boulderside.common.aop;

import java.util.Collection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.line7studio.boulderside.common.security.details.CustomUserDetails;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class AuthInfoLoggingAspect {
	@Around("execution(* com.line7studio.boulderside..*Controller.*(..))")
	public Object logAuthenticationInfo(ProceedingJoinPoint joinPoint) throws Throwable {
		String methodSignature = joinPoint.getSignature().toShortString();

		Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext()
			.getAuthentication();

		if (auth != null && auth.isAuthenticated()) {
			Object principal = auth.getPrincipal();
			Long userId = null;

			if (principal instanceof CustomUserDetails userDetails) {
				userId = userDetails.getUserId();
			}

			Collection<? extends GrantedAuthority> roles = auth.getAuthorities();

			String ip = null;
			Object details = auth.getDetails();
			if (details instanceof WebAuthenticationDetails webDetails) {
				ip = webDetails.getRemoteAddress();
			}

			log.info("method: {}, userId: {}, roles: {}, ip: {}", methodSignature, userId, roles, ip);
		}

		return joinPoint.proceed();
	}

}
