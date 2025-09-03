package org.ace.accounting.system.employee.service;

import java.util.List;

import javax.annotation.Resource;
import org.ace.accounting.system.employee.Employee;
import org.ace.accounting.system.employee.persistence.interfaces.IEmployeeDAO;
import org.ace.accounting.system.employee.service.interfaces.IEmployeeService;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "EmployeeService")
public class EmployeeService implements IEmployeeService {

	@Resource(name = "EmployeeDAO")
	private IEmployeeDAO employeeDAO;
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void addNewEmployee (Employee employee) throws DAOException{
		try {
			employeeDAO.insert(employee);
		}catch(DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed To Add New Employee!",e);
		}
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateEmployee(Employee employee)throws DAOException{
		try {
			employeeDAO.update(employee);
		}catch(DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed TO Update Employee!",e);
		}
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteEmployee(Employee employee)throws DAOException{
		try {
			employeeDAO.delete(employee);
		}catch(DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed TO Delete Employee!",e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Employee> findAllEmployee() throws SystemException {
		List<Employee> result = null;
		try {
			result = employeeDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of Branch)", e);
		}
		return result;
	}

	
}
