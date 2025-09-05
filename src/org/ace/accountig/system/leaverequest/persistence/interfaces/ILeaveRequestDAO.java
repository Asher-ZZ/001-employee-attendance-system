package org.ace.accountig.system.leaverequest.persistence.interfaces;

import java.util.List;

import org.ace.accountig.system.leaverequest.LeaveRequest;
import org.ace.java.component.persistence.exception.DAOException;

public interface ILeaveRequestDAO {

	public void insert (LeaveRequest leaveRequest)throws DAOException;
	
	public void update (LeaveRequest leaveRequest) throws DAOException;
	
	public void delete (LeaveRequest leaveRequest)throws DAOException;
	
	public List<LeaveRequest> findAll() throws DAOException;
}
