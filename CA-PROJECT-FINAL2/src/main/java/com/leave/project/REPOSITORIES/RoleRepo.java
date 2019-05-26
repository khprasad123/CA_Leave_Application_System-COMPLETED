package com.leave.project.REPOSITORIES;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.leave.project.MODELS.*;
@Repository
public interface RoleRepo extends JpaRepository<Role, Integer>{
}
