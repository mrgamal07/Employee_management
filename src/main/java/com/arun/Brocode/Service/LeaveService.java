package com.arun.Brocode.Service;

import com.arun.Brocode.Repository.LeaveRepository;
import com.arun.Brocode.model.LeaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    public int applyForLeave(LeaveRequest leaveRequest) {
        return leaveRepository.insertLeaveRequest(leaveRequest);
    }

    public List<LeaveRequest> getEmployeeLeaveHistory(int employeeSn) {
        return leaveRepository.findAllByEmployeeSn(employeeSn);
    }

    public List<LeaveRequest> getAllPendingLeaveRequests() {
        return leaveRepository.findAllPending();
    }

    public int updateLeaveStatus(int leaveId, String status, int reviewedBy, String adminRemarks) {
        return leaveRepository.updateLeaveStatus(leaveId, status, reviewedBy, adminRemarks);
    }

    public int deleteLeaveRequest(int leaveId) {
        return leaveRepository.deleteLeaveRequest(leaveId);
    }
}
