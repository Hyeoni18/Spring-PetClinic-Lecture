package org.springframework.samples.petclinic.proxy;

public class Store {

	Payment payment;

	//생성자를 만듦으로써 스토어를 만들 때 페이먼트 객체가 반드시 필요함
	public Store(Payment payment) {
		this.payment = payment;
	}

	public void buySomothing(int amount) {
		payment.pay(amount);
	}
}
