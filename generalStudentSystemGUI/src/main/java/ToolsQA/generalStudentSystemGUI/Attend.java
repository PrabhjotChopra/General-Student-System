package ToolsQA.generalStudentSystemGUI;

public class Attend {
	private boolean present;
	private boolean late;
	private int minutesLate;
	private String reason;
	
	public Attend(boolean p, boolean l, int m, String s) {
		present = p;
		late = l;
		minutesLate = m;
		reason = s;
	}
	/**
	 * 
	 */
	public Attend() {
		// TODO Auto-generated constructor stub
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
