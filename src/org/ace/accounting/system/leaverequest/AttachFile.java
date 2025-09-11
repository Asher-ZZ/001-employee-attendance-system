package org.ace.accounting.system.leaverequest;

import javax.persistence.*;
import java.io.Serializable;
import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.TableName;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.ATTACHFILE)
@TableGenerator(name = "ATTACHFILE_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "ATTACHFILE_GEN", allocationSize = 10)
@EntityListeners(IDInterceptor.class)
public class AttachFile implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ATTACHFILE_GEN")
	private String id;

	@Column(name = "MEDICALRECORD", length = 500)
	private String medicalRecord; // filesystem path

	@Version
	private int version;

	@Embedded
	private BasicEntity basicEntity;

	@ManyToOne
	@JoinColumn(name = "LEAVEREQUESTID")
	private LeaveRequest leaveRequest;

	public AttachFile() {
	}

	public AttachFile(String medicalRecord, LeaveRequest leaveRequest) {
		this.medicalRecord = medicalRecord;
		this.leaveRequest = leaveRequest;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMedicalRecord() {
		return medicalRecord;
	}

	public void setMedicalRecord(String medicalRecord) {
		this.medicalRecord = medicalRecord;
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

	public LeaveRequest getLeaveRequest() {
		return leaveRequest;
	}

	public void setLeaveRequest(LeaveRequest leaveRequest) {
		this.leaveRequest = leaveRequest;
	}

	// convenient getter for file path
	public String getFilePath() {
		return medicalRecord;
	}
}
