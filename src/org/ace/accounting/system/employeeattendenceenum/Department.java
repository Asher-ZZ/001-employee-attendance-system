package org.ace.accounting.system.employeeattendenceenum;

public enum Department {

	HR("HUMAN RESOURCES"), IT("INFORMATION TECHNOLOGY"), ADMIN("ADMINISTRATION"), PRODUCTION("PRODUCTION"),
	FINANCE("FINANCE"), MANAGEMENT("MANAGEMENT");

	private final String department;

	Department(String department) {
		this.department = department;
	}

	public String getdepartment() {
		return department;
	}

	public String toLowerCase(String input) {
		if (input == null) {
			return null;
		}
		char[] chars = input.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] >= 'A' && chars[i] <= 'Z') {
				chars[i] = (char) (chars[i] + ('a' - 'A'));
			}
		}
		return new String(chars);
	}

}
