package com.leave.project.SERVICES;

import java.sql.Date;
import java.util.List;

import com.leave.project.MODELS.Employee;
import com.leave.project.MODELS.LeaveHistoryDetails;

public interface ILeaveService {

	String validateDate(LeaveHistoryDetails leave, Employee employee);

	String checkLeaveCount(LeaveHistoryDetails Leave);
		
	int noOfDays(Date startDate, Date endDate);
	
	List<LeaveHistoryDetails> filterByDate(List<LeaveHistoryDetails> leaveList,String start_date,String end_date);

	List<LeaveHistoryDetails> filterByLeaveType(List<LeaveHistoryDetails> leaveList,String leave_type);
	
	List<LeaveHistoryDetails> filterByStatus(List<LeaveHistoryDetails> leaveList,String status);

	List<LeaveHistoryDetails> filterByLeaveTypeStatus(List<LeaveHistoryDetails> leaveList,String leave_type,String status);


}