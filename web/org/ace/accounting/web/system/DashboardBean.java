package org.ace.accounting.web.system;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.system.employee.Employee;
import org.ace.accounting.system.leaverequest.LeaveRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class DashboardBean implements Serializable {
	private int todayAttendance;
	private int presentEmployees;
	private int totalEmployees;
	private int pendingApprovals;
	private int lateArrivals;
	private int onLeave;
	private List<String> recentActivities;
	private List<Employee> recentEmployees;
	private List<LeaveRequest> recentLeaveRequests;

	@PostConstruct
	public void init() {
		// Initialize with sample data - replace with actual service calls
		todayAttendance = 85;
		presentEmployees = 34;
		totalEmployees = 40;
		pendingApprovals = 5;
		lateArrivals = 3;
		onLeave = 6;

		// Sample recent activities
		recentActivities = new ArrayList<>();
		recentActivities.add("John Doe checked in at 8:45 AM");
		recentActivities.add("Jane Smith requested leave (Pending)");
		recentActivities.add("Robert Johnson checked out at 5:30 PM");
		recentActivities.add("Sara Williams updated her attendance");

		// Initialize other lists
		recentEmployees = new ArrayList<>(); // Populate from service
		recentLeaveRequests = new ArrayList<>(); // Populate from service
	}

	// Navigation methods
	public String navigateToAttendance() {
		return "attendance?faces-redirect=true";
	}

	public String navigateToLeaveRequest() {
		return "leaveRequest?faces-redirect=true";
	}

	public String navigateToEmployees() {
		return "manageEmployee?faces-redirect=true";
	}

	public String navigateToLeaveManagement() {
		return "leaveEnquiry?faces-redirect=true";
	}

	public void generateReport() {
		// Implement report generation logic
	}

	public void viewEmployee(Employee emp) {
		// Implement employee view logic
	}

	public void viewLeaveRequest(LeaveRequest leave) {
		// Implement leave request view logic
	}

	// Getters and setters
	public int getTodayAttendance() {
		return todayAttendance;
	}

	public void setTodayAttendance(int todayAttendance) {
		this.todayAttendance = todayAttendance;
	}

	public int getPresentEmployees() {
		return presentEmployees;
	}

	public void setPresentEmployees(int presentEmployees) {
		this.presentEmployees = presentEmployees;
	}

	public int getTotalEmployees() {
		return totalEmployees;
	}

	public void setTotalEmployees(int totalEmployees) {
		this.totalEmployees = totalEmployees;
	}

	public int getPendingApprovals() {
		return pendingApprovals;
	}

	public void setPendingApprovals(int pendingApprovals) {
		this.pendingApprovals = pendingApprovals;
	}

	public int getLateArrivals() {
		return lateArrivals;
	}

	public void setLateArrivals(int lateArrivals) {
		this.lateArrivals = lateArrivals;
	}

	public int getOnLeave() {
		return onLeave;
	}

	public void setOnLeave(int onLeave) {
		this.onLeave = onLeave;
	}

	public List<String> getRecentActivities() {
		return recentActivities;
	}

	public void setRecentActivities(List<String> recentActivities) {
		this.recentActivities = recentActivities;
	}

	public List<Employee> getRecentEmployees() {
		return recentEmployees;
	}

	public void setRecentEmployees(List<Employee> recentEmployees) {
		this.recentEmployees = recentEmployees;
	}

	public List<LeaveRequest> getRecentLeaveRequests() {
		return recentLeaveRequests;
	}

	public void setRecentLeaveRequests(List<LeaveRequest> recentLeaveRequests) {
		this.recentLeaveRequests = recentLeaveRequests;
	}
}