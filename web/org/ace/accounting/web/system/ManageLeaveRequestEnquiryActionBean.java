package org.ace.accounting.web.system;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.system.employee.Employee;
import org.ace.accounting.system.leaverequest.LeaveRequest;
import org.ace.accounting.system.leaverequest.service.interfaces.ILeaveRequestService;
import org.ace.java.web.common.BaseBean;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "ManageLeaveRequestEnquiryActionBean")
@ViewScoped
public class ManageLeaveRequestEnquiryActionBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{LeaveRequestService}")
	private ILeaveRequestService leaveRequestService;

	private List<LeaveRequest> resultList;
	private LeaveRequest selectedLeaveRequest;

	// ===== Criteria Object =====
	public static class LeaveCriteria implements Serializable {
		private String employeeName;
		private String leaveType;
		private String status;
		private Date startDate;
		private Date endDate;

		// Getters & Setters
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

	private LeaveCriteria criteria = new LeaveCriteria();

	// ===== Methods =====
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

	public void approve(LeaveRequest request) {
		try {
			request.setStatus("APPROVED");
			leaveRequestService.updateLeaveRequest(request);
			search(); // refresh table
			addInfoMessage("Success", "Leave Request Approved Successfully");
		} catch (Exception e) {
			addErrorMessage("Approval Error", e.getMessage());
			e.printStackTrace();
		}
	}

	public void reject(LeaveRequest request) {
		try {
			request.setStatus("REJECTED");
			leaveRequestService.updateLeaveRequest(request);
			search(); // refresh table
			addInfoMessage("Success", "Leave Request Rejected Successfully");
		} catch (Exception e) {
			addErrorMessage("Rejection Error", e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * public StreamedContent firstMedicalRecordImage(LeaveRequest leave) { try {
	 * return leaveRequestService.getFirstMedicalRecordImage(leave); } catch
	 * (Exception e) { e.printStackTrace(); return null; } }
	 */

	// ===== Getters & Setters =====
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

	public LeaveCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(LeaveCriteria criteria) {
		this.criteria = criteria;
	}
}
