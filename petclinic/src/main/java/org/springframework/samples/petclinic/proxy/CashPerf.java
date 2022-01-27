package org.springframework.samples.petclinic.proxy;

import org.springframework.util.StopWatch;

//캐시 클래스의 프록시 클래스인거야.
public class CashPerf implements Payment {

	//신용카드에 한도가 있거나 제한이 있을 경우 캐시로 폴백하는거야.
	Payment cash = new Cash();

	@Override
	public void pay(int amount) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		cash.pay(amount); //캐시를 쓸 경우에는 그냥 이렇게 호출만 해주면 돼

		stopWatch.stop();
		System.out.println(stopWatch.prettyPrint());
	}

	//이런 프록시가 자동으로 빈이 등록될때 만들어진다고 생각하면 돼.
	//원래는 캐시가 빈으로 등록되어야 하는데 그걸 빈으로 등록하라고 설정을 해뒀지만, 내가 만들고 싶은 프록시가 자동으로 생겨서 캐시 대신에 내가 만든 프록시가 등록이 되고, 그래서 클라이언트가 원래의 빈으로 등록되어야 하는 캐시가 아니라 캐시 퍼프를 대신 쓰게 되는 일이 스프링 내부에서 발생하게 되는거야.
	//그래서 리포지토리에서 봤던 Transactional 어노테이션이 붙어있으면 리포지토리 타입의 객체가 프록시로, 객체 타입의 프록시가 새로 만들어져.
}
