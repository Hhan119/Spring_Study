package com.example.helloboot.auto;

import org.springframework.stereotype.Component;

@Component
public class MyCar implements Car{

	@Override
	public void driver() {
		System.out.println("차가 열심히 굴러가요!!");
	}

}
