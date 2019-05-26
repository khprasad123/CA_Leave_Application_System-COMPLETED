package com.leave.project.FORMS;

public class LoginForm {
	private String Username;
	private String Password;
	public String error;
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public LoginForm() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LoginForm(String username, String password, String error) {
		super();
		Username = username;
		Password = password;
		this.error = error;
	}
	
}
