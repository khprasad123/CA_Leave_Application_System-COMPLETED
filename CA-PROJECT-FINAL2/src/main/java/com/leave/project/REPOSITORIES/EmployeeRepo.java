package com.leave.project.REPOSITORIES;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.leave.project.MODELS.Employee;
import com.leave.project.MODELS.Role;
@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer>{

	List<Employee> findByUserNameAndPassword(String userName, String password);

	List<Employee> findByReportsTo(Employee reportsTo);

	Employee findByUserName(String userName);
	
}
