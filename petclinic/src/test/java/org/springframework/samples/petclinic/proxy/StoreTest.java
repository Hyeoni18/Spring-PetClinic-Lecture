package org.springframework.samples.petclinic.proxy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {

	@Test
	public void testPay() {
		Payment cashPerf = new CashPerf(); //원래는 그냥 Cash 클래스를 쓰고 있었을거야. 그러면 성능 측정은 되지않아. 그런데 캐시퍼프라는 프록시를 만들었고
		Store store = new Store(cashPerf);	//클라이언트 코드가 페이먼트라는 인터페이스의 캐시퍼프를 쓰도록 소스를 바꿨기 때문에 성능측정이 되는거야. 기존 소스를 건들지 않고. 새로운 소스를 추가했더라도. 이게 AOP를 프록시 패턴으로 구현하는 방법이야.
		store.buySomothing(100);
	}

}
