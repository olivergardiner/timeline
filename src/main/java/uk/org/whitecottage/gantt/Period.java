package uk.org.whitecottage.gantt;

public class Period {
	protected String label = "";

	public Period() {
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Period(String label) {
		this.label = label;
	}
}
