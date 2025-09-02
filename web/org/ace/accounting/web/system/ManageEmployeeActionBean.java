package org.ace.accounting.web.system;

import org.ace.accounting.system.employee.Employee;
import org.ace.accounting.system.employee.service.EmployeeService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "ManageEmployeeActionBean")
@ViewScoped
public class ManageEmployeeActionBean implements Serializable {

	private Employee employee;
	private List<Employee> employees;

	@ManagedProperty(value = "#{EmployeeService}")
	private EmployeeService employeeService;

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@PostConstruct
	public void init() {
		employee = new Employee();
		employees = employeeService.findAll();
	}

	public Employee getEmployee() {
		return employee;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void save() {
		employeeService.save(employee);
		employees = employeeService.findAll(); // refresh list
		reset();
	}

	public void reset() {
		employee = new Employee();
	}

	public void edit(Employee emp) {
		this.employee = emp;
	}

	public void delete(Employee emp) {
		employeeService.delete(emp);
		employees = employeeService.findAll(); // refresh list
	}
}
