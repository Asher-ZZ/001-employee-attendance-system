package org.ace.java.web.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Key;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.leaverequest.UploadFileConfig;
import org.ace.java.component.SystemException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class BaseBean {

	private static final String THEME = "theme";
	private static final String DASHBOARD_URL = "http://localhost:8080/fnilp/view/dashboard.xhtml";
	private Properties filePathConfig;

	
	protected static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	protected ServletContext getServletContext() {
		return (ServletContext) getFacesContext().getExternalContext().getContext();
	}

	protected static Application getApplication() {
		return getFacesContext().getApplication();
	}

	protected Object getSpringBean(String beanName) {
		return WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean(beanName);
	}

	protected Map getApplicationMap() {
		return getFacesContext().getExternalContext().getApplicationMap();
	}

	protected Map<String, Object> getSessionMap() {
		return getFacesContext().getExternalContext().getSessionMap();
	}

	protected void putParam(String key, Object obj) {
		getSessionMap().put(key, obj);
	}

	protected Object getParam(String key) {
		return getSessionMap().get(key);
	}

	protected boolean isExistParam(String key) {
		return getSessionMap().containsKey(key);
	}

	protected void removeParam(String key) {
		if (isExistParam(key)) {
			getSessionMap().remove(key);
		}
	}

	protected static ResourceBundle getBundle() {
		return ResourceBundle.getBundle(getApplication().getMessageBundle(), getFacesContext().getViewRoot().getLocale());
	}

	protected String getWebRootPath() {
		Object context = getFacesContext().getExternalContext().getContext();
		String systemPath = ((ServletContext) context).getRealPath("/");
		return systemPath;
	}

	protected String getFullTempDirPath() {
		return getWebRootPath() + Constants.TEMP_DIR;
	}

	protected String getFullUploadDirPath() {
		return getWebRootPath() + Constants.UPLOAD_DIR;
	}

	protected String getFullReportDirPath() {
		return getWebRootPath() + Constants.REPORT_DIR;
	}

	/* File Path to create Folder config */
	protected String getFilePath() {
		String filePath = null;
		filePathConfig = new Properties();
		try {
			filePathConfig.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("file-path.properties"));
			filePath = filePathConfig.getProperty(Constants.DOWNLOAD_PATH);
		} catch (IOException e) {
			throw new SystemException("", "Failed to load file-path.properties", e);
		}
		return filePath;
	}

	public String getProposalIdBySplitFilePath(String filePath) {
		String[] stringArray = filePath.split("/", 0);
		String proposalId = stringArray[3];
		return proposalId;
	}

	public String getProposalIdByFilePath(String filePath) {
		String[] stringArray = filePath.split("/", 0);
		String proposalId = stringArray[2];
		return proposalId;
	}

	public static String getMessage(String errorCode, Object... params) {
		String text = null;
		try {
			text = getBundle().getString(errorCode);
		} catch (MissingResourceException e) {
			text = "!! key " + errorCode + " not found !!";
		}
		if (params != null) {
			MessageFormat mf = new MessageFormat(text);
			text = mf.format(text, params).toString();
		}
		return text;
	}

	// This method is only for addOn in jsfPages
	public boolean isNextLine(int i) {
		return i % 2 == 0 ? true : false;
	}

	protected void addWranningMessage(String id, String errorCode, Object... params) {
		String message = getMessage(errorCode, params);
		getFacesContext().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_WARN, message, ""));
	}

	protected void addInfoMessage(String id, String errorCode, Object... params) {
		String message = getMessage(errorCode, params);
		getFacesContext().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_INFO, message, ""));
	}

	protected void addErrorMessage(String id, String errorCode, Object... params) {
		String message = getMessage(errorCode, params);
		getFacesContext().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}

	protected void addDirectErrorMessage(String id, String message) {
		getFacesContext().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}

	protected void addErrorMessage(String message) {
		getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
	}

	protected void addInfoMessage(String message) {
		getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, ""));
	}

	protected void addWranningMessage(String message) {
		getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message, ""));
	}

	protected void handelException(Exception exception) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, exception.getMessage(), "");
		getFacesContext().addMessage(null, facesMessage);
	}

	/* Premium Recalculation config */
	protected final String PROPOSAL = "PROPOSAL";
	protected final String INFORM = "INFORM";
	protected final String CONFIRMATION = "CONFIRMATION";
	protected final String PAYMENT = "PAYMENT";
	private static Properties themeConfig;

	/* Dialog Selection */
	public void selectAgent() {
		Map<String, Object> dialogOptions = new HashMap<String, Object>();
		dialogOptions.put("modal", true);
		dialogOptions.put("draggable", false);
		dialogOptions.put("resizable", false);
		dialogOptions.put("contentHeight", 520);
		dialogOptions.put("contentWidth", 950);
		PrimeFaces.current().dialog().openDynamic("agentDialog", dialogOptions, null);
	}

	/**
	 * 
	 * @param workflowTask
	 * @param workFlowType
	 * @param transactionType
	 * @param branchId
	 * @param accessBranchId
	 * @purpose to find access user of workFlow. If assigned value in branchId
	 *          parameter, must be NULL in accessBranchId. And then, If assigned
	 *          value in accessBranchId parameter, must be NULL in branchId.
	 */

	public void selectCustomer() {
		Map<String, Object> dialogOptions = new HashMap<String, Object>();
		dialogOptions.put("modal", true);
		dialogOptions.put("draggable", false);
		dialogOptions.put("resizable", false);
		dialogOptions.put("contentHeight", 500);
		dialogOptions.put("contentWidth", 900);
		PrimeFaces.current().dialog().openDynamic("customerDialog", dialogOptions, null);
	}

	public void selectCashier() {
		Map<String, Object> dialogOptions = new HashMap<String, Object>();
		dialogOptions.put("modal", true);
		dialogOptions.put("draggable", false);
		dialogOptions.put("resizable", false);
		dialogOptions.put("contentHeight", 500);
		dialogOptions.put("contentWidth", 1000);
		PrimeFaces.current().dialog().openDynamic("cashierDialog", dialogOptions, null);
	}

	public void selectFirePolicy() {
		Map<String, Object> dialogOptions = new HashMap<String, Object>();
		dialogOptions.put("modal", true);
		dialogOptions.put("draggable", false);
		dialogOptions.put("resizable", false);
		dialogOptions.put("contentHeight", 500);
		dialogOptions.put("contentWidth", 1350);
		PrimeFaces.current().dialog().openDynamic("firePolicyDialog", dialogOptions, null);
	}

	public void selectPolicy() {
		Map<String, Object> dialogOptions = new HashMap<String, Object>();
		dialogOptions.put("modal", true);
		dialogOptions.put("draggable", false);
		dialogOptions.put("resizable", false);
		dialogOptions.put("contentHeight", 500);
		dialogOptions.put("contentWidth", 800);
		PrimeFaces.current().dialog().openDynamic("selectPolicyDialog", dialogOptions, null);
	}

	public void selectMotorPolicyNo() {
		Map<String, Object> dialogOptions = new HashMap<String, Object>();
		dialogOptions = new HashMap<String, Object>();
		dialogOptions.put("modal", true);
		dialogOptions.put("draggable", false);
		dialogOptions.put("resizable", false);
		dialogOptions.put("contentHeight", 500);
		dialogOptions.put("contentWidth", 800);
		PrimeFaces.current().dialog().openDynamic("motorPolicyNoDialog", dialogOptions, null);
	}

	public void selectClaimNo() {
		Map<String, Object> dialogOptions = new HashMap<String, Object>();
		dialogOptions = new HashMap<String, Object>();
		dialogOptions.put("modal", true);
		dialogOptions.put("draggable", false);
		dialogOptions.put("resizable", false);
		dialogOptions.put("contentHeight", 500);
		dialogOptions.put("contentWidth", 800);
		PrimeFaces.current().dialog().openDynamic("claimNoDialog", dialogOptions, null);
	}

	public void selectFireClaimNo() {
		Map<String, Object> dialogOptions = new HashMap<String, Object>();
		dialogOptions = new HashMap<String, Object>();
		dialogOptions.put("modal", true);
		dialogOptions.put("draggable", false);
		dialogOptions.put("resizable", false);
		dialogOptions.put("contentHeight", 500);
		dialogOptions.put("contentWidth", 800);
		PrimeFaces.current().dialog().openDynamic("fireClaimNoDialog", dialogOptions, null);
	}

	public void selectCargoPolicyNo() {
		Map<String, Object> dialogOptions = new HashMap<String, Object>();
		dialogOptions = new HashMap<String, Object>();
		dialogOptions.put("modal", true);
		dialogOptions.put("draggable", false);
		dialogOptions.put("resizable", false);
		dialogOptions.put("contentHeight", 500);
		dialogOptions.put("contentWidth", 800);
		PrimeFaces.current().dialog().openDynamic("cargoPolicyNoDialog", dialogOptions, null);
	}

	protected boolean isEmpty(Object value) {
		if (value == null) {
			return true;
		}
		if (value.toString().isEmpty()) {
			return true;
		}
		return false;
	}

	private static final String ALGORITHM = "Blowfish";
	private static String keyString = "secretTest";

	public static void encrypt(File inputFile, File outputFile, OutputStream op) throws Exception {
		doCrypto(Cipher.ENCRYPT_MODE, inputFile, outputFile, op);
		System.out.println("File encrypted successfully!");
	}

	public static void decrypt(File inputFile, File outputFile) throws Exception {
		OutputStream op = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
			}
		};
		doCrypto(Cipher.DECRYPT_MODE, inputFile, outputFile, op);
		System.out.println("File decrypted successfully!");
	}

	private static void doCrypto(int cipherMode, File inputFile, File outputFile, OutputStream op) throws Exception {

		Key secretKey = new SecretKeySpec(keyString.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(cipherMode, secretKey);

		FileInputStream inputStream = new FileInputStream(inputFile);
		byte[] inputBytes = new byte[(int) inputFile.length()];
		inputStream.read(inputBytes);

		byte[] outputBytes = cipher.doFinal(inputBytes);

		FileOutputStream outputStream = new FileOutputStream(outputFile);
		op.write(outputBytes);
		outputStream.write(outputBytes);
		inputStream.close();
	}
	
	public void createFile(File file, byte[] content) {
		try {
			/* At First : Create directory of target file */
			String filePath = file.getPath();
			int lastIndex = filePath.lastIndexOf("\\") + 1;
			FileUtils.forceMkdir(new File(filePath.substring(0, lastIndex)));

			/* Create target file */
			FileOutputStream outputStream = new FileOutputStream(file);
			IOUtils.write(content, outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getSystemPath() {
		Object context = getFacesContext().getExternalContext().getContext();
		String systemPath = ((ServletContext) context).getRealPath("/");
		return systemPath;
	}

	public String getUploadPath() {
		return UploadFileConfig.getUploadFilePathHome();
	}

	public String getFileName(String filePath) {
		int lastIndex = filePath.lastIndexOf("/") + 1;
		return filePath.substring(lastIndex, filePath.length());
	}

	public List<Integer> getYearList() {
		List<Integer> years = new ArrayList<Integer>();
		int endYear = Calendar.getInstance().get(Calendar.YEAR);
		for (int startYear = 1999; startYear <= endYear; startYear++) {
			years.add(startYear);
		}
		Collections.reverse(years);
		return years;
	}

	public void redirectDashboardPage() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		try {
			externalContext.redirect(DASHBOARD_URL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isPdfFile(String fileName) {
		if (fileName != null && fileName.contains(".pdf"))
			return true;
		return false;
	}

	public boolean isImageFile(String fileName) {// gif|jpe?g|png
		if (fileName != null && (fileName.contains(".gif") || fileName.contains(".jpeg") || fileName.contains(".jpg") || fileName.contains(".png")))
			return true;
		return false;
	}

	public double validateMinSI() {
		return 1;
	}

	protected void handleSysException(SystemException systemException) {
		String errorCode = systemException.getErrorCode();
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage(errorCode), "");
		getFacesContext().addMessage(null, facesMessage);
	}

	protected void handleException(Exception exception) {
		String message = exception.getMessage() == null ? "System Error." : exception.getMessage();
		exception.printStackTrace();
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, message, "");
		getFacesContext().addMessage(null, facesMessage);
	}

	/**
	 *********************** Dialog Information *******************
	 */
	private static Map<String, Object> dialogOptions;

	public static Map<String, Object> getDialogOptions() {
		if (dialogOptions == null) {
			dialogOptions = new HashMap<String, Object>();
			dialogOptions.put("modal", true);
			dialogOptions.put("draggable", false);
			dialogOptions.put("resizable", false);
			dialogOptions.put("width", 640);
			dialogOptions.put("height", 340);
			dialogOptions.put("contentHeight", "100%");
			dialogOptions.put("contentWidth", "100%");
		}
		return dialogOptions;
	}

	public static Map<String, Object> getDialogOptions(int width, int height, int contentHeight, int contentWidth) {
		if (dialogOptions == null) {
			dialogOptions = new HashMap<String, Object>();
		}
		dialogOptions.put("modal", true);
		dialogOptions.put("draggable", false);
		dialogOptions.put("resizable", false);
		dialogOptions.put("width", width);
		dialogOptions.put("height", height);
		dialogOptions.put("contentHeight", contentHeight + "%");
		dialogOptions.put("contentWidth", contentWidth + "%");
		return dialogOptions;
	}

	public static Map<String, Object> getCustomDialogOptions() {
		if (dialogOptions == null) {
			dialogOptions = new HashMap<String, Object>();
		}
		dialogOptions.put("modal", true);
		dialogOptions.put("draggable", false);
		dialogOptions.put("resizable", true);
		dialogOptions.put("width", 85 + "%");
		dialogOptions.put("height", 350);
		dialogOptions.put("position", "top");
		dialogOptions.put("contentHeight", 100 + "%");
		dialogOptions.put("contentWidth", 100 + "%");
		return dialogOptions;
	}

	public void selectRole() {
		PrimeFaces.current().dialog().openDynamic(DialogId.ROLE_DIALOG, getDialogOptions(), null);
		/*
		 * RequestContext.getCurrentInstance().openDialog(DialogId.ROLE_DIALOG,
		 * getDialogOptions(), null);
		 */
	}

	public void selectBranch() {
		PrimeFaces.current().dialog().openDynamic(DialogId.BRANCH_DIALOG, getDialogOptions(), null);
		/*
		 * RequestContext.getCurrentInstance().openDialog(DialogId.BRANCH_DIALOG,
		 * getDialogOptions(800, 500, 100, 100), null);
		 */
	}

	public void selectEmployee() {
		PrimeFaces.current().dialog().openDynamic(DialogId.EMPLOYEE_DIALOG, getDialogOptions(), null);
	}

	// select occupation
	public void selectOccupation() {
		PrimeFaces.current().dialog().openDynamic(DialogId.OCCUPATION_DIALOG, getDialogOptions(), null);
	}

	public void selectDepartment() {
		PrimeFaces.current().dialog().openDynamic(DialogId.DEPT_DIALOG, getDialogOptions(), null);
		/*
		 * RequestContext.getCurrentInstance().openDialog(DialogId.DEPT_DIALOG,
		 * getDialogOptions(), null);
		 */
	}

	public void selectCoa() {
		PrimeFaces.current().dialog().openDynamic(DialogId.COA_DIALOG, getDialogOptions(), null);
		/*
		 * RequestContext.getCurrentInstance().openDialog(DialogId.COA_DIALOG,
		 * getCustomDialogOptions(), null);
		 */
	}

	public void selectFFCoa() {
		Map<String, Object> custom = getDialogOptions();
//      custom.put("width", "80%");
//      custom.put("height", "100%");
		custom.put("modal", true);
		custom.put("draggable", false);
		custom.put("resizable", false);
		custom.put("width", 900);
		custom.put("height", 500);
		PrimeFaces.current().dialog().openDynamic(DialogId.FF_COA_DIALOG, getDialogOptions(), null);
		/*
		 * RequestContext.getCurrentInstance().openDialog(DialogId.FF_COA_DIALOG,
		 * getDialogOptions(), null);
		 */
	}

	public void selectCCOAAccountCode() {
		PrimeFaces.current().dialog().openDynamic(DialogId.CCOA_DIALOG, getDialogOptions(800, 500, 100, 100), null);
		/*
		 * RequestContext.getCurrentInstance().openDialog(DialogId.CCOA_DIALOG,
		 * getDialogOptions(800, 500, 100, 100), null);
		 */
	}

	public void selectCCOAAccountCode(Currency currency) {
		putParam(ParamId.CURRENCY_DATA, currency);
		PrimeFaces.current().dialog().openDynamic(DialogId.CCOA_DIALOG, getDialogOptions(800, 500, 100, 100), null);
		/*
		 * RequestContext.getCurrentInstance().openDialog(DialogId.CCOA_DIALOG,
		 * getDialogOptions(800, 500, 100, 100), null);
		 */
	}

	public void selectVoucherNo() {
		PrimeFaces.current().dialog().openDynamic(DialogId.VOUCHER_NO_DIALOG, getDialogOptions(), null);
		/*
		 * RequestContext.getCurrentInstance().openDialog(DialogId.VOUCHER_NO_DIALOG,
		 * getDialogOptions(), null);
		 */
	}

}