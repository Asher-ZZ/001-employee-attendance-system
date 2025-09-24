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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ATTACHFILE_GEN")
	private String id;
	
	private String name;
	private String filePath;
	
	@Version
	private int version;

	@Embedded
	private BasicEntity basicEntity;

	public AttachFile(String fileNameString, String filePath2) {
		this.name = fileNameString;
		this.filePath = filePath2;
	}
	
	

	public AttachFile() {
		super();
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	

	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFileData() {
		// TODO Auto-generated method stub
		return null;
	}
}
