package org.ace.accountig.system.leaverequest.service.interfaces;

import java.util.List;

import org.ace.accountig.system.leaverequest.LeaveRequest;
import org.ace.java.component.SystemException;

public interface ILeaveRequestService {

	public List<LeaveRequest> findAllLeaveRequest() throws SystemException;

	public void addNewLeaveRequest(LeaveRequest leaveRequest) throws SystemException;

	public void updateLeaveRequest(LeaveRequest leaveRequest) throws SystemException;

	public void deleteLeaveRequest(LeaveRequest leaveRequest) throws SystemException;

}
