package com.leave.project.SERVICES;

import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.leave.project.MODELS.Employee;
import com.leave.project.REPOSITORIES.EmployeeRepo;

@Service
public class EmployeeService implements IEmployeeService {
	@Autowired
	private EmployeeRepo empRepo;

	@Transactional
	public Employee authenticate(String userName,String password) {
		List<Employee> list=(List<Employee>)empRepo.findByUserNameAndPassword(userName, password);
		if(list.isEmpty())
			return null;
		return list.get(0);
	}
	public Employee GetUser() {
		org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName(); //get logged in username
		return empRepo.findByUserName(name);
		
	}
	
	//For leave cancel noOfDays will positive value
		//For leave approve noOfDays will negative value
		@Override
		public Employee updateLeaveCount(Employee e, String leaveType, int noOfDays) {
			if (leaveType.equals("Medical Leave")) {
				e.setMedicalLeaveCount(e.getMedicalLeaveCount() + noOfDays);
			} else if (leaveType.equals("Compensation Leave")) {
				e.setCompensationLeaveCount(e.getCompensationLeaveCount() + noOfDays);
			} else if (leaveType.equals("Annual Leave")) {
				e.setAnnualLeaveCount(e.getAnnualLeaveCount() + noOfDays);
			}

			return e;
		}
}
