package org.springframework.samples.petclinic.sample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleConfig {

	// 빈을 정의할 수 있음. 해당 메서드에서 리턴하는 객체 자체가 빈으로 등록이 됨. IOC 컨테이너 안에. 그러니까 우리는 SampleController 클래스에서 @Controller 어노테이션을 붙일 필요가 없어진다.
	@Bean
	public SampleController sampleController() {
		return new SampleController();
	}
}
