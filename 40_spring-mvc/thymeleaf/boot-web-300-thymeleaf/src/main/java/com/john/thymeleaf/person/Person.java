package com.john.thymeleaf.person;

public class Person {
	
	
	private Integer id;
	
	private String name;
	
	private String nickName;
	
	
	

	
	public Person() {
		super();
	}
	
	public Person(Integer id, String name, String nickName) {
		super();
		this.id = id;
		this.name = name;
		this.nickName = nickName;
	}
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	
}
