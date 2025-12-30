package com.arun.Brocode.Repository;

import com.arun.Brocode.model.LeaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;

@Repository
public class LeaveRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insertLeaveRequest(LeaveRequest leaveRequest) {
        String sql = "INSERT INTO leave_requests (employee_sn, leave_type, start_date, end_date, reason) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                leaveRequest.getEmployeeSn(),
                leaveRequest.getLeaveType(),
                leaveRequest.getStartDate(),
                leaveRequest.getEndDate(),
                leaveRequest.getReason());
    }

    public List<LeaveRequest> findAllByEmployeeSn(int employeeSn) {
        String sql = "SELECT * FROM leave_requests WHERE employee_sn = ? ORDER BY applied_at DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            LeaveRequest lr = new LeaveRequest();
            lr.setLeaveId(rs.getInt("leave_id"));
            lr.setEmployeeSn(rs.getInt("employee_sn"));
            lr.setLeaveType(rs.getString("leave_type"));
            lr.setStartDate(rs.getDate("start_date"));
            lr.setEndDate(rs.getDate("end_date"));
            lr.setReason(rs.getString("reason"));
            lr.setStatus(rs.getString("status"));
            lr.setAppliedAt(rs.getTimestamp("applied_at"));
            lr.setReviewedBy(rs.getObject("reviewed_by", Integer.class));
            lr.setReviewedAt(rs.getTimestamp("reviewed_at"));
            lr.setAdminRemarks(rs.getString("admin_remarks"));
            return lr;
        }, employeeSn);
    }

    public List<LeaveRequest> findAllPending() {
        String sql = "SELECT lr.*, e.name as employee_name FROM leave_requests lr JOIN employee e ON lr.employee_sn = e.sn WHERE lr.status = 'PENDING' ORDER BY lr.applied_at ASC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            LeaveRequest lr = new LeaveRequest();
            lr.setLeaveId(rs.getInt("leave_id"));
            lr.setEmployeeSn(rs.getInt("employee_sn"));
            lr.setLeaveType(rs.getString("leave_type"));
            lr.setStartDate(rs.getDate("start_date"));
            lr.setEndDate(rs.getDate("end_date"));
            lr.setReason(rs.getString("reason"));
            lr.setStatus(rs.getString("status"));
            lr.setAppliedAt(rs.getTimestamp("applied_at"));
            lr.setReviewedBy(rs.getObject("reviewed_by", Integer.class));
            lr.setReviewedAt(rs.getTimestamp("reviewed_at"));
            lr.setAdminRemarks(rs.getString("admin_remarks"));
            // Optionally, set employee name if needed for display
            // lr.setEmployeeName(rs.getString("employee_name")); // Assuming LeaveRequest model has this field
            return lr;
        });
    }

    public int updateLeaveStatus(int leaveId, String status, int reviewedBy, String adminRemarks) {
        String sql = "UPDATE leave_requests SET status = ?, reviewed_by = ?, reviewed_at = CURRENT_TIMESTAMP, admin_remarks = ? WHERE leave_id = ?";
        return jdbcTemplate.update(sql, status, reviewedBy, adminRemarks, leaveId);
    }

    public int deleteLeaveRequest(int leaveId) {
        String sql = "DELETE FROM leave_requests WHERE leave_id = ?";
        return jdbcTemplate.update(sql, leaveId);
    }
}

