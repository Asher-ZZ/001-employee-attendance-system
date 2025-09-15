package org.ace.accounting.system.attendance.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.system.attendance.Attendance;
import org.ace.accounting.system.attendance.persistence.interfaces.IAttendanceDAO;
import org.ace.accounting.system.attendance.service.interfaces.IAttendanceService;
import org.ace.accounting.system.employee.Employee;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "AttendanceService")
public class AttendanceService implements IAttendanceService {

	@Resource(name = "AttendanceDAO")
	private IAttendanceDAO attendanceDAO;

	@Transactional(propagation = Propagation.REQUIRED)
	public void addNewAttendance(Attendance attendance) throws SystemException {
		try {
			attendanceDAO.insert(attendance);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed To Add New Attendance!", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateAttendance(Attendance attendance) throws SystemException {
		try {
			attendanceDAO.update(attendance);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed To Update Attendance!", e);
		}
	}

	/*
	 * @Transactional(propagation = Propagation.REQUIRED) public void
	 * deleteAttendance(Attendance attendance) throws SystemException { try {
	 * attendanceDAO.delete(attendance); } catch (DAOException e) { throw new
	 * SystemException(e.getErrorCode(), "Failed To Delete Attendance!", e); } }
	 */

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Attendance> findAllAttendance() throws SystemException {
		try {
			return attendanceDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all Attendance", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Attendance> findAll() throws SystemException {
		try {
			return attendanceDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all Attendance", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean existsByEmployeeAndDate(Employee employee, Date date, String excludeId) {
		List<Attendance> list = findAttendanceByEmployeeAndDate(employee.getId(), date);
		return list.stream().anyMatch(a -> excludeId == null || !a.getId().equals(excludeId));
	}

	private List<Attendance> findAttendanceByEmployeeAndDate(String empId, Date date) {
		try {
			return attendanceDAO.findByEmployeeAndDate(empId, date);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find Attendance by Employee and Date", e);
		}
	}

}