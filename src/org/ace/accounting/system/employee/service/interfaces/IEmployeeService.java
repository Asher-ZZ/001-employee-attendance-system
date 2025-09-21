package org.ace.accounting.system.employee.service.interfaces;

import java.util.List;

import org.ace.accounting.system.employee.Employee;
import org.ace.java.component.SystemException;

public interface IEmployeeService {

	public List<Employee> findAllEmployee() throws SystemException;

	public void addNewEmployee(Employee employee) throws SystemException;

	public void updateEmployee(Employee employee) throws SystemException;

	public void deleteEmployee(Employee employee) throws SystemException;
}