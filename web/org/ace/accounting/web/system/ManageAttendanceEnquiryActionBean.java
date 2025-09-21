package org.ace.accounting.web.system;

import org.ace.accounting.system.attendance.Attendance;
import org.ace.accounting.system.attendance.service.interfaces.IAttendanceService;
import org.ace.accounting.system.employee.Employee;
import org.ace.accounting.system.employeeattendenceenum.Department;
import org.ace.accounting.system.leaverequest.ExcelExport;
import org.ace.java.web.common.BaseBean;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean(name = "ManageAttendanceEnquiryActionBean")
@ViewScoped
public class ManageAttendanceEnquiryActionBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Employee employee;
	// Search filters
	private String searchEmployee;
	private Department searchDepartment;
	private Date fromDate;
	private Date toDate;
	private Attendance selectedAttendance;

	private static final int OFFICE_START_HOUR = 9;
	private static final int OFFICE_END_HOUR = 17;

	// Result list
	private List<Attendance> resultList;

	public Department[] getDepartmentValues() {
		return Department.values();
	}

	@ManagedProperty(value = "#{AttendanceService}")
	private IAttendanceService attendanceService;

	public void setAttendanceService(IAttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	@PostConstruct
	public void init() {
		resultList = new ArrayList<>();
		toDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate);
		cal.add(Calendar.DAY_OF_MONTH, -7);
		fromDate = cal.getTime();
	}

	// ====== Search ======
	// ====== Search ======
	public void search() {
		try {
			List<Attendance> allRecords = attendanceService.findAll();

			resultList = allRecords.stream().filter(att -> {
				boolean matches = true;

				if (searchEmployee != null && !searchEmployee.trim().isEmpty()) {
					matches &= att.getEmployee() != null && att.getEmployee().getFullName() != null
							&& att.getEmployee().getFullName().toLowerCase().contains(searchEmployee.toLowerCase());
				}

				if (searchDepartment != null) {
					matches &= att.getEmployee() != null && att.getEmployee().getDepartment() != null
							&& att.getEmployee().getDepartment().equals(searchDepartment);
				}

				if (fromDate != null) {
					matches &= att.getDate() != null && !att.getDate().before(fromDate);
				}

				if (toDate != null) {
					matches &= att.getDate() != null && !att.getDate().after(toDate);
				}

				return matches;
			}).collect(Collectors.toList());

			addInfoMessage("Search Completed", resultList.size() + " record(s) found.");
		} catch (Exception e) {
			addErrorMessage("Search Error", e.getMessage());
			e.printStackTrace();
		}
	}

	public void reset() {
		searchEmployee = null;
		searchDepartment = null;
		resultList = new ArrayList<>();
	}

	// ====== Computed values ======
	public static String getTotalHours(Attendance att) {
		if (att == null) {
			return "0h 0m";
		}
		if (att.getArrivalTime() != null && att.getDepartureTime() != null) {
			long diff = att.getDepartureTime().getTime() - att.getArrivalTime().getTime();
			long hours = diff / (1000 * 60 * 60);
			long minutes = (diff / (1000 * 60)) % 60;
			return hours + "h " + minutes + "m";
		}
		return "0h 0m";
	}

	public static String getLateArrival(Attendance att) {
		if (att == null || att.getArrivalTime() == null) {
			return "N/A";
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(att.getArrivalTime());
		int arrivalHour = cal.get(Calendar.HOUR_OF_DAY);
		int arrivalMinute = cal.get(Calendar.MINUTE);
		int lateMinutes = (arrivalHour - OFFICE_START_HOUR) * 60 + arrivalMinute;
		return lateMinutes > 0 ? lateMinutes + " min" : "On Time";
	}

	public static String getEarlyDeparture(Attendance att) {
		if (att == null || att.getDepartureTime() == null) {
			return "N/A";
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(att.getDepartureTime());
		int departureHour = cal.get(Calendar.HOUR_OF_DAY);
		int departureMinute = cal.get(Calendar.MINUTE);
		int earlyMinutes = (OFFICE_END_HOUR - departureHour) * 60 - departureMinute;
		return earlyMinutes > 0 ? earlyMinutes + " min" : "On Time";
	}

	// ====== Row highlight helpers ======
	public boolean isLateArrival(Attendance att) {
		if (att.getArrivalTime() == null)
			return false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(att.getArrivalTime());
		int minutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
		return minutes > OFFICE_START_HOUR * 60;
	}

	public boolean isEarlyDeparture(Attendance att) {
		if (att.getDepartureTime() == null)
			return false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(att.getDepartureTime());
		int minutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
		return minutes < OFFICE_END_HOUR * 60;
	}

	public void exportExcel() {
		try {
			ExcelExport.exportToExcel(resultList); // filePath မလိုတော့ပါ
		} catch (IOException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Export Failed", e.getMessage()));
		}
	}

	// ====== Getters & Setters ======
	public Department getSearchDepartment() {
		return searchDepartment;
	}

	public void setSearchDepartment(Department searchDepartment) {
		this.searchDepartment = searchDepartment;
	}

	public String getSearchEmployee() {
		return searchEmployee;
	}

	public void setSearchEmployee(String searchEmployee) {
		this.searchEmployee = searchEmployee;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public List<Attendance> getResultList() {
		return resultList;
	}

	public void setResultList(List<Attendance> resultList) {
		this.resultList = resultList;
	}

	public void selectAttendance(Attendance att) {
		this.selectedAttendance = att;
	}

	public Attendance getSelectedAttendance() {
		return selectedAttendance;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
