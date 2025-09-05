package org.ace.accounting.web.system;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accountig.system.attendance.Attendance;
import org.ace.accountig.system.attendance.service.interfaces.IAttendanceService;
import org.ace.accounting.common.validation.MessageId;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;

@ManagedBean(name = "ManageAttendanceActionBean")
@ViewScoped
public class ManageAttendanceActionBean extends BaseBean implements Serializable {

	@ManagedProperty(value = "#{AttendanceService}")
	private IAttendanceService attendanceService;

	private boolean createNew;
	private Attendance attendance;
	private List<Attendance> attendanceList;

	@PostConstruct
	public void init() {
		createNewAttendance();
		rebindData();
	}

	public void createNewAttendance() {
		createNew = true;
		attendance = new Attendance();
	}

	public void rebindData() {
		if (attendanceService != null) {
			attendanceList = attendanceService.findAllAttendance();
		}
	}

	public void prepareUpdateAttendance(Attendance attendance) {
		createNew = false;
		this.attendance = attendance;
	}

	public void save() {
		try {
			if (createNew) {
				attendanceService.addNewAttendance(attendance);
				addInfoMessage(null, MessageId.INSERT_SUCCESS, "Attendance");
			} else {
				attendanceService.updateAttendance(attendance);
				addInfoMessage(null, MessageId.UPDATE_SUCCESS, "Attendance");
			}
			createNewAttendance();
			rebindData();
		} catch (SystemException ex) {
			handleSysException(ex);
		}
	}

	/*
	 * public void delete(Attendance attendance) { try {
	 * attendanceService.deleteAttendance(attendance); addInfoMessage(null,
	 * MessageId.DELETE_SUCCESS, "Attendance"); rebindData(); } catch
	 * (SystemException ex) { handleSysException(ex); } }
	 */

	public void reset() {
		createNewAttendance();
	}

	// Getters and Setters
	public IAttendanceService getAttendanceService() {
		return attendanceService;
	}

	public void setAttendanceService(IAttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	public Attendance getAttendance() {
		return attendance;
	}

	public void setAttendance(Attendance attendance) {
		this.attendance = attendance;
	}

	public List<Attendance> getAttendanceList() {
		return attendanceList;
	}

	public void setAttendanceList(List<Attendance> attendanceList) {
		this.attendanceList = attendanceList;
	}

	public boolean isCreateNew() {
		return createNew;
	}

	public void setCreateNew(boolean createNew) {
		this.createNew = createNew;
	}
}