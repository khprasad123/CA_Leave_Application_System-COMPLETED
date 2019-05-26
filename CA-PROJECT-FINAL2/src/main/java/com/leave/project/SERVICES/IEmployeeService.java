package com.leave.project.SERVICES;

import com.leave.project.MODELS.Employee;

public interface IEmployeeService {

	Employee authenticate(String userName, String password);
	Employee GetUser();
	Employee updateLeaveCount(Employee employee, String leaveType, int noOfDays);

}