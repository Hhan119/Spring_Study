package com.example.boot13.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entity 어노테이션을 붙여주면 아래 필드명대로 DB에서 Column을 생성해준다.
// (name="변경할 DB 테이블 이름") 
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity(name="PHONE_INFO")
public class Phone {
	// Id라는 칼럼은 primary Key 값으로 설정되도록 한다. 
	@Id
	// id 값을 순차적으로 증가시킨다.(strategy = GenerationType.AUTO) 옵션은 시퀀스라고 생각하면 된다.
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String company;
	private String name;
	// null 값이 가능한 Entity 필드는 반드시 참조 data type 이어야 한다.
	// 데이터를 null 값으로 두려면 Integer type을 사용해야 하고, Column 어노테이션을 붙어야 한다.  
	@Column(nullable = true)
	private Integer price;	
	// 등록일을 저장하고 싶다면
	@Column(nullable = false)
	private Date regdate;
	
	// Entity를 영속화 하기 직전에 뭔가 작업할게 있으면, @PrePersist 어노테이션을 활용 하면된다.
	@PrePersist
	public void onPersist() {
		// 오라클에서 데이터를 넣을때 SYSDATE 함수를 이용해서 넣는 효과를 낸다.
		regdate = new Date();
	}
}
