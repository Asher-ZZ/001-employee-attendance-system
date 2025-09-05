package org.ace.accountig.system.attendance.persistence.interfaces;

import java.util.List;

import org.ace.accountig.system.attendance.Attendance;
import org.ace.java.component.persistence.exception.DAOException;

public interface IAttendanceDAO {

	public void insert (Attendance attendance)throws DAOException;
	
	public void update (Attendance attendance)throws DAOException;
	
	public void delete (Attendance attendance)throws DAOException;
	
	public List<Attendance> findAll() throws DAOException;
}
