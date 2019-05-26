package com.leave.project.REPOSITORIES;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leave.project.MODELS.LeaveType;

@Repository
public interface LeaveTypeRepo extends JpaRepository<LeaveType, Integer>{

}
