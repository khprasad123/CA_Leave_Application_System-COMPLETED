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
import com.leave.project.REPOSITORIES.PublicHollydayRepo;
import com.leave.project.SERVICES.IEmployeeService;

@Controller
public class AdminHollydayController {
	@Autowired
	private IEmployeeService emp;
	
	private PublicHollydayRepo phRepo;
	@Autowired
	public void setPhRepo(PublicHollydayRepo phRepo) {
		this.phRepo = phRepo;
	}
	
	@GetMapping(path="/admin/Hollyday/view")
	public String viewHollydays(Model model) {
		
		Employee t=emp.GetUser();
		if(!t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		model.addAttribute("hollydays",phRepo.findAll());
		return "ViewHollydays";
	}
	
	@GetMapping(path="/admin/Hollyday/add")
	public String getHollydayForm(Model model) {
		
		Employee t=emp.GetUser();
		if(!t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		model.addAttribute("hollyday",new PublicHollyday());
		return "HollydayForm";
	}
	
	@PostMapping(path="/admin/Hollyday")
	public String saveHollyday(Model model,PublicHollyday E) {
		
		Employee t=emp.GetUser();
		if(!t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		phRepo.save(E);
		return "redirect:/admin/Hollyday/view";
	}

	
	 @GetMapping(path="/admin/Hollyday/edit/{id}") 
	 public String editHollyday(@PathVariable(name = "id") int id,Model model) {
			
			Employee t=emp.GetUser();
			if(!t.getRole().getRoleName().equals("Admin"))
				return "redirect:/logout";
		 
		 model.addAttribute("hollyday",phRepo.findById(id)); 
		 return "HollydayForm";
	 }
	 
	
//for deletion simple 
	@RequestMapping(path ="/admin/Hollyday/delete/{id}", method = RequestMethod.GET)
	public String deleteProduct(@PathVariable(name = "id") int id,Model model){
		
		Employee t=emp.GetUser();
		if(!t.getRole().getRoleName().equals("Admin"))
			return "redirect:/logout";
		
		
		phRepo.delete(phRepo.findById(id).orElse(null)); 
		return "redirect:/admin/Hollyday/view";
	}
}
