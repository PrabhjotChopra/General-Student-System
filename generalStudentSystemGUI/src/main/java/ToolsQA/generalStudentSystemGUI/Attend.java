package ToolsQA.generalStudentSystemGUI;

public class Attend {
	private boolean present;
	private boolean late;
	private int minutesLate;
	private String reason;
	
	// A constructor for the attendance of an individual student on an individual day
	public Attend(boolean p, boolean l, int m, String s) {
		present = p;
		late = l;
		minutesLate = m;
		reason = s;
	}
	public boolean getPresent() {
		return present;
	}
	public boolean getLate() {
		return late;
	}
	public int getMinutesLate() {
		return minutesLate;
	}
	public String getReason() {
		return reason;
	}
}
