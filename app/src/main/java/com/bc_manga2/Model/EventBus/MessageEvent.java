package com.bc_manga2.Model.EventBus;

public class MessageEvent {
	public String Message;
	public MessageEvent(String message) {
		Message = message;
	}
	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}
}
