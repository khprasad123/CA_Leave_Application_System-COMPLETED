package com.leave.project.REPOSITORIES;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leave.project.MODELS.PublicHollyday;
@Repository
public interface PublicHollydayRepo extends JpaRepository<PublicHollyday, Integer>{
	List<PublicHollyday> findByStartDateBetween(Date startDate,Date endDate);
	List<PublicHollyday> findByStartDateOrStartDate(Date startDate,Date endDate);

}
