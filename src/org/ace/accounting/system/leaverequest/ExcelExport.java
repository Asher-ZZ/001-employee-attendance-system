package org.ace.accounting.system.leaverequest;

import java.io.IOException;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.ace.accounting.system.attendance.Attendance;
import org.ace.accounting.web.system.ManageAttendanceEnquiryActionBean;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;

public class ExcelExport {

	public static void exportToExcel(List<Attendance> attList) throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Attendance Data");

			// Header row
			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("Employee Name");
			headerRow.createCell(1).setCellValue("Department");
			headerRow.createCell(2).setCellValue("Date");
			headerRow.createCell(3).setCellValue("Arrival Time");
			headerRow.createCell(4).setCellValue("Departure Time");
			headerRow.createCell(5).setCellValue("Total Hours");
			headerRow.createCell(6).setCellValue("Late Arrival");
			headerRow.createCell(7).setCellValue("Early Departure");
			headerRow.createCell(8).setCellValue("Remarks");

			// Data rows
			int rowNum = 1;
			for (Attendance att : attList) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(att.getEmployee().getFullName());

				// Department ကို String အနေနဲ့ထည့်
				row.createCell(1).setCellValue(
						att.getEmployee().getDepartment() != null ? att.getEmployee().getDepartment().toString() : "");

				row.createCell(2).setCellValue(att.getDate() != null ? att.getDate().toString() : "");
				row.createCell(3).setCellValue(att.getArrivalTime() != null ? att.getArrivalTime().toString() : "");
				row.createCell(4).setCellValue(att.getDepartureTime() != null ? att.getDepartureTime().toString() : "");
				row.createCell(5).setCellValue(ManageAttendanceEnquiryActionBean.getTotalHours(att));
				row.createCell(6).setCellValue(ManageAttendanceEnquiryActionBean.getLateArrival(att));
				row.createCell(7).setCellValue(ManageAttendanceEnquiryActionBean.getEarlyDeparture(att));
				row.createCell(8).setCellValue(att.getRemarks());

				row.createCell(8).setCellValue(att.getRemarks() != null ? att.getRemarks() : "");
			}

			// Auto size columns
			for (int i = 0; i <= 8; i++) {
				sheet.autoSizeColumn(i);
			}

			// HTTP response setup
			response.reset();
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=\"attendance_report.xlsx\"");

			try (OutputStream out = response.getOutputStream()) {
				workbook.write(out);
				out.flush();
			}

			facesContext.responseComplete();
		}
	}
}
