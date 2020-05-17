package com.automate.testrunner.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * Aspect for logging execution of service and repository Spring components
 *
 * @author Arun Kumar S A
 *
 */
@Aspect
@Component
public class LoggingAspect {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Pointcut that matches all Spring beans in the application's main packages.
	 * Method is empty as this is just a Pointcut, the implementations are in the
	 * advices.
	 */
	@Pointcut("within(com.automate.testrunner.service..*)")
	public void applicationPackagePointcut() {
		// TO-DO
	}

	/**
	 * Advice that logs when a method is entered and exited
	 *
	 * @param joinPoint join point for advice
	 * @return result
	 * @throws Throwable throws IllegalArgumentException
	 */
	@Around("applicationPackagePointcut()")
	public Object logAround(final ProceedingJoinPoint joinPoint) throws Throwable {
		final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		final String className = methodSignature.getDeclaringType().getSimpleName();
		final String methodName = methodSignature.getName();
		final StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		if (log.isDebugEnabled()) {
			log.debug("Enter: {}.{}() with argument[s] = {}", className, methodName,
					Arrays.toString(joinPoint.getArgs()));
		}
		try {
			final Object result = joinPoint.proceed();
			stopWatch.stop();
			final String loggingInfo = new StringBuilder("Execution time of ").append(className).append(".")
					.append(methodName).append(" :: ").append(stopWatch.getTotalTimeMillis()).append("ms").toString();
			log.info(loggingInfo);
			if (log.isDebugEnabled()) {
				log.debug("Exit: {}.{}() with result = {}", className, methodName, result);
			}
			return result;
		} catch (final IllegalArgumentException e) {
			log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()), className, methodName);
			throw e;
		}
	}

}