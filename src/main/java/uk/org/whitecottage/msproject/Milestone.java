package uk.org.whitecottage.msproject;

import java.util.Date;

public class Milestone {
	String name;
	Date date;

	public Milestone(Task task) {
		date = task.getStartDate();
		name = task.getName();
	}

	public String getName() {
		return name;
	}

	public Date getDate() {
		return date;
	}
}
