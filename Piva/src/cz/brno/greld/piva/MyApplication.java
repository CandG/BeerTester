package cz.brno.greld.piva;

import android.app.Application;

public class MyApplication extends Application {
	private Events events;
	private Event event;

	public Events getEvents() {
		return events;
	}

	public void setEvents(Events events) {
		this.events = events;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
	
	
}
