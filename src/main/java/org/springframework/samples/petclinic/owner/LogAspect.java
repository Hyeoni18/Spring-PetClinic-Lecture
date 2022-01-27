package org.springframework.samples.petclinic.owner;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

//빈으로 등록되어야 하기 때문에 컴포넌트 어노테이션을 추가해줌
@Component
@Aspect
public class LogAspect {

	Logger logger = LoggerFactory.getLogger(LogAspect.class);

	//어라운트 어노테이션을 붙인 메소드를 정의했는데, joinPoint 라는 파라미터를 받을 수 있어, 이게 뭐냐면 어라운드에 적인 어노테이션 이름이 적힌 어노테이션이 붙여진 메소드야. 타겟인거지.
	@Around("@annotation(LogExecutionTime)")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		Object proceed = joinPoint.proceed(); //파라미터로 들어온 메소드를 실행하겠다. 그리고 결과(proceed)가 있다면

		stopWatch.stop();
		logger.info(stopWatch.prettyPrint());

		return proceed;	//리턴하겠다.
	}

	// 이게 Aspect 이고, 스프링이 제공해주는 어노테이션 기반의 AOP 야. 그리고 프록시 패턴을 기반으로 동작을 하는거지.
	// Store-Cash-CashPerf-Payment 와 비교를 하면
	// OwnerController 는 Cash 클래스와 같은거야. Aspect 를 적용할 타켓인거지.
	// 그리고 여기에 만든 logExecutionTime 메소드는 CashPerf 클래스와 같은거지. 단지 빈으로 등록할 때 오너컨트롤러 대신의 인터페이스 상속받고 테스트 돌릴 때 cash 대신의 cashperf 를 넣어주는 등의 작업은 자동으로 이루어지는거지.
	// AOP 자체는 공부할 게 굉장히 많음. 그 중 Around 어노테이션 말고도 다른게 있는데 After , Before 등 () 내부에 들어가는 표현식도 어노테이션 말고도 빈 또는 메소드가 실행되는 시점을 적을 수 있는데. 그럴 경우 타켓에 어노테이션을 작성하지 않아도 됨.

}
