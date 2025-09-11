package org.ace.accounting.system.employee;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.TableName;
import org.ace.accounting.system.attendance.Attendance;
import org.ace.accounting.system.employeeattendenceenum.Department;
import org.ace.accounting.system.employeeattendenceenum.Position;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.EMPLOYEE)
@TableGenerator(name = "EMPLOYEE_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "EMPLOYEE_GEN", allocationSize = 10)
@EntityListeners(IDInterceptor.class)
public class Employee implements Serializable {

	private static final long serialVersionUID = -3773190552836366546L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "EMPLOYEE_GEN")
	private String id;

	private String fullName;
	private Date hireDate;
	private String status;
	private Date dateOfBirth;
	private String gender;

	@OneToMany(mappedBy = "employee")
	private List<Attendance> attendance;

	@Version
	private int version;

	@Embedded
	private BasicEntity basicEntity;

	@Enumerated(EnumType.STRING)
	private Department department;
	
	@Enumerated(EnumType.STRING)
	private Position position;

	// Getters and setters...
	public List<Attendance> getAttendance() {
		return attendance;
	}

	public void setAttendance(List<Attendance> attendance) {
		this.attendance = attendance;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public BasicEntity getBasicEntity() {
		return basicEntity;
	}

	public void setBasicEntity(BasicEntity basicEntity) {
		this.basicEntity = basicEntity;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
}
