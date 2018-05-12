package it.unipr.ce.dsg.deus.automator.multithreading.messages.handler;

public class HandlerMessage {

	boolean ready;

	public HandlerMessage(boolean ready) {
		this.ready = ready;
	}

	public boolean isReady() {
		return ready;
	}
}
