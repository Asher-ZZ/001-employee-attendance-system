package org.ace.accounting.web.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.ace.accountig.system.attendance.Attendance;

@ManagedBean(name = "ManageAttendanceActionBean")
@ViewScoped
public class ManageAttendanceActionBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Attendance attendance = new Attendance();
	private List<Attendance> attendanceList = new ArrayList<>();

	public Attendance getAttendance() {
		return attendance;
	}

	public void setAttendance(Attendance attendance) {
		this.attendance = attendance;
	}

	public List<Attendance> getAttendanceList() {
		return attendanceList;
	}

	// Save new or update existing record
	public void save() {
		if (!attendanceList.contains(attendance)) {
			attendanceList.add(attendance);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Saved", "Attendance added successfully"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Updated", "Attendance updated successfully"));
		}
		attendance = new Attendance(); // reset form
	}

	// Edit record
	public void edit(Attendance att) {
		this.attendance = att;
	}

	// Delete record
	public void delete(Attendance att) {
		attendanceList.remove(att);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted", "Attendance deleted"));
	}

	// Clear form
	public void clear() {
		this.attendance = new Attendance();
	}
}