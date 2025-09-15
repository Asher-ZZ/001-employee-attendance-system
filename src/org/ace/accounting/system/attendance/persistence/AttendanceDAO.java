package org.ace.accounting.system.attendance.persistence;

import java.util.Date;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.ace.accounting.system.attendance.Attendance;
import org.ace.accounting.system.attendance.persistence.interfaces.IAttendanceDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("AttendanceDAO")
public class AttendanceDAO extends BasicDAO implements IAttendanceDAO {

	@Transactional(propagation = Propagation.REQUIRED)
	public void insert(Attendance attendance) throws DAOException {
		try {
			em.persist(attendance);
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Fail To Insert Attendance", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void update(Attendance attendance) throws DAOException {
		try {
			em.merge(attendance);
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Fail To Update Attendance", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Attendance attendance) throws DAOException {
		try {
			Attendance att = em.merge(attendance);
			em.remove(att);
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Fail To Delete Attendance", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<Attendance> findAll() throws DAOException {
		try {
			return em.createQuery("SELECT a FROM Attendance a", Attendance.class).getResultList();
		} catch (PersistenceException pe) {
			throw translate("Failed to retrieve Attendance", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Attendance> findByEmployeeAndDate(String empId, Date date) throws DAOException {
		try {
			TypedQuery<Attendance> q = em.createQuery(
					"SELECT a FROM Attendance a WHERE a.employee.id = :empId AND a.date = :date", Attendance.class);
			q.setParameter("empId", empId);
			q.setParameter("date", date);
			return q.getResultList();
		} catch (PersistenceException pe) {
			throw translate("Failed to find Attendance by Employee and Date", pe);
		}
	}

}
