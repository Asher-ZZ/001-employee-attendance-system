package org.ace.accounting.common.service;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("EmployeeValidator")
public class EmployeeValidator implements Validator<String> {
	@Override
	public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
		if (value == null || !value.matches("^\\d{9,20}$")) {
			throw new ValidatorException(
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Value must be number with 9-20 digits", null));
		}
	}
}
