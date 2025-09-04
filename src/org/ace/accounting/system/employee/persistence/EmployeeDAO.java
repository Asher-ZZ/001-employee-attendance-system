package org.ace.accounting.system.employee.persistence;
import java.util.List;

import javax.persistence.PersistenceException;
import org.ace.accounting.system.employee.Employee;
import org.ace.accounting.system.employee.persistence.interfaces.IEmployeeDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



@Repository("EmployeeDAO")
public class EmployeeDAO extends BasicDAO implements IEmployeeDAO {

	@Transactional(propagation = Propagation.REQUIRED)
	public void insert(Employee employee)throws DAOException{
		try {
			em.persist(employee);
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Fail To Insert Employee", pe);
		}	
	}
 	
	@Transactional(propagation = Propagation.REQUIRED)
	public void update (Employee employee)throws DAOException{
		try {
			em.persist(employee);
			em.flush();
		}catch(PersistenceException pe) {
			throw translate("Fail To Update Employee", pe);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete (Employee employee)throws DAOException{
		try {
			em.persist(employee);
			em.flush();
		}catch(PersistenceException pe){
			throw translate("Fail TO Delete Employee", pe);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Employee> findAll() throws DAOException {
	    try {
	        return em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
	    } catch (PersistenceException pe) {
	        throw translate("Failed to retrieve employees", pe);
	    }
	}	
}
