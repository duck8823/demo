package com.duck8823.aspect;

import com.duck8823.service.MailService;
import com.duck8823.util.MailBuilder;
import lombok.extern.log4j.Log4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * Created by maeda on 2015/12/13.
 */
@Log4j
@Aspect
@Component
public class LoggingAspect {

	@Autowired
	private MailService mailService;

	/**
	 * メソッドの処理時間を出力する
	 * @param joinPoint ProceedingJoinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* com.duck8823..*(..))")
	public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Object result = joinPoint.proceed();
		stopWatch.stop();
		log.debug(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "() : " + stopWatch.getTotalTimeMillis() + "ms");

		return result;
	}

	/**
	 * 例外発生時にメールを送信する
	 * @param th Throwable
	 */
	@AfterThrowing(value = "execution(* com.duck8823..*(..))", throwing = "th")
	public void sendMail(Throwable th) {
		mailService.sendMail(MailBuilder.getInstance().build(th));
	}
}
