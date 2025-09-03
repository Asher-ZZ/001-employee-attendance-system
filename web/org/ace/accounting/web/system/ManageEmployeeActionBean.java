package org.ace.accounting.web.system;

import org.ace.accounting.system.employee.Employee;
import org.ace.accounting.system.employee.service.interfaces.IEmployeeService;
import org.ace.accounting.system.employeeattendenceenum.Department;
import org.ace.accounting.system.employeeattendenceenum.Position;
import org.ace.java.component.SystemException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "ManageEmployeeActionBean")
@ViewScoped
public class ManageEmployeeActionBean implements Serializable {

	private Employee employee;
	private List<Employee> employees;

	@ManagedProperty(value = "#{EmployeeService}")
	private IEmployeeService employeeService;

	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@PostConstruct
	public void init() {
		employee = new Employee();
		try {
			employees = employeeService.findAllEmployee(); // Load from DB
		} catch (SystemException e) {
			employees = new ArrayList<>();
			addErrorMessage("Failed to load employees: " + e.getMessage());
		}
	}

	public Department[] getDepartmentValues() {
		return Department.values();
	}

	public Position[] getPositionValues() {
		return Position.values();
	}

	// --- CRUD Operations ---
	public void save() {
		try {
			if (employee.getId() == null) {
				employeeService.addNewEmployee(employee); // Insert
				// Create a new instance to add to the list to avoid reference issues
				Employee newEmployee = new Employee();
				// Copy properties if needed, or refresh the list from DB
				employees = employeeService.findAllEmployee(); // Reload from DB
				addInfoMessage("Employee added successfully: " + employee.getFullName());
			} else {
				employeeService.updateEmployee(employee); // Update
				// Refresh the list from DB to ensure consistency
				employees = employeeService.findAllEmployee();
				addInfoMessage("Employee updated successfully: " + employee.getFullName());
			}
			reset(); // reset form
		} catch (SystemException ex) {
			addErrorMessage("Error saving employee: " + ex.getMessage());
		}
	}

	public void prepareUpdateEmployee(Employee emp) {
		this.employee = emp;
	}

	public void deleteEmployee(Employee emp) {
		try {
			employeeService.deleteEmployee(emp);
			employees.remove(emp);
			addInfoMessage("Employee deleted successfully: " + emp.getFullName());
			reset();
		} catch (SystemException ex) {
			addErrorMessage("Error deleting employee: " + ex.getMessage());
		}
	}

	public void createNewEmployee() {
		reset();
	}

	public void reset() {
		employee = new Employee();
	}

	// --- Messaging Helpers ---
	private void addErrorMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
	}

	private void addInfoMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", message));
	}

	// --- Getters & Setters ---
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
}
