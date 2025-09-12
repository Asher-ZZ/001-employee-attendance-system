package org.ace.accounting.common.service;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("EmailValidator")
public class EmailValidator implements Validator<String> {

	private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

	@Override
	public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
		if (value == null || !value.matches(EMAIL_PATTERN)) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email format,eg-example@gmail.com", null));
		}
	}
}
