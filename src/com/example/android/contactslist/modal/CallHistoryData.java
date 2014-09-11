package com.example.android.contactslist.modal;

import java.io.Serializable;
import java.util.Date;

public class CallHistoryData implements Serializable{

	private String calltype;
	private String callnumber;
	private Date calldatetime;
	private String callduration;
	private String callerName;
	private String callerImageURL;

	public CallHistoryData()
	{

	}

	public CallHistoryData(String calltype, String callnumber, Date calldatetime, String callduration)
	{
		this.calldatetime=calldatetime;
		this.callduration=callduration;
		this.callnumber=callnumber;
		this.calltype=calltype;
	}

	public String getCalltype() {
		return calltype;
	}

	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}

	public String getCallnumber() {
		return callnumber;
	}

	public void setCallnumber(String callnumber) {
		this.callnumber = callnumber;
	}

	public Date getCalldatetime() {
		return calldatetime;
	}

	public void setCalldatetime(Date calldatetime) {
		this.calldatetime = calldatetime;
	}

	public String getCallduration() {
		return callduration;
	}

	public void setCallduration(String callduration) {
		this.callduration = callduration;
	}

	public String getCallerName() {
		return callerName;
	}

	public void setCallerName(String callerName) {
		this.callerName = callerName;
	}

	public String getCallerImageURL() {
		return callerImageURL;
	}

	public void setCallerImageURL(String callerImageURL) {
		this.callerImageURL = callerImageURL;
	}

}