package org.ace.accounting.system.attendance.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.ace.accounting.system.attendance.Attendance;
import org.ace.accounting.system.attendance.persistence.interfaces.IAttendanceDAO;
import org.ace.accounting.system.attendance.service.interfaces.IAttendanceService;
import org.ace.accounting.system.employee.Employee;
import org.ace.accounting.system.employeeattendenceenum.Department;
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
        if (employee == null || employee.getId() == null) {
            return false;
        }

        List<Attendance> list = attendanceDAO.findByEmployeeAndDate(
                employee.getId(),
                date,
                employee.getDepartment()
        );

        if (list == null || list.isEmpty()) {
            return false;
        }

        // excludeId မပါလျှင် record တစ်ခုခုရှိတာနဲ့ true
        if (excludeId == null) {
            return !list.isEmpty();
        }

        // excludeId ပါလျှင် excludeId မတူတဲ့ record ရှိ/မရှိ စစ်
        return list.stream().anyMatch(a -> !Objects.equals(a.getId(), excludeId));
    }
}
