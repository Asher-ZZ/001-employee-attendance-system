package org.ace.accounting.web.system;

import org.ace.accounting.system.attendance.Attendance;
import org.ace.accounting.system.attendance.service.interfaces.IAttendanceService;
import org.ace.java.web.common.BaseBean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
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

	// Search filters
	private String searchEmployee;
	private String searchDepartment;
	private Date fromDate;
	private Date toDate;
	private Attendance selectedAttendance;

	private static final int OFFICE_START_HOUR = 9;
	private static final int OFFICE_END_HOUR = 17;

	// Result list
	private List<Attendance> resultList;

	@ManagedProperty(value = "#{AttendanceService}")
	private IAttendanceService attendanceService;

	public void setAttendanceService(IAttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	@PostConstruct
	public void init() {
		resultList = new ArrayList<>();
	}

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

				if (searchDepartment != null && !searchDepartment.trim().isEmpty()) {
					matches &= att.getEmployee() != null && att.getEmployee().getDepartment() != null
							&& att.getEmployee().getDepartment().toLowerCase(searchDepartment)
									.contains(searchDepartment.toLowerCase());
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
		fromDate = null;
		toDate = null;
		resultList = new ArrayList<>();
	}

	// ====== Computed values ======
	public String getTotalHours(Attendance att) {
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

	public String getLateArrival(Attendance att) {
		if (att == null || att.getArrivalTime() == null) {
			return "N/A";
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(att.getArrivalTime());
		int arrivalHour = cal.get(Calendar.HOUR_OF_DAY);
		int arrivalMinute = cal.get(Calendar.MINUTE);
		int lateMinutes = (arrivalHour - OFFICE_START_HOUR) * 60 + arrivalMinute;
		return lateMinutes > 0 ? lateMinutes + " min" : "On time";
	}

	public String getEarlyDeparture(Attendance att) {
		if (att == null || att.getDepartureTime() == null) {
			return "N/A";
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(att.getDepartureTime());
		int departureHour = cal.get(Calendar.HOUR_OF_DAY);
		int departureMinute = cal.get(Calendar.MINUTE);
		int earlyMinutes = (OFFICE_END_HOUR - departureHour) * 60 - departureMinute;
		return earlyMinutes > 0 ? earlyMinutes + " min" : "On time";
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

	// ====== Getters & Setters ======
	public String getSearchEmployee() {
		return searchEmployee;
	}

	public void setSearchEmployee(String searchEmployee) {
		this.searchEmployee = searchEmployee;
	}

	public String getSearchDepartment() {
		return searchDepartment;
	}

	public void setSearchDepartment(String searchDepartment) {
		this.searchDepartment = searchDepartment;
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

}
