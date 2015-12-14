package net.duck8823.aspect;

import lombok.extern.log4j.Log4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * Created by maeda on 2015/12/13.
 */
@Log4j
@Aspect
@Component
public class LoggingAspect {

	/**
	 * メソッドの処理時間を出力する
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* net.duck8823..*(..))")
	public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Object result = joinPoint.proceed();
		stopWatch.stop();
		log.debug(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "() : " + stopWatch.getTotalTimeMillis() + "ms");

		return result;
	}
}
