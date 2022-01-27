package org.springframework.samples.petclinic.owner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//이 어노테이션을 어디에 쓸 수 있는지, 타겟을 설정해서 메소드에 쓰겠다. 라는 의미
@Target(ElementType.METHOD)
//리텐션이라고 해서 이 어노테이션 정보를 언제까지 유지하겠다. 라는 의미
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {

}
