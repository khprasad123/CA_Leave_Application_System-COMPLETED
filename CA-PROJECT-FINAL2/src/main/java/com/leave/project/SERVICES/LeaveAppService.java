package com.leave.project.SERVICES;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.project.MODELS.Employee;
import com.leave.project.MODELS.LeaveHistoryDetails;
import com.leave.project.MODELS.PublicHollyday;
import com.leave.project.REPOSITORIES.LeaveHistoryRepo;
import com.leave.project.REPOSITORIES.PublicHollydayRepo;
import com.leave.project.UTILITIES.Status;

@Service
public class LeaveAppService implements ILeaveService {

	public static int WEEKEND_OR_HOLIDAY_ERROR = 2;
	@Autowired
	private IEmployeeService empService;
	@Autowired
	private PublicHollydayRepo holidayRepo;

	@Autowired
	private LeaveHistoryRepo leaveHistoryRepo;

	@Override
	public String validateDate(LeaveHistoryDetails leave, Employee employee) {
		Date startDate = leave.getStartDate();
		Date endDate = leave.getEndDate();
		LocalDate start = startDate.toLocalDate();
		LocalDate end = endDate.toLocalDate();
		if (isHoliday(startDate, endDate)) {
			return "Start and End-date can not be Holiday.";
		}
		if (!isWeekdays(start, end)) {
			return "Start and End-date must be working days.";

		}
		if (!isTwoDateValid(start, end)) {
			return "End date cannot be less than Start Date.";
		}
		if (isAlreadyApply(employee,leave)) {
			return "You can't apply leave for already applied leave dates.";
		}

		String error = checkLeaveCount(leave);
		if (!error.equals("")) {
			return error;
		}

		return "";

	}

	@Transactional
	private int noOfHoliday(Date start, Date end) {
		return holidayRepo.findByStartDateBetween(start, end).size();
	}

	@Transactional
	private boolean isHoliday(Date start, Date end) {
		return holidayRepo.findByStartDateOrStartDate(start, end).size() != 0;
	}

	private boolean isTwoDateValid(LocalDate start, LocalDate end) {
		if (end.isAfter(start) || end.isEqual(start))
			return true;
		else
			return false;

	}

	private boolean isWeekdays(LocalDate start, LocalDate end) {
		if (start.getDayOfWeek().getValue() < 6 && end.getDayOfWeek().getValue() < 6) {
			return true;
		} else
			return false;
	}

	@Transactional
	private boolean isAlreadyApply(Employee employee,LeaveHistoryDetails l) {
		Date start = l.getStartDate();
		Date end = l.getEndDate();
		
		List<LeaveHistoryDetails> list = leaveHistoryRepo.findByEmployee(employee);
		
		if(l.getStatus() == Status.UPDATED) {
		 System.out.println(list.removeIf(leave -> leave.getLeaveHistoryId() == l.getLeaveHistoryId()));
		}
		list = list.stream()
				.filter(leave -> (leave.getStatus() == Status.APPLIED || leave.getStatus() == Status.APPROVED
						|| leave.getStatus() == Status.UPDATED) && leave.isBetween(start, end))
				.collect(Collectors.toList());
		return list.size() != 0;
	}

	// if Monday ,getDayOfWeek().getValue() will return 1
	// and increase by one for next days
	// so Sunday then getDayOfWeek().getValue() is 7
	@Override
	public int noOfDays(Date startDate, Date endDate) {
		LocalDate start = startDate.toLocalDate();
		LocalDate end = endDate.toLocalDate();
		int totalDays = (int) ChronoUnit.DAYS.between(start, end);
		totalDays++; // between returned result need to add one day to get actual total days
		int weekends = 0;
		while (start.isBefore(end)) {
			if (start.getDayOfWeek().getValue() > 5) {
				weekends++;
			}
			start = start.plusDays(1);
		}

		if (totalDays <= 14) {
			totalDays = totalDays - (weekends) - noOfHoliday(startDate, endDate);
		}

		return totalDays;

	}

	@Override
	public String checkLeaveCount(LeaveHistoryDetails leave) {
		int days = noOfDays(leave.getStartDate(), leave.getEndDate());
		if (leave.getLeaveName().equals("Medical Leave") && leave.getMedicalCount() < days) {
			return makeError(leave.getLeaveName(), leave.getMedicalCount());
		} else if (leave.getLeaveName().equals("Compensation Leave") && leave.getCompensationCount() < days) {
			return makeError(leave.getLeaveName(), leave.getCompensationCount());
		} else if (leave.getLeaveName().equals("Annual Leave") && leave.getAnnualCount() < days) {
			return makeError(leave.getLeaveName(), leave.getAnnualCount());
		}

		return "";
	}

	private String makeError(String lname, int count) {
		return "Over limit. Your remaining " + lname + " count is " + count;
	}

	@Override
	public List<LeaveHistoryDetails> filterByDate(List<LeaveHistoryDetails> leaveList, String start_date,
			String end_date) {
		return leaveList.stream()
				.filter(leave -> leave.checkForHistory(Date.valueOf(start_date), Date.valueOf(end_date)))
				.collect(Collectors.toList());
	}

	@Override
	public List<LeaveHistoryDetails> filterByLeaveType(List<LeaveHistoryDetails> leaveList, String leave_type) {
		return leaveList.stream().filter(leave -> leave.getLeaveName().equals(leave_type)).collect(Collectors.toList());
	}

	@Override
	public List<LeaveHistoryDetails> filterByStatus(List<LeaveHistoryDetails> leaveList, String status) {
		return leaveList.stream().filter(leave -> leave.getStatus().get().equals(status)).collect(Collectors.toList());
	}

	@Override
	public List<LeaveHistoryDetails> filterByLeaveTypeStatus(List<LeaveHistoryDetails> leaveList, String leave_type,
			String status) {
		return leaveList.stream()
				.filter(leave -> (leave.getLeaveName().equals(leave_type) && leave.getStatus().get().equals(status)))
				.collect(Collectors.toList());
	}
}
