package com.leave.project.MODELS;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.leave.project.UTILITIES.Status;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

@Entity
public class LeaveHistoryDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int leaveHistoryId;
	
	@ManyToOne
	@JoinColumn(name="Emp_Id")
	@CsvBindByPosition( position = 0)
	private Employee employee;
	
	@ManyToOne
	@JoinColumn(name="Leave_Type_Id")
	@CsvBindByPosition( position = 1)
	private LeaveType leaveType;
	
	@NotNull
	@CsvBindByPosition( position = 2)
	private Date startDate;
	
	@NotNull
	@CsvBindByPosition( position = 3)
	private Date endDate;
	
	@NotNull
	@CsvBindByPosition( position = 4)
	private String applyingReason;

	@CsvBindByPosition( position = 5)
	private String rejectionReason;
	@NotNull
	@Enumerated(EnumType.STRING)
	private Status status=Status.APPLIED;
	
	@CsvBindByPosition( position = 6)
	private String workDesemination;
	
	
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public LeaveType getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getApplyingReason() {
		return applyingReason;
	}
	public void setApplyingReason(String applyingReason) {
		this.applyingReason = applyingReason;
	}
	public String getRejectionReason() {
		return rejectionReason;
	}
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getWorkDesemination() {
		return workDesemination;
	}
	public void setWorkDesemination(String workDesemination) {
		this.workDesemination = workDesemination;
	}
	public LeaveHistoryDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LeaveHistoryDetails(Employee employee, LeaveType leaveType, @NotNull Date startDate, @NotNull Date endDate,
			@NotNull String applyingReason, String rejectionReason, @NotNull Status status, String workDesemination) {
		super();
		this.employee = employee;
		this.leaveType = leaveType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.applyingReason = applyingReason;
		this.rejectionReason = rejectionReason;
		this.status = status;
		this.workDesemination = workDesemination;
	}
	public int getLeaveHistoryId() {
		return leaveHistoryId;
	}
	public void setLeaveHistoryId(int leaveHistoryId) {
		this.leaveHistoryId = leaveHistoryId;
	}
	
	public boolean check(Date startDate2, Date endDate2) {
    	boolean isAfter = this.startDate.toLocalDate().isAfter(startDate2.toLocalDate().minusDays(1));
    	boolean isBefore = this.endDate.toLocalDate().isBefore(endDate2.toLocalDate().plusDays(1));
    	
    	if( isAfter && isBefore)
    			{
    				return true;
    			}    	
		return false;
	}
	
	@Override
	public String toString() {
		return "LeaveHistoryDetails [leaveHistoryId=" + leaveHistoryId + ", manager=" + employee + ", leaveType="
				+ leaveType + ", startDate=" + startDate + ", endDate=" + endDate + ", applyingReason=" + applyingReason
				+ ", rejectionReason=" + rejectionReason + ", status=" + status + ", workDesemination="
				+ workDesemination + "]";
	}
	
	
	///getters and setters
	
	public String getLeaveName() {
		return this.leaveType.getType();
	}
	public int getCompensationCount() {
		return this.employee.getCompensationLeaveCount();
	}
	public int getMedicalCount() {
		return this.employee.getMedicalLeaveCount();
	}
	public int getAnnualCount() {
		return this.employee.getAnnualLeaveCount();
	}
	public boolean checkForHistory(Date startDate2, Date endDate2) {
		boolean isAfter = this.startDate.toLocalDate().isAfter(startDate2.toLocalDate().minusDays(1));
		boolean isBefore = this.endDate.toLocalDate().isBefore(endDate2.toLocalDate().plusDays(2));

		LocalDate d = this.startDate.toLocalDate();

		if (isAfter && isBefore)
			return true;

		return false;
	}

	public boolean isBetween(Date startDate2, Date endDate2) {

		if (isWithinRange(startDate2) || isWithinRange(endDate2))
			return true;

		return false;
	}

	boolean isWithinRange(Date date) {
		return !(date.before(startDate) || date.after(endDate));
	}

	public int getLeaveCount() {
		long diff = this.endDate.getTime() - this.startDate.getTime();
		return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}
	
	
}
