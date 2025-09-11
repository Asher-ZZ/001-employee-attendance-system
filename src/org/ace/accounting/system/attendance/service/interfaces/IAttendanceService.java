package org.ace.accounting.system.attendance.service.interfaces;

import java.util.List;

import org.ace.accounting.system.attendance.Attendance;
import org.ace.java.component.SystemException;

public interface IAttendanceService {
	void addNewAttendance(Attendance attendance) throws SystemException;

	void updateAttendance(Attendance attendance) throws SystemException;

	/* void deleteAttendance(Attendance attendance) throws SystemException; */

	List<Attendance> findAllAttendance() throws SystemException;

		List<Attendance> findAll();

}