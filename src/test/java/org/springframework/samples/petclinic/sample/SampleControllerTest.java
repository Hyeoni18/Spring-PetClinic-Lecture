package org.springframework.samples.petclinic.sample;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SampleControllerTest {

	//오토와이어로 IOC 컨테이너에서 빈을 꺼내서 쓸 수 있다.
	@Autowired
	ApplicationContext applicationContext;

	@Test
	public void testDI() {
		SampleController bean = applicationContext.getBean(SampleController.class);
		AssertionsForClassTypes.assertThat(bean).isNotNull();
		//해당 빈이 있는지 없는지 확인하는 소스.
	}
}
