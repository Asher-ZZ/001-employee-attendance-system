package org.ace.accounting.system.leaverequest.persistence.interfaces;

import java.util.List;

import org.ace.accounting.system.leaverequest.LeaveRequest;
import org.ace.java.component.persistence.exception.DAOException;

public interface ILeaveRequestDAO {

	public void insert (LeaveRequest leaveRequest)throws DAOException;
	
	public void update (LeaveRequest leaveRequest) throws DAOException;
	
	public void delete (LeaveRequest leaveRequest)throws DAOException;
	
	public List<LeaveRequest> findAll() throws DAOException;
	
	 List<LeaveRequest> findAllLeaveRequest();

	public List<LeaveRequest> searchLeaveRequests(String employeeName, String leaveType, String status);
}
