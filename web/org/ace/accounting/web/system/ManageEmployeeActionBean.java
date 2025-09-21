package org.ace.accounting.web.system;

import org.ace.accounting.system.employee.Employee;
import org.ace.accounting.system.employee.service.interfaces.IEmployeeService;
import org.ace.accounting.system.employeeattendenceenum.Department;
import org.ace.accounting.system.employeeattendenceenum.Position;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "ManageEmployeeActionBean")
@ViewScoped
public class ManageEmployeeActionBean extends BaseBean {

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
			employees = employeeService.findAllEmployee();
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
				employeeService.addNewEmployee(employee);
				Employee newEmployee = new Employee();
				employees = employeeService.findAllEmployee();
				addInfoMessage("Employee added successfully: " + employee.getFullName());
			} else {
				employeeService.updateEmployee(employee);
				employees = employeeService.findAllEmployee();
				addInfoMessage("Employee updated successfully: " + employee.getFullName());
			}
			reset();
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

	public Date getMinDateOfBirth() {
		Calendar cal = Calendar.getInstance();
		cal.set(1950, Calendar.JANUARY, 1);
		return cal.getTime();
	}

	// Maximum date of birth = Today - 16 years (age ≥16)
	public Date getMaxDateOfBirth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -16); // 16+ only
		return cal.getTime();
	}

	// Maximum year for dropdown
	public int getMaxDateOfBirthYear() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -16);
		return cal.get(Calendar.YEAR);
	}

	// Age validator (server-side)
	public void validateAge16(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value != null && value instanceof Date) {
			Date dob = (Date) value;
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -16);
			Date maxDate = cal.getTime();

			if (dob.after(maxDate)) {
				throw new ValidatorException(
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Employee must be at least 16 years old.", null));
			}
		}
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
