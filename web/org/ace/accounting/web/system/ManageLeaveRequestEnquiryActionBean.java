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

	private String approveReason;
	private String rejectReason;
	private Date approveDate;
	private Date rejectDate;

	private LeaveCriteria criteria = new LeaveCriteria();

	@PostConstruct
	public void init() {
		setDefaultDates(); // page load အချိန်မှာ default date ချမှတ်မယ်
	}

	public void search() {
		try {
			resultList = leaveRequestService.searchLeaveRequests(criteria.getEmployeeName(), criteria.getLeaveType(),
					criteria.getStatus());
			if (resultList.isEmpty()) {
				addInfoMessage("Search Result", "No leave requests found.");
			}
		} catch (Exception e) {
			addErrorMessage("Search Error", e.getMessage());
			e.printStackTrace();
		}
	}

	// Prepare Approve Dialog
	public void prepareApprove(LeaveRequest leave) {
		this.selectedLeaveRequest = leave;
		this.approveDate = new Date(); // default to today
		this.approveReason = null;
	}

	// Prepare Reject Dialog
	public void prepareReject(LeaveRequest leave) {
		this.selectedLeaveRequest = leave;
//		this.rejectReason = null; // clear previous reason
//		this.rejectDate = new Date(); // set today as default
	}

	// Approve method
	public String approve() {
		try {
//			if (selectedLeaveRequest != null) {
//				selectedLeaveRequest.setStatus("APPROVED");
//				/*
//				 * selectedLeaveRequest.setApproveReason(approveReason);
//				 */ selectedLeaveRequest.setApprovedDate(approveDate); // use today
//				selectedLeaveRequest.setApproveReason(approveReason);
//				leaveRequestService.updateLeaveRequest(selectedLeaveRequest);
//				search(); // refresh table
//				addInfoMessage("Success", "Leave Request Approved Successfully");
//			}
			if (selectedLeaveRequest == null) {
				addErrorMessage("Approval Error", "No leave request selected.");
				return null;
			}

			// DEBUG
			System.out.println("approveReason=" + approveReason + ", approveDate=" + approveDate);

			selectedLeaveRequest.setStatus("APPROVED");
			selectedLeaveRequest.setApproveReason(approveReason);
			selectedLeaveRequest.setApprovedDate(approveDate);

			leaveRequestService.updateLeaveRequest(selectedLeaveRequest);

			search(); // refresh table
			addInfoMessage("Success", "Leave Request Approved Successfully");

			return null;
		} catch (Exception e) {
			addErrorMessage("Approval Error", e.getMessage());
            e.printStackTrace();
            return null;
		}
	}

	// Reject method
	public void reject() {
		try {
			if (selectedLeaveRequest != null) {
				selectedLeaveRequest.setStatus("REJECTED");
				selectedLeaveRequest.setRejectReason(rejectReason);
				selectedLeaveRequest.setRejectedDate(rejectDate); // use today
				leaveRequestService.updateLeaveRequest(selectedLeaveRequest);
				search(); // refresh table
				addInfoMessage("Success", "Leave Request Rejected Successfully");
			}
		} catch (Exception e) {
			addErrorMessage("Rejection Error", e.getMessage());
			e.printStackTrace();
		}
	}

	private void setDefaultDates() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		criteria.endDate = cal.getTime(); // today
		cal.add(Calendar.DAY_OF_MONTH, -7);
		criteria.startDate = cal.getTime(); // 7 days before today
	}

	public void reset() {
		criteria = new LeaveCriteria();
		setDefaultDates(); // reset လုပ်တဲ့အချိန် default ပြန်သွားမယ်
	}

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

	public LeaveCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(LeaveCriteria criteria) {
		this.criteria = criteria;
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

	// LeaveCriteria class (as you had)
	public static class LeaveCriteria implements Serializable {
		private String employeeName;
		private String leaveType;
		private String status;
		private Date startDate;
		private Date endDate;

		// Getters/Setters
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
}
