package com.leave.project.ADMIN.CONTROLLERS;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.leave.project.MODELS.Employee;
import com.leave.project.REPOSITORIES.EmployeeRepo;
import com.leave.project.REPOSITORIES.RoleRepo;
import com.leave.project.SERVICES.IEmployeeService;

@Controller
public class AdminEmployeeController {
	
	@Autowired
	private IEmployeeService emp;
	
	private EmployeeRepo empRepo;
	@Autowired
	public void setEmprepo(EmployeeRepo emprepo) {
		this.empRepo = emprepo;
	}
	
	private RoleRepo roleRepo;
	@Autowired
	public void setRoleRepo(RoleRepo roleRepo) {
		this.roleRepo = roleRepo;
	}

	
	@GetMapping(path = "/admin/Employee/view")
	public String getEmployeeList(Model model) {
		
		Employee t=emp.GetUser();
		
	
		if(!t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";

		
		ArrayList<Employee> list = (ArrayList<Employee>) empRepo.findAll();
		list = (ArrayList<Employee>)list.stream().filter(emp -> !emp.getRole().getRoleName().equalsIgnoreCase("admin")).collect(Collectors.toList());
		model.addAttribute("employees", list);
		return "ViewEmployees";
	}
	
	
	@GetMapping(path = "/admin/Employee/add")
	public String getEmployeeForm(Model model) {
		
		Employee t=emp.GetUser();
		if(!t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		
		List<Employee> managers = empRepo.findAll();
		managers = (List<Employee>)managers.stream().filter(emp -> emp.getRole().getRoleName().equalsIgnoreCase("manager")).collect(Collectors.toList());
		model.addAttribute("MANAGERS", managers);
		model.addAttribute("EMPLOYEE_ROLES",roleRepo.findAll());
		model.addAttribute("employee", new Employee());
		return "EmployeeAdd";
	}
	
	
	
	
	@PostMapping(path = "/admin/Employee")
	public String saveEmployee(Model model,Employee E) {
		
		Employee t=emp.GetUser();
		if(!t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		try{
			empRepo.save(E);
		}catch(Exception e) {
			System.out.println("Error Bro What To do");
		}
		return "redirect:/admin/Employee/view";
	}
	
	
	@GetMapping(path = "/admin/Employee/edit/{id}")
	public String editProduct(@PathVariable(value = "id") int id,Model model) {
		
		Employee t=emp.GetUser();
		if(!t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		List<Employee> managers = empRepo.findAll();
		managers = (List<Employee>)managers.stream().filter(emp -> emp.getRole().getRoleName().equalsIgnoreCase("manager")).collect(Collectors.toList());
		model.addAttribute("MANAGERS", managers);
		model.addAttribute("EMPLOYEE_ROLES",roleRepo.findAll());
		model.addAttribute("employee", empRepo.findById(id));
		return "EmployeeAdd";
	}
	
	
	
	@RequestMapping(path = "/admin/Employee/delete/{id}", method = RequestMethod.GET)
	public String deleteProduct(@PathVariable(name = "id") int id,Model model) {
		
		Employee t=emp.GetUser();
		if(!t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		try{
			empRepo.delete(empRepo.findById(id).orElse(null));
		}catch(Exception e) {
			System.out.println("Employee Referencing himself");
		}
		return "redirect:/admin/Employee/view";
	}
	
}
