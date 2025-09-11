package org.ace.accounting.system.employee.persistence.interfaces;

import java.util.List;

import org.ace.accounting.system.employee.Employee;
import org.ace.java.component.persistence.exception.DAOException;

public interface IEmployeeDAO {

	public void insert (Employee employee) throws DAOException;
	
	public void update (Employee employee) throws DAOException;
	
	public void delete (Employee employee) throws DAOException;

	public List<Employee> findAll() throws DAOException;
	
}
