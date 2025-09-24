package org.ace.accounting.web.system;

import org.ace.accounting.common.FileHandler;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.system.employee.Employee;
import org.ace.accounting.system.leaverequest.AttachFile;
import org.ace.accounting.system.leaverequest.LeaveRequest;
import org.ace.accounting.system.leaverequest.UploadFileConfig;
import org.ace.accounting.system.leaverequest.service.interfaces.ILeaveRequestService;
import org.ace.java.web.common.BaseBean;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.*;

@ManagedBean(name = "ManageLeaveRequestActionBean")
@ViewScoped
public class ManageLeaveRequestActionBean extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LeaveRequest leaveRequest = new LeaveRequest();
	private LeaveRequest selectedLeaveRequest;
	private List<LeaveRequest> leaveRequests;
	private Employee employee;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	private final String PROPOSAL_DIR = "/upload/medical-image";
	private Map<String, String> medicalUploadedFileMap;

	private String temporyDir;

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
		temporyDir = "/temp/" + System.currentTimeMillis() + "/";
		medicalUploadedFileMap = new HashMap<String, String>();
		String srcPath = getUploadPath() + PROPOSAL_DIR;
		String destPath = getUploadPath() + temporyDir;
		try {
			FileHandler.copyDirectory(srcPath, destPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String filePath = null;

		for (AttachFile attachFile : leaveRequest.getAttachFiles()) {
			filePath = attachFile.getFilePath();
			filePath = filePath.replaceAll("/upload/medical-image/", temporyDir);
		}

		for (AttachFile attFile : leaveRequest.getAttachFiles()) {
			medicalUploadedFileMap.put(attFile.getName(), attFile.getFilePath());
		}
		reset();
	}

	public void onLeaveTypeChange() {
		if (!"MEDICAL".equals(leaveRequest.getLeaveType())) {
			medicalUploadedFileMap.clear();
		}
	}

	public void handleProposalAttachment(FileUploadEvent event) {
		UploadedFile uploadedFile = event.getFile();
		String fileName = uploadedFile.getFileName().replaceAll("\\s", "_");
		String filePath = temporyDir + "/" + fileName;
		medicalUploadedFileMap.put(fileName, filePath);
		createFile(new File(getUploadPath() + filePath), uploadedFile.getContents());
		// medicalUploadedFileMap.put(fileName, filePath);
		// createFile(new File(getUploadPath() + filePath), uploadedFile.getContents());
//		String webPath = "/uploads/" + fileName; // adjust to match your servlet context mapping
//		medicalUploadedFileMap.put(fileName, webPath);
	}

	private void moveUploadedFiles() {
		try {
			FileHandler.moveFiles(getUploadPath(), temporyDir, PROPOSAL_DIR);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> getProposalAttachmentList() {
		return new ArrayList<String>(medicalUploadedFileMap.values());
	}

	public List<String> getMedicalUploadedFileList() {
		return new ArrayList<>(medicalUploadedFileMap.values());
	}

	public void removeProposalUploadedFile(String filePath) {
		try {
			String fileName = getFileName(filePath);
			medicalUploadedFileMap.remove(fileName);
			FileHandler.forceDelete(new File(getUploadPath() + filePath));
			if (medicalUploadedFileMap.isEmpty()) {
				FileHandler.forceDelete(new File(getUploadPath() + temporyDir));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public boolean isImageFile(String fileName) {
		return fileName.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)$");
	}

	public boolean isPdfFile(String filePath) {
		return filePath.toLowerCase().endsWith(".pdf");
	}

	// ပထမဆုံး attach file path ကို return
	public AttachFile firstMedicalRecordImage(LeaveRequest leave) {
		if (leave != null && leave.getAttachFiles() != null && !leave.getAttachFiles().isEmpty()) {
			return leave.getAttachFiles().get(0);
		}
		return null;
	}

	private void loadAttachment() {
		List<AttachFile> proposalAttachmentList = new ArrayList<>();
		for (String fileNameString : medicalUploadedFileMap.keySet()) {
			String filePath = PROPOSAL_DIR + "/" + fileNameString;
			proposalAttachmentList.add(new AttachFile(fileNameString, filePath));
		}
		leaveRequest.setAttachFiles(proposalAttachmentList);
	}

	public void save() {
		try {
			// Validate employee selection
			if (leaveRequest == null || leaveRequest.getEmployee() == null) {
				addErrorMessage("Please select an Employee before saving.");
				return;
			}
			leaveRequest.setStatus("PENDING");
			loadAttachment();
			leaveRequestService.addNewLeaveRequest(leaveRequest);
			moveUploadedFiles();
			addInfoMessage(null, MessageId.INSERT_SUCCESS, leaveRequest.getEmployee().getFullName());
			reset();

		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error saving LeaveRequest:",
					e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
		}
	}

	public void createNewLeaveRequest() {
		reset();
	}

	public void reset() {
		leaveRequest = new LeaveRequest();
		employee = new Employee();
		getProposalAttachmentList().clear();
		leaveRequest.setAttachFiles(new ArrayList<>());
		medicalUploadedFileMap.clear();
		leaveRequests = leaveRequestService.findAllLeaveRequest();
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

	public String getTemporyDir() {
		return temporyDir;
	}

	public void setTemporyDir(String temporyDir) {
		this.temporyDir = temporyDir;
	}

}
