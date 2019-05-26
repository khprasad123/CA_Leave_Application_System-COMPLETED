package com.leave.project.MODELS;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
public class Employee {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int empId;
	@NotNull
	private String fullName;
	@NotNull
	private String email;
	@NotNull
	@Column(unique=true)
	@Length(max=100)
	private String userName;
	@NotNull
	private String password;
	
	@ManyToOne
	@JoinColumn(name="Role_Id")
	private Role role;
	
	@ManyToOne
	@JoinColumn(name="Reports_To")
	private Employee reportsTo;
	
	private int annualLeaveCount;
	private int compensationLeaveCount;
	private int medicalLeaveCount;
	
	
	
	
	///getters and setters
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Employee getReportsTo() {
		return reportsTo;
	}
	public void setReportsTo(Employee reportsTo) {
		this.reportsTo = reportsTo;
	}
	public int getAnnualLeaveCount() {
		return annualLeaveCount;
	}
	public void setAnnualLeaveCount(int annualLeaveCount) {
		this.annualLeaveCount = annualLeaveCount;
	}
	public int getCompensationLeaveCount() {
		return compensationLeaveCount;
	}
	public void setCompensationLeaveCount(int compensationLeaveCount) {
		this.compensationLeaveCount = compensationLeaveCount;
	}
	public int getMedicalLeaveCount() {
		return medicalLeaveCount;
	}
	public void setMedicalLeaveCount(int medicalLeaveCount) {
		this.medicalLeaveCount = medicalLeaveCount;
	}
	public Employee(@NotNull String fullName, @NotNull String email, @NotNull String userName,
			@NotNull String password, Role role, Employee reportsTo, int annualLeaveCount,int medicalLeaveCount, int compensationLeaveCount) {
		super();
		this.fullName = fullName;
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.role = role;
		this.reportsTo = reportsTo;
		this.annualLeaveCount = annualLeaveCount;
		this.compensationLeaveCount = compensationLeaveCount;
		this.medicalLeaveCount = medicalLeaveCount;
	}
	
	public Employee(int empId, @NotNull String fullName, @NotNull String email,
			@NotNull @Length(max = 100) String userName, @NotNull String password, Role role) {
		super();
		this.empId = empId;
		this.fullName = fullName;
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.role = role;
	}
	public Employee(int empId) {
		super();
		this.empId = empId;
	}
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Employee(@NotNull String fullName) {
		super();
		this.fullName = fullName;
	}
	@Override
	public String toString() {
		return  fullName ;
	}
	
	
	
	
}


