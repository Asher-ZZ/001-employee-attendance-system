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

	// ========== Search ==========
	public void search() {
		try {
			// load all records (you can optimize by writing a DAO query instead)
			List<Attendance> allRecords = attendanceService.findAll();

			resultList = allRecords.stream().filter(att -> {
				boolean matches = true;

				if (searchEmployee != null && !searchEmployee.trim().isEmpty()) {
					matches &= att.getEmployee() != null
							&& att.getEmployee().getFullName().toLowerCase().contains(searchEmployee.toLowerCase());
				}

				if (searchDepartment != null && !searchDepartment.trim().isEmpty()) {
					matches &= att.getEmployee() != null && att.getEmployee().getDepartment() != null
							&& att.getEmployee().getDepartment().toLowerCase(searchDepartment).contains(searchDepartment.toLowerCase());
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

	// ========== Getters & Setters ==========
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
}
