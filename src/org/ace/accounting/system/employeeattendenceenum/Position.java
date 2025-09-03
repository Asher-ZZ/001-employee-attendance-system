package org.ace.accounting.system.employeeattendenceenum;

public enum Position {
	
	INTERN("INTERN"),
	JUNIOT("JUNIOR STAFF"),
	SENIOR("ENIOT STAFF"),
	LEADER("TEAM LEADER"),
	MANAGER("MANAGER"),
	SENIOR_MANAGER("SENIOR MANAGER");
	
	private final String position;
	
	Position(String position){
		this.position=position;
	}
	public String getposition() {
		return position;
	}
	
	/*
	 * INTERN("Intern"), JUNIOR("Junior Staff"), SENIOR("Senior Staff"),
	 * LEAD("Team Lead"), MANAGER("Manager"), SENIOR_MANAGER("Senior Manager"),
	 * DIRECTOR("Director"), VICE_PRESIDENT("Vice President"),
	 * PRESIDENT("President"), CEO("Chief Executive Officer"),
	 * CTO("Chief Technology Officer"), CFO("Chief Financial Officer"),
	 * COO("Chief Operating Officer");
	 * 
	 * private final String label;
	 * 
	 * Position(String label) { this.label = label; }
	 * 
	 * public String getLabel() { return label; }
	 */
}
