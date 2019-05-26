package com.leave.project.ADMIN.CONTROLLERS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.leave.project.MODELS.Employee;
import com.leave.project.MODELS.PublicHollyday;
import com.leave.project.MODELS.Role;
import com.leave.project.REPOSITORIES.PublicHollydayRepo;
import com.leave.project.REPOSITORIES.RoleRepo;
import com.leave.project.SERVICES.IEmployeeService;

@Controller
public class AdminRoleController {
	@Autowired
	private IEmployeeService emp;
	
	private RoleRepo phRepo;
	@Autowired
	public void setPhRepo(RoleRepo phRepo) {
		this.phRepo = phRepo;
	}
	
	@GetMapping(path="/admin/role/view")
	public String viewHollydays(Model model) {
		
		Employee t=emp.GetUser();
		if(!t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		model.addAttribute("hollydays",phRepo.findAll());
		return "ViewRoles";
	}
	
	@GetMapping(path="/admin/role/add")
	public String getRoleForm(Model model) {
		
		   Employee t=emp.GetUser(); 
		  if(!t.getRole().getRoleName().equals("Admin")) 
			  	return "redirect:/logout";
		 		
		model.addAttribute("role",new Role());
		return "RoleForm";
	}
	
	@PostMapping(path="/admin/role")
	public String saveHollyday(Model model,Role E) {
		
		Employee t=emp.GetUser();
		if(!t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		phRepo.save(E);
		return "redirect:/admin/role/view";
	}

	
//	 @GetMapping(path="/admin/role/edit/{id}") 
//	 public String editHollyday(@PathVariable(name = "id") int id,Model model) {
//			
//			Employee t=emp.GetUser();
//			if(!t.getRole().getRoleName().equals("Admin"))
//				return "redirect:/logout";
//		 
//		 model.addAttribute("role",phRepo.findById(id)); 
//		 return "RoleForm";
//	 }
	 
//	
////for deletion simple 
//	@RequestMapping(path ="/admin/role/delete/{id}", method = RequestMethod.GET)
//	public String deleteProduct(@PathVariable(name = "id") int id,Model model){
//		
//		Employee t=emp.GetUser();
//		if(!t.getRole().getRoleName().equals("Admin"))
//			return "redirect:/logout";
//		
//		
//		phRepo.delete(phRepo.findById(id).orElse(null)); 
//		return "redirect:/admin/role/view";
//	}
}
