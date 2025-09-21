package org.ace.accounting.web.system;

import org.ace.accounting.common.FileHandler;
import org.ace.accounting.system.employee.Employee;
import org.ace.accounting.system.leaverequest.AttachFile;
import org.ace.accounting.system.leaverequest.LeaveRequest;
import org.ace.accounting.system.leaverequest.service.interfaces.ILeaveRequestService;
import org.ace.java.web.common.BaseBean;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.*;

@ManagedBean(name = "ManageLeaveRequestActionBean")
@ViewScoped
public class ManageLeaveRequestActionBean extends BaseBean implements Serializable {

	private LeaveRequest leaveRequest;
	private LeaveRequest selectedLeaveRequest;
	private List<LeaveRequest> leaveRequests;
	private Employee employee;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	private Map<String, String> medicalUploadedFileMap = new LinkedHashMap<>();

	private String temporyDir = "/uploads/";

	@ManagedProperty(value = "#{LeaveRequestService}")
	private ILeaveRequestService leaveRequestService;

	public void setLeaveRequestService(ILeaveRequestService leaveRequestService) {
		this.leaveRequestService = leaveRequestService;
	}

	@PostConstruct
	public void init() {
		leaveRequest = new LeaveRequest();
		leaveRequest.setAttachFiles(new ArrayList<>());
		leaveRequests = leaveRequestService.findAllLeaveRequest();
	}

	public void onLeaveTypeChange() {
		if (!"MEDICAL".equals(leaveRequest.getLeaveType())) {
			medicalUploadedFileMap.clear();
		}
	}

	/*
	 * public void handleProposalAttachment(FileUploadEvent event) { UploadedFile
	 * uploadedFile = event.getFile(); String fileName =
	 * uploadedFile.getFileName().replaceAll("\\s", "_");
	 * 
	 * if (!medicalUploadedFileMap.containsKey(fileName)) { try { String filePath =
	 * temporyDir + fileName; File targetFile = new File(getUploadPath() +
	 * filePath); targetFile.getParentFile().mkdirs(); // create directories if not
	 * exist Files.write(targetFile.toPath(), uploadedFile.getContents());
	 * medicalUploadedFileMap.put(fileName, filePath);
	 * leaveRequest.getAttachFiles();
	 * 
	 * addInfoMessage("Upload Success", fileName + " uploaded successfully."); }
	 * catch (IOException e) { addErrorMessage("Upload Error", e.getMessage());
	 * e.printStackTrace(); } } }
	 */

	public void handleProposalAttachment(FileUploadEvent event) {
		UploadedFile uploadedFile = event.getFile();
		String fileName = uploadedFile.getFileName().replaceAll("\\s", "_");

		if (!medicalUploadedFileMap.containsKey(fileName)) {
			String filePath = null;
			if (leaveRequest.getId() != null) {
				filePath = temporyDir + leaveRequest.getId() + "/" + "/Medical_Record/" + fileName;
			} else {
				filePath = temporyDir + "/" + fileName;
			}

			medicalUploadedFileMap.put(fileName, filePath);
			try {
				String physicalPath = getUploadPath() + filePath;

				FileHandler.createFile(new File(physicalPath), uploadedFile.getContents());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public List<String> getMedicalUploadedFileList() {
		return new ArrayList<>(medicalUploadedFileMap.values());
	}

	public StreamedContent getMedicalRecordImage(String filePath) {
		if (filePath == null || filePath.isEmpty()) {
			return null; // Null safe
		}
		try {
			File file = new File(getUploadPath() + filePath);
			if (!file.exists())
				return null; // File not found

			String contentType = Files.probeContentType(file.toPath());
			FileInputStream fis = new FileInputStream(file);

			// PrimeFaces 7.0 compatible way
			return new DefaultStreamedContent(fis, contentType, file.getName());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean isPdfFile(String filePath) {
		return filePath.toLowerCase().endsWith(".pdf");
	}

	public boolean isImageFile(String filePath) {
		String lower = filePath.toLowerCase();
		return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith(".gif");
	}

	// ပထမဆုံး attach file path ကို return
	public AttachFile firstMedicalRecordImage(LeaveRequest leave) {
		if (leave != null && leave.getAttachFiles() != null && !leave.getAttachFiles().isEmpty()) {
			return leave.getAttachFiles().get(0);
		}
		return null;
	}

	public void save() {
		try {
			// Always set status to Pending on submit
			leaveRequest.setStatus("PENDING");

			leaveRequestService.addNewLeaveRequest(leaveRequest);
			leaveRequests = leaveRequestService.findAllLeaveRequest();

			addInfoMessage("Success", "Leave Request Saved Successfully (Status: Pending)");
			reset();
		} catch (Exception e) {
			addErrorMessage("Error saving LeaveRequest:", e.getMessage());
			e.printStackTrace();
		}
	}

	public void createNewLeaveRequest() {
		reset();
	}

	private void reset() {
		leaveRequest = new LeaveRequest();
		/*
		 * leaveRequest.setAttachFiles(new ArrayList<>());
		 * medicalUploadedFileMap.clear(); employee = null;
		 * leaveRequest.setLeaveType(null);
		 */
	}

	public String update() {
		try {
			leaveRequestService.updateLeaveRequest(selectedLeaveRequest);
			leaveRequests = leaveRequestService.findAllLeaveRequest();
			addInfoMessage("Success", "Leave Request Updated Successfully");
		} catch (Exception e) {
			addErrorMessage("Update Error", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void delete(LeaveRequest request) {
		try {
			leaveRequestService.deleteLeaveRequest(request);
			leaveRequests = leaveRequestService.findAllLeaveRequest();
			addInfoMessage("Success", "Leave Request Deleted Successfully");
		} catch (Exception e) {
			addErrorMessage("Delete Error", e.getMessage());
			e.printStackTrace();
		}
	}

	public void returnEmployee(SelectEvent event) {
		Employee employee = (Employee) event.getObject();
		leaveRequest.setEmployee(employee);
	}

	// ================= Getters & Setters =================
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

}
