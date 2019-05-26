package com.leave.project.UTILITIES;

public enum Status {
	APPLIED("APPLIED"),REJECTED("REJECTED"),APPROVED("APPROVED"),UPDATED("UPDATED"),DELETED("DELETED"),CANCELLED("CANCELLED");
	String status;
	Status(String status) {
		this.status = status;
	}
	public String get() {
		return this.status;
	}
}
