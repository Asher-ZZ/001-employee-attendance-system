package org.ace.java.web.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/employee-attendance/image/*")
public class ImageServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_DIR = "D:/Attachments/temp/";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String requestedFile = req.getPathInfo().substring(1); // removes the leading '/'
		File file = new File(UPLOAD_DIR, requestedFile);

		if (!file.exists()) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// Set content type
		String mimeType = getServletContext().getMimeType(file.getName());
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		resp.setContentType(mimeType);
		resp.setContentLengthLong(file.length());

		Files.copy(file.toPath(), resp.getOutputStream());

	}
}
