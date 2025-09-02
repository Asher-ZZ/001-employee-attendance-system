package org.ace.accounting.system.employee.service;

import org.ace.accounting.system.employee.Employee;
import org.ace.accounting.system.employee.persistence.EmployeeDAO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class EmployeeService {

    @Inject
    private EmployeeDAO employeeDAO;

    public void save(Employee employee) {
        employeeDAO.save(employee);
    }

    public void delete(Employee employee) {
        employeeDAO.delete(employee);
    }

    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }
}
