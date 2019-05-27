package com.leave.project.LEAVE.CONTROLLERS;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.leave.project.MODELS.Employee;
import com.leave.project.MODELS.LeaveHistoryDetails;
import com.leave.project.MODELS.LeaveType;
import com.leave.project.REPOSITORIES.EmployeeRepo;
import com.leave.project.REPOSITORIES.LeaveHistoryRepo;
import com.leave.project.REPOSITORIES.PublicHollydayRepo;
import com.leave.project.SERVICES.IEmployeeService;
import com.leave.project.SERVICES.ILeaveService;
import com.leave.project.UTILITIES.Status;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Controller
public class LeaveAppController {

	@Autowired
	private IEmployeeService emp;

	

	@Autowired
	private LeaveHistoryRepo leaveHistoryDetailsRepo;

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private PublicHollydayRepo publicrepo;

	@Autowired
	private ILeaveService leaveService;

	private List<LeaveHistoryDetails> leaveList;
	private List<LeaveType> leaveTypeList = Arrays.asList(new LeaveType(1,"Medical Leave"),new LeaveType(2,"Compensation Leave"),new LeaveType(3,"Annual Leave"));
	private List<LeaveHistoryDetails> toExportList = new ArrayList<LeaveHistoryDetails>();
	private String intro = "I'm sorry to inform that I can't accept your request. Because ";


	@RequestMapping(path = "/staff/staffView", method = RequestMethod.GET)
	public String staffView() {
		Employee t = emp.GetUser();
		if (t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		return "STAFF";
	}

	@RequestMapping(path = "/staff/leaveAppForm", method = RequestMethod.GET)
	public String leaveAppForm(Model model) {
		Employee t = emp.GetUser();
		if (t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		LeaveHistoryDetails temp=new LeaveHistoryDetails();
		temp.setEmployee(t);
		model.addAttribute("leaveDetails", temp);
		model.addAttribute("leaveTypeList", leaveTypeList);
		return "LeaveAppForm";
	}

	@RequestMapping(path = "/staff/leaveAppForm", method = RequestMethod.POST)
	public String leaveAppSubmit(@Valid LeaveHistoryDetails leaveDetails, BindingResult result,
			Model model) {

		Employee t = emp.GetUser();
		leaveDetails.setEmployee(t);
		if (t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		System.out.println("**************");
		System.out.println("StartDate "+leaveDetails.getStartDate().toString());

		String error_msg = leaveService.validateDate(leaveDetails,t);
		if (!error_msg.equals("")) {
			model.addAttribute("leaveDetails",leaveDetails);
			model.addAttribute("leaveTypeList", leaveTypeList);
			model.addAttribute("message", error_msg);
			model.addAttribute("error", "error");

			return "LeaveAppForm";
		}
		System.out.println("*******Filter*******");
		System.out.println("StartDate "+leaveDetails.getStartDate().toString());
		leaveDetails.setEmployee(t);
		leaveHistoryDetailsRepo.save(leaveDetails);
		List<LeaveHistoryDetails> historyList = new ArrayList<LeaveHistoryDetails>();
		historyList = leaveHistoryDetailsRepo.findByEmployeeAndStatusOrStatus(t,Status.APPLIED, Status.UPDATED);

		model.addAttribute("leaveTypeList", leaveTypeList);
		model.addAttribute("leaveDetails", leaveDetails);
		model.addAttribute("leaveHistoryList", historyList);
		return "ManageLeaveApp";
	}

	@RequestMapping(path = "/staff/manageLeaveDetails", method = RequestMethod.GET)
	public String manageLeaveApp(LeaveHistoryDetails leaveDetails, Model model) {
		Employee t = emp.GetUser();
		if (t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";

		List<LeaveHistoryDetails> historyList = leaveHistoryDetailsRepo.findByEmployeeAndStatusOrStatus(t,Status.APPLIED,Status.UPDATED);
		model.addAttribute("leaveHistoryList", historyList);
		return "ManageLeaveApp";
	}

	@RequestMapping(path = "/staff/updateLeaveForm/{leaveHistoryId}", method = RequestMethod.GET)
	public String updateLeaveForm(@PathVariable(value = "leaveHistoryId") int leaveHistoryId, Model model) {
		Employee t = emp.GetUser();
		if (t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";

		LeaveHistoryDetails leavehistory = leaveHistoryDetailsRepo.findById(leaveHistoryId).orElse(null);
		leavehistory.setStatus(Status.UPDATED);
		model.addAttribute("leaveDetails", leavehistory);
		model.addAttribute("leaveTypeList", leaveTypeList);
		return "LeaveAppForm";
	}

	@RequestMapping(path = "/staff/deleteLeaveForm/{leaveHistoryId}", method = RequestMethod.GET)
	public String deleteLeaveForm(@PathVariable(value = "leaveHistoryId") int leaveHistoryId, Model model) {
		Employee t = emp.GetUser();
		if (t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";

		LeaveHistoryDetails leavehistory = leaveHistoryDetailsRepo.findById(leaveHistoryId).orElse(null);
		leavehistory.setStatus(Status.DELETED);
		leaveHistoryDetailsRepo.save(leavehistory);
		return "redirect:/staff/manageLeaveDetails";
	}

	@RequestMapping(path = "/staff/viewApprovedLeaves", method = RequestMethod.GET)
	public String viewApprovedLeaves(LeaveHistoryDetails leaveDetails, Model model) {
		Employee t = emp.GetUser();
		if (t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";

		List<LeaveHistoryDetails> leaveHistoryList = leaveHistoryDetailsRepo.findByStatus(Status.APPROVED);
		leaveHistoryList = (List<LeaveHistoryDetails>)leaveHistoryList.stream().filter(emp -> emp.getEmployee().getEmpId()==t.getEmpId()).collect(Collectors.toList());
		model.addAttribute("leaveHistoryList", leaveHistoryList);
		return "ViewAppliedLeaves";
	}

	@RequestMapping(path = "/staff/cancelLeaveForm/{leaveHistoryId}", method = RequestMethod.GET)
	public String cancelLeaveForm(@PathVariable(value = "leaveHistoryId") int leaveHistoryId, Model model) {
		Employee t = emp.GetUser();
		if (t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";

		LeaveHistoryDetails leavehistory = leaveHistoryDetailsRepo.findById(leaveHistoryId).orElse(null);
		if (leavehistory.getStatus() == Status.APPROVED) {
			int noOfDays = leaveService.noOfDays(leavehistory.getStartDate(), leavehistory.getEndDate());
			Employee e = emp.updateLeaveCount(leavehistory.getEmployee(), leavehistory.getLeaveName(), noOfDays);// noOfDays value to negative3
			employeeRepo.save(e);
		}
		leavehistory.setStatus(Status.CANCELLED);
		leaveHistoryDetailsRepo.save(leavehistory);
		List<LeaveHistoryDetails> leaveHistoryList = leaveHistoryDetailsRepo.findByStatus(Status.APPROVED);
		leaveHistoryList = (List<LeaveHistoryDetails>)leaveHistoryList.stream().filter(emp -> emp.getEmployee().getEmpId()==t.getEmpId()).collect(Collectors.toList());
		model.addAttribute("leaveHistoryList", leaveHistoryList);
		return "ViewAppliedLeaves";
	}

	@RequestMapping(path = "/staff/viewPersonalLeaveHistory", method = RequestMethod.GET)
	public String viewPersonalLeaveHistory(LeaveHistoryDetails leaveDetails, Model model) {
		Employee t = emp.GetUser();
		if (t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";

		List<LeaveHistoryDetails> leaveHistoryList = leaveHistoryDetailsRepo.findByEmployee(t);
		model.addAttribute("leaveHistoryList", leaveHistoryList);
		return "ViewPersonalLeaveHistory";

	}

//****************************************** Manager's part ************************************************************
	@RequestMapping(path = "/leave/approval_list", method = RequestMethod.GET)
	public String showEmployeeList(Model model) {

		Employee t = emp.GetUser();
		if (!t.getRole().getRoleName().equals("Manager"))
			return "redirect:/logout";

		List<Employee> empList = employeeRepo.findByReportsTo(t);
		leaveList = new ArrayList<LeaveHistoryDetails>();
		List<LeaveHistoryDetails> temp = new ArrayList<LeaveHistoryDetails>();

		Iterator<Employee> i = empList.iterator();
		while (i.hasNext()) {
			temp = leaveHistoryDetailsRepo.findByEmployeeAndStatusOrStatus(i.next(), Status.APPLIED,Status.UPDATED);
			if (temp.size() != 0)
				leaveList.addAll(temp);
		}

		model.addAttribute("leave_list", leaveList);

		return "approval_list";
	}

	@RequestMapping(path = "/leave/leave_history", method = RequestMethod.GET)
	public String showLeaveHistory(Model model) {

		Employee t = emp.GetUser();
		if (!t.getRole().getRoleName().equals("Manager"))
			return "redirect:/logout";

		List<Employee> empList = employeeRepo.findByReportsTo(t);
		leaveList = new ArrayList<LeaveHistoryDetails>();

		Iterator<Employee> i = empList.iterator();
		while (i.hasNext()) {
			leaveList.addAll(leaveHistoryDetailsRepo.findByEmployee(i.next()));
		}
		toExportList.clear();
		toExportList.addAll(leaveList);
		model.addAttribute("startdate", "dd/mm/yyyy");
		model.addAttribute("enddate", "dd/mm/yyyy");
		model.addAttribute("status", "ALL");
		model.addAttribute("leave_type", "ALL");
		model.addAttribute("leave_list", leaveList);
		model.addAttribute("leave_type_lists", leaveTypeList);

		return "leave_history";
	}

	@RequestMapping(path = "/leave/filter", method = RequestMethod.GET)
	public String filterLeaveHistory(@RequestParam String leave_type, @RequestParam String status,
			@RequestParam String start_date, @RequestParam String end_date, Model model) {
		Employee t = emp.GetUser();
		if (!t.getRole().getRoleName().equals("Manager"))
			return "redirect:/logout";

		boolean dateFilter = start_date != "" && end_date != "";
		if (leave_type.equals("ALL") && status.equals("ALL") && !dateFilter)
			return "redirect:/leave/leave_history";
		  
		filter(dateFilter, start_date, end_date, leave_type, status);
		
		model.addAttribute("startdate", start_date);
		model.addAttribute("enddate", end_date);
		model.addAttribute("status", status);
		model.addAttribute("leave_type", leave_type);
		model.addAttribute("leave_list", toExportList);
		model.addAttribute("leave_type_lists",leaveTypeList);

		return "leave_history";
	}

	private void filter( boolean dateFilter,String start_date, String end_date, String leave_type, String status) {
		toExportList.clear();
		if (dateFilter) {
			toExportList = leaveService.filterByDate(leaveList, start_date, end_date);
		}

		if (!leave_type.equals("ALL") && !status.equals("ALL")) {
			toExportList = leaveService.filterByLeaveTypeStatus(leaveList, leave_type, status);
		} else if (status.equals("ALL")) {
			toExportList = leaveService.filterByLeaveType(leaveList, leave_type);
		} else if (leave_type.equals("ALL")) {
			toExportList = leaveService.filterByStatus(leaveList, status);
		}		
	}

	@RequestMapping(path = "leave/edit_leave/{leave_history_id}/{reject}", method = RequestMethod.GET)
	public String updateleave(Model model, @PathVariable(value = "leave_history_id") String leave_history_id, @PathVariable(value="reject") String reject) {
		Employee t = emp.GetUser();
		if (!t.getRole().getRoleName().equals("Manager"))
			return "redirect:/logout";

		LeaveHistoryDetails lhd = leaveHistoryDetailsRepo.findById(Integer.valueOf(leave_history_id)).orElse(null);
		if(reject.equals("null")) {
			reject="      ";
			}
		model.addAttribute("reason",reject);
		model.addAttribute("updateleave", lhd);
		return "edit_leave";
	}

	@RequestMapping(path = "leave/leave_detail/{leave_history_id}", method = RequestMethod.GET)
	public String leaveDetail(Model model, @PathVariable(value = "leave_history_id") String leave_history_id) {
		Employee t = emp.GetUser();
		if (!t.getRole().getRoleName().equals("Manager"))
			return "redirect:/logout";

		LeaveHistoryDetails lhd = leaveHistoryDetailsRepo.findById(Integer.valueOf(leave_history_id)).orElse(null);
		model.addAttribute("leave_detail", lhd);
		return "leave_detail";
	}

	@RequestMapping(path = "leave/update_leave", method = RequestMethod.GET)
	public String saveleavestatus(@RequestParam("action") String action,
			@RequestParam("leaveHistoryId") String leaveHistoryId, @RequestParam("reasons") String reasons,Model model) {
		String a;
		
		Employee t = emp.GetUser();
		if (!t.getRole().getRoleName().equals("Manager"))
			return "redirect:/logout";

		LeaveHistoryDetails ldh = leaveHistoryDetailsRepo.findById(Integer.valueOf(leaveHistoryId)).orElse(null);
		String s= "redirect:/leave/edit_leave/{" + ldh.getLeaveHistoryId() + "}";
		if (action.equals("2")) {
			ldh.setStatus(Status.REJECTED);
			if(reasons == "") {
				a = "Please Enter Rejection Reason";
				System.out.println(a);
				model.addAttribute("reason",a);
			return "redirect:/leave/edit_leave/"+ldh.getLeaveHistoryId()+"/"+a;			
			}
			sendMail(ldh.getEmployee().getEmail(), Status.REJECTED.get(), intro + reasons);
			ldh.setRejectionReason(reasons);
			leaveHistoryDetailsRepo.save(ldh);
			return "redirect:/leave/approval_list";
		} 
		else if (action.equals("1")) {
			ldh.setStatus(Status.APPROVED);
			updateLeaveCount(ldh.getEmployee(), ldh);
			sendMail(ldh.getEmployee().getEmail(), Status.APPROVED.get(), "Ok.I accept.");
			leaveHistoryDetailsRepo.save(ldh);
			return "redirect:/leave/approval_list";
		}
		return null;
	}

	private void updateLeaveCount(Employee t, LeaveHistoryDetails ldh) {
		int noOfDays = leaveService.noOfDays(ldh.getStartDate(), ldh.getEndDate());
		Employee e = emp.updateLeaveCount(t, ldh.getLeaveName(), noOfDays * (-1));// noOfDays value to negative3
		employeeRepo.save(e);
	}

//		********* Send Mail *********
	@Autowired
	private JavaMailSender javaMailSender;

	public void sendMail(String email, String status, String reasons) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(email);
		msg.setFrom("isslaps.hr@gmail.com");

		msg.setSubject("LEAVE " + status);
		msg.setText(reasons);

		javaMailSender.send(msg);
	}

//		********* Export to Excel file *********
	@GetMapping("/leave/export")
	public void exportCSV(HttpServletResponse response) throws Exception {
		String filename = "LeaveList.csv";
		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// Header
		PrintWriter pw = response.getWriter()
				.append("NAME,LEAVE_TYPE,START_DATE,END_DATE,APPLY_REASON,REJECT_REASON,WORK_DESEMINATION\n");
		// object list
		StatefulBeanToCsv<LeaveHistoryDetails> writer = new StatefulBeanToCsvBuilder<LeaveHistoryDetails>(pw)
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(false).build();

		writer.write(toExportList);
	}

}
