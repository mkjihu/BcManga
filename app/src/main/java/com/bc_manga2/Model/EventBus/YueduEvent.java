package com.bc_manga2.Model.EventBus;

public class YueduEvent {
	public String Message;

	public YueduEvent(String message) {
		Message = message;
	}
	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}
	
}