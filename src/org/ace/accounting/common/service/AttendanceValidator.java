package org.ace.accounting.common.service;

import java.time.LocalTime;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.ace.accounting.system.attendance.Attendance;
import org.ace.accounting.web.system.ManageAttendanceActionBean;

@FacesValidator("attendanceValidator")
public class AttendanceValidator implements Validator<Object> {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		// Backing Bean ကို JSF Context ကနေ ယူမယ်
		ManageAttendanceActionBean bean = context.getApplication().evaluateExpressionGet(context,
				"#{ManageAttendanceActionBean}", ManageAttendanceActionBean.class);

		Attendance att = bean.getAttendance();

		// 1. Employee Required
		if (att.getEmployee() == null) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Employee is required.", null));
		}

		// 2. Date Required
		if (att.getDate() == null) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date is required.", null));
		}

		// 3. ArrivalTime Required
		if (att.getArrivalTime() == null) {
			throw new ValidatorException(
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Arrival Time is required.", null));
		}

		// 4. DepartureTime must be greater than ArrivalTime
		if (att.getArrivalTime() != null && att.getDepartureTime() != null) {
			LocalTime arrival = new java.sql.Time(att.getArrivalTime().getTime()).toLocalTime();
			LocalTime departure = new java.sql.Time(att.getDepartureTime().getTime()).toLocalTime();

			if (!departure.isAfter(arrival)) {
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Departure Time must be later than Arrival Time.", null));
			}
		}

		// 5. Remarks Length
		if (att.getRemarks() != null && att.getRemarks().length() > 255) {
			throw new ValidatorException(
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Remarks cannot exceed 255 characters.", null));
		}
	}
}
