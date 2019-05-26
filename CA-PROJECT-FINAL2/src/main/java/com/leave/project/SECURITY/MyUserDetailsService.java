package com.leave.project.SECURITY;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.leave.project.MODELS.Employee;
import com.leave.project.REPOSITORIES.EmployeeRepo;
@Service
public class MyUserDetailsService implements UserDetailsService {
	@Autowired
	private EmployeeRepo empRepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Employee user=empRepo.findByUserName(username);
		if(user==null)
			throw new UsernameNotFoundException("UserName Error Bro");
		
		return new UserPrincipal(user);
	}

}
