package org.ace.accounting.web.system;

import org.ace.accountig.system.leaverequest.LeaveRequest;
import org.ace.accountig.system.leaverequest.service.interfaces.ILeaveRequestService;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.DefaultStreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "manageLeaveRequestActionBean")
@ViewScoped
public class ManageLeaveRequestActionBean implements Serializable {

	private LeaveRequest leaveRequest;
	private LeaveRequest selectedLeaveRequest;

	private List<LeaveRequest> leaveRequests;
	private UploadedFile file; // uploaded image

	@ManagedProperty(value = "#{LeaveRequestService}")
	private ILeaveRequestService leaveRequestService;

	public void setLeaveRequestService(ILeaveRequestService leaveRequestService) {
		this.leaveRequestService = leaveRequestService;
	}

	@PostConstruct
	public void init() {
		leaveRequest = new LeaveRequest();
		leaveRequests = leaveRequestService.findAllLeaveRequest();
	}

	// --- getters and setters ---
	public LeaveRequest getLeaveRequest() {
		return leaveRequest;
	}

	public void setLeaveRequest(LeaveRequest leaveRequest) {
		this.leaveRequest = leaveRequest;
	}

	public LeaveRequest getSelectedLeaveRequest() {
		return selectedLeaveRequest;
	}

	public void setSelectedLeaveRequest(LeaveRequest selectedLeaveRequest) {
		this.selectedLeaveRequest = selectedLeaveRequest;
	}

	public List<LeaveRequest> getLeaveRequests() {
		return leaveRequests;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	// --- CRUD operations ---
	public String save() {
		try {
			if (file != null) {
				leaveRequest.setMedicalRecord(file.getContents());
			}
			leaveRequestService.addNewLeaveRequest(leaveRequest);
			leaveRequests = leaveRequestService.findAllLeaveRequest(); // refresh
			leaveRequest = new LeaveRequest(); // reset form
			file = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String update() {
		try {
			if (file != null && selectedLeaveRequest != null) {
				selectedLeaveRequest.setMedicalRecord(file.getContents());
			}
			leaveRequestService.updateLeaveRequest(selectedLeaveRequest);
			leaveRequests = leaveRequestService.findAllLeaveRequest(); // refresh
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void delete(LeaveRequest request) {
		try {
			leaveRequestService.deleteLeaveRequest(request);
			leaveRequests = leaveRequestService.findAllLeaveRequest(); // refresh
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public StreamedContent getMedicalRecordImage(LeaveRequest request) {
		if (request != null && request.getMedicalRecord() != null) {
			return new DefaultStreamedContent(new ByteArrayInputStream(request.getMedicalRecord()), "image/png");
		}
		return null;
	}

}
