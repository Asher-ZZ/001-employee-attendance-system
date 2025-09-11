package org.ace.accounting.system.attendance.persistence;

import java.util.List;

import javax.persistence.PersistenceException;

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
}
