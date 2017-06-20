package com.bc_manga2.Model.EventBus;

public class CollectionEvent {
	public String Message;
	public CollectionEvent(String message) {
		Message = message;
	}
	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}
	
}
