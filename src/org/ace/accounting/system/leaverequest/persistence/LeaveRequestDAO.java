package org.ace.accounting.system.leaverequest.persistence;

import java.util.List;
import javax.persistence.PersistenceException;

import org.ace.accounting.system.leaverequest.LeaveRequest;
import org.ace.accounting.system.leaverequest.persistence.interfaces.ILeaveRequestDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("LeaveRequestDAO")
public class LeaveRequestDAO extends BasicDAO implements ILeaveRequestDAO {

	@Transactional(propagation = Propagation.REQUIRED)
	public void insert(LeaveRequest leaveRequest) throws DAOException {
		try {
			em.persist(leaveRequest);
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Fail To Insert LeaveRequest", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void update(LeaveRequest leaveRequest) throws DAOException {
		try {
			em.merge(leaveRequest);
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Fail To Update LeaveRequest", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(LeaveRequest leaveRequest) throws DAOException {
		try {
			LeaveRequest merged = em.merge(leaveRequest);
			em.remove(merged);
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Fail TO Delete LeaveRequest", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<LeaveRequest> findAll() throws DAOException {
		try {
			return em.createQuery("SELECT l FROM LeaveRequest l", LeaveRequest.class).getResultList();
		} catch (PersistenceException pe) {
			throw translate("Failed to retrieve LeaveRequests", pe);
		}
	}
}
