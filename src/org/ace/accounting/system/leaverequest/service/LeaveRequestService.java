package org.ace.accounting.system.leaverequest.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.system.leaverequest.LeaveRequest;
import org.ace.accounting.system.leaverequest.persistence.interfaces.ILeaveRequestDAO;
import org.ace.accounting.system.leaverequest.service.interfaces.ILeaveRequestService;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "LeaveRequestService")
public class LeaveRequestService implements ILeaveRequestService {

	@Resource(name = "LeaveRequestDAO")
	private ILeaveRequestDAO leaveRequestDAO;

	@Transactional(propagation = Propagation.REQUIRED)
	public void addNewLeaveRequest(LeaveRequest leaveRequest) throws DAOException {
		try {
			leaveRequestDAO.insert(leaveRequest);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed To Add New LeaveRequest!", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateLeaveRequest(LeaveRequest leaveRequest) throws DAOException {
		try {
			leaveRequestDAO.update(leaveRequest);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed TO Update LeaveRequest!", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteLeaveRequest(LeaveRequest leaveRequest) throws DAOException {
		try {
			leaveRequestDAO.delete(leaveRequest);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed TO Delete LeaveRequest!", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<LeaveRequest> findAllLeaveRequest() throws SystemException {
		List<LeaveRequest> result = null;
		try {
			result = leaveRequestDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all LeaveRequests", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<LeaveRequest> searchLeaveRequests(String employeeName, String leaveType, String status) {
		return leaveRequestDAO.searchLeaveRequests(employeeName, leaveType, status);
	}
	/*
	 * public String getFirstMedicalRecordImage(LeaveRequest leaveRequest) { try {
	 * if (leaveRequest.getAttachFiles() != null &&
	 * !leaveRequest.getAttachFiles().isEmpty()) { String base64Data =
	 * leaveRequest.getAttachFiles().get(0).getFileData(); String contentType =
	 * leaveRequest.getAttachFiles().get(0).getContentType(); // e.g., "image/png"
	 * if (base64Data != null && !base64Data.isEmpty()) { // Return data URI return
	 * "data:" + contentType + ";base64," + base64Data; } } } catch (Exception e) {
	 * e.printStackTrace(); } return null; // fallback }
	 */
}
