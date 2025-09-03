package org.ace.accounting.system.employeeattendenceenum;

public enum Department {
	
	HR("HUMAN RESOURCES"),
	IT("INFORMATION TECHNOLOGY"),
	ADMIN("ADMINISTRATION"),
	PRODUCTION("PRODUCTION"),
	FINANCE("FINANCE"),
	MANAGEMENT("MANAGEMENT");
	
	private final String department;
	
	Department(String department){
		this.department=department;
	}
	public String getdepartment() {
		return department;
	}

}

