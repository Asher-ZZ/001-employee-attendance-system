package org.ace.accountig.system.leaverequest.service.interfaces;

import java.util.List;

import org.ace.accountig.system.leaverequest.LeaveRequest;
import org.ace.java.component.SystemException;

public interface ILeaveRequestService {


	public List<LeaveRequest> findAllBranch() throws SystemException;

	public LeaveRequest findBranchByBranchCode(String branchCode) throws SystemException;

	public void addNewBranch(LeaveRequest leaveRequest) throws SystemException;

	public void updateBranch(LeaveRequest leaveRequest) throws SystemException;

	public void deleteBranch(LeaveRequest leaveRequest) throws SystemException;

}
