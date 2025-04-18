package com.java.lld.design.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User {
	private final String userId;
	private final String name;
	private final String email;

	public User(String name, String email) {
		this.userId = UUID.randomUUID().toString();
		this.name = name;
		this.email = email;
	}

	@Override
	public String toString() {
		return "User{" + "userId='" + userId + '\'' + ", name='" + name + '\'' + ", email='" + email + '\'' + '}';
	}
}
