package it.unipr.ce.dsg.deus.example.basic;



public class TestEvent {

	String id = null;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	float triggeringTime = 0;
	public TestEvent(String id, float t) {
		this.id = id;
		this.triggeringTime = t;
	}
	
}
