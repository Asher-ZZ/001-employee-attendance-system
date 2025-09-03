package org.ace.accountig.system.attendance.service.interfaces;

import java.util.List;

import org.ace.accountig.system.attendance.Attendance;
import org.ace.java.component.SystemException;

public interface IAttendanceService {

	public List<Attendance> findAllBranch() throws SystemException;

	public void addNewBranch(Attendance attendance) throws SystemException;

	public void updateBranch(Attendance attendance) throws SystemException;

	public void deleteBranch(Attendance attendance) throws SystemException;

}
