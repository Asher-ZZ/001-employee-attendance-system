package org.ace.accounting.web.dialog;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.system.employee.Employee;
import org.ace.accounting.system.employee.service.interfaces.IEmployeeService;
import org.ace.java.web.common.BaseBean;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "EmployeeDialogActionBean")
@ViewScoped
public class EmployeeDialogActionBean extends BaseBean {

	@ManagedProperty(value = "#{EmployeeService}")
	protected IEmployeeService employeeService;

	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	private List<Employee> employeeList;

	@PostConstruct
	public void init() {
		employeeList = employeeService.findAllEmployee();
	}

	public List<Employee> getEmployeeList(){
		return employeeList;
	}
	public void selectEmployee(Employee employee) {
		PrimeFaces.current().dialog().closeDynamic(employee);
	}
	
}
