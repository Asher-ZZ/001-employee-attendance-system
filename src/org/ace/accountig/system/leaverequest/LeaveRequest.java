package org.ace.accountig.system.leaverequest;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.TableName;
import org.ace.accounting.system.employee.Employee;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.LEAVEREQUEST)
@TableGenerator(name = "LEAVEREQUEST_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "LEAVEREQUEST_GEN", allocationSize = 10)
@EntityListeners(IDInterceptor.class)
public class LeaveRequest implements Serializable{
	
	private static final long serialVersionUID = -3773190552836366546L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "BRANCH_GEN")
	private String id;

	private String leaveType;
	
	private Date startDate;
	
	private Date endDate;
	
	private String reason;
	
	private String status;
	
	/*
	 * @ManyToOne private Employee employee;
	 */
	
	@Version
	private int version;

	@Embedded
	private BasicEntity basicEntity;


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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
