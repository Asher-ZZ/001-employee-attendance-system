package org.ace.accounting.web.system;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.system.attendance.Attendance;
import org.ace.accounting.system.attendance.service.interfaces.IAttendanceService;
import org.ace.accounting.system.employee.Employee;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "ManageAttendanceActionBean")
@ViewScoped
public class ManageAttendanceActionBean extends BaseBean {

	private Attendance attendance;
	private List<Attendance> attendanceList;
	private Employee employee;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@ManagedProperty(value = "#{AttendanceService}")
	private IAttendanceService attendanceService;

	public void setAttendanceService(IAttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	@PostConstruct
	public void init() {
		attendance = new Attendance();
		try {
			attendanceList = attendanceService.findAllAttendance();
		} catch (SystemException e) {
			attendanceList = new ArrayList<>();
			addErrorMessage("Failed to load attendance list: " + e.getMessage());
		}
	}

	public void save() {
		try {
			if (attendance.getId() == null) {
				attendanceService.addNewAttendance(attendance);
				addInfoMessage("Attendance added successfully");
			} else {
				attendanceService.updateAttendance(attendance);
				addInfoMessage("Attendance updated successfully");
			}
			attendanceList = attendanceService.findAllAttendance();
			reset();
		} catch (SystemException e) {
			addErrorMessage("Error saving attendance: " + e.getMessage());
		}
	}

	public void prepareUpdateAttendance(Attendance att) {
		this.attendance = att;
	}

	public void reset() {
		attendance = new Attendance();
	}

	/** Called when employee is selected from dialog */
	public void returnEmployee(SelectEvent event) {
		Employee employee = (Employee) event.getObject();
		attendance.setEmployee(employee);
	}

	// Getters and setters
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
}
