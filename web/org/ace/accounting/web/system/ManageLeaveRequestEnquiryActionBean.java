package org.ace.accounting.web.system;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.system.leaverequest.LeaveRequest;
import org.ace.accounting.system.leaverequest.service.interfaces.ILeaveRequestService;
import org.ace.java.web.common.BaseBean;

@ManagedBean(name = "ManageLeaveRequestEnquiryActionBean")
@ViewScoped
public class ManageLeaveRequestEnquiryActionBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{LeaveRequestService}")
	private ILeaveRequestService leaveRequestService;

	private List<LeaveRequest> resultList;
	private LeaveRequest selectedLeaveRequest;

	// For Approve/Reject
	private String approveReason;
	private String rejectReason;
	private Date approveDate;
	private Date rejectDate;

	// Search fields (replace LeaveCriteria)
	private String employeeName;
	private String leaveType;
	private String status;
	private Date startDate;
	private Date endDate;

	@PostConstruct
	public void init() {
		setDefaultDates();
	}

	// Search directly with fields
	public void search() {
		try {
			resultList = leaveRequestService.searchLeaveRequests(employeeName, leaveType, status);
			if (resultList.isEmpty()) {
				addInfoMessage("Search Result", "No leave requests found.");
			}
		} catch (Exception e) {
			addErrorMessage("Search Error", e.getMessage());
			e.printStackTrace();
		}
	}

	// Approve dialog
	public void prepareApprove(LeaveRequest leave) {
		this.selectedLeaveRequest = leave;
		this.approveReason = null;
		this.approveDate = new Date();
	}

	// Reject dialog
	public void prepareReject(LeaveRequest leave) {
		this.selectedLeaveRequest = leave;
		this.rejectReason = null;
		this.rejectDate = new Date();
	}

	// Approve method
	public void approve() {
		try {
  			if (selectedLeaveRequest != null) {
				selectedLeaveRequest.setStatus("APPROVED");
				selectedLeaveRequest.setApproveReason(approveReason);      
				selectedLeaveRequest.setApprovedDate(approveDate);
				leaveRequestService.updateLeaveRequest(selectedLeaveRequest);
				search();
				addInfoMessage("Success", "Leave Request Approved Successfully");
			}
		} catch (Exception e) {
			addErrorMessage("Approval Error", e.getMessage());
			e.printStackTrace();
		}
	}

	// Reject method
	public void reject() {
		try {
			if (selectedLeaveRequest != null) {
				selectedLeaveRequest.setStatus("REJECTED");
				selectedLeaveRequest.setRejectReason(rejectReason);
				selectedLeaveRequest.setRejectedDate(rejectDate);
				leaveRequestService.updateLeaveRequest(selectedLeaveRequest);
				search();
				addInfoMessage("Success", "Leave Request Rejected Successfully");
			}
		} catch (Exception e) {
			addErrorMessage("Rejection Error", e.getMessage());
			e.printStackTrace();
		}
	}

	// Default date range (today and 7 days before)
	private void setDefaultDates() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		endDate = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -7);
		startDate = cal.getTime();
	}

	// Reset search fields
	public void reset() {
		employeeName = null;
		leaveType = null;
		status = null;
		setDefaultDates();
	}

	// Select for details
	public void selectLeave(LeaveRequest leave) {
		this.selectedLeaveRequest = leave;
	}

	// Getters and Setters
	public ILeaveRequestService getLeaveRequestService() {
		return leaveRequestService;
	}

	public void setLeaveRequestService(ILeaveRequestService leaveRequestService) {
		this.leaveRequestService = leaveRequestService;
	}

	public List<LeaveRequest> getResultList() {
		return resultList;
	}

	public void setResultList(List<LeaveRequest> resultList) {
		this.resultList = resultList;
	}

	public LeaveRequest getSelectedLeaveRequest() {
		return selectedLeaveRequest;
	}

	public void setSelectedLeaveRequest(LeaveRequest selectedLeaveRequest) {
		this.selectedLeaveRequest = selectedLeaveRequest;
	}

	public String getApproveReason() {
		return approveReason;
	}

	public void setApproveReason(String approveReason) {
		this.approveReason = approveReason;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	public Date getRejectDate() {
		return rejectDate;
	}

	public void setRejectDate(Date rejectDate) {
		this.rejectDate = rejectDate;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
