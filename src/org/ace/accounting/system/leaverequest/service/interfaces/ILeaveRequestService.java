package org.ace.accounting.system.leaverequest.service.interfaces;

import java.util.List;

import org.ace.accounting.system.leaverequest.LeaveRequest;
import org.ace.java.component.SystemException;
import org.primefaces.model.StreamedContent;

public interface ILeaveRequestService {

	public List<LeaveRequest> findAllLeaveRequest() throws SystemException;

	public void addNewLeaveRequest(LeaveRequest leaveRequest) throws SystemException;

	public void updateLeaveRequest(LeaveRequest leaveRequest) throws SystemException;

	public void deleteLeaveRequest(LeaveRequest leaveRequest) throws SystemException;

	public List<LeaveRequest> searchLeaveRequests(String employeeName, String leaveType, String status);

	/* public StreamedContent getFirstMedicalRecordImage(LeaveRequest leave); */

}
