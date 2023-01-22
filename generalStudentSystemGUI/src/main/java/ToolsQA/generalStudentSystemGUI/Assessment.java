package ToolsQA.generalStudentSystemGUI;


public class Assessment {
	private String name;
	private double weight;
	private int totalMarks;
	
	public Assessment() {}
	public Assessment(String s, double w, int n) {
		name = s;
		

		weight = w;
		totalMarks = n;
	}

	public int getTotalMarks() {
		return totalMarks;
	}

	public double getWeight() {
		return weight;
	}
	public String getName() {
		return name;
	}
}
