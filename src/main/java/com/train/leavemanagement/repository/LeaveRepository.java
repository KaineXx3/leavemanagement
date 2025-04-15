package com.train.leavemanagement.repository;

import com.train.leavemanagement.entity.Leave;
import com.train.leavemanagement.entity.LeaveStatusType;
import com.train.leavemanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByUser(User user);

    List<Leave> findByApplicationStatus(LeaveStatusType leaveStatusType);


}
