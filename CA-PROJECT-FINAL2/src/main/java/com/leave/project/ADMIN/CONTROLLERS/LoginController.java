package com.leave.project.ADMIN.CONTROLLERS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.leave.project.MODELS.Employee;
import com.leave.project.SERVICES.IEmployeeService;

@Controller
@SessionAttributes("session")
public class LoginController {
	@Autowired
	private IEmployeeService emp;
	
	@GetMapping("/")
	public String redirectRoute() {
		return "redirect:/*/authenticate";
	}
	
	
	
	@GetMapping(path = "/*/authenticate")
	public String onAuthen(Model model) {		
		String mv="";
		Employee E =emp.GetUser();
		//mv=E.getRole().getRoleName().toUpperCase();
		if(E.getRole().getRoleName().equals("Manager"))	
			mv="MANAGER";
		else if(E.getRole().getRoleName().equals("Admin"))
			mv="ADMIN";
		else
			mv="STAFF";
		//so each new roles can be directed to staff view
		//Each view is Get for a particular role  if(admin   then view is ADMIN  		
		return mv;
	}
	@PostMapping(path = "/*/authenticate")
	public String onAuthenti(Model model) {		
		String mv="";
		Employee E =emp.GetUser();
		//mv=E.getRole().getRoleName().toUpperCase();
		if(E.getRole().getRoleName().equals("Manager"))	
			mv="MANAGER";
		else if(E.getRole().getRoleName().equals("Admin"))
			mv="ADMIN";
		else 
			mv="STAFF";
		//so each new roles can be directed to staff view
		//Each view is Get for a particular role  if(admin   then view is ADMIN  		
		return mv;
	}
}
