package ToolsQA.generalStudentSystemGUI;

import javax.swing.JButton;

public class Assessment {
	private String name;
	private double weight;
	private int totalMarks;
	private JButton details;
	
	public Assessment() {}
	// A constructor to make an new assessment, also initializing its one jbutton
	public Assessment(String s, double w, int n) {
		name = s;
		
		weight = w;
		totalMarks = n;
		
		
		details = new JButton("View Details");
		details.addActionListener(new School());
		details.setActionCommand("indAss " + name);
		details.setFont(Student.studentStandard);
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
	public JButton getDetails() {
		return details;
	}
	public void setTotalMarks(int n) {
		totalMarks=n;
	}
	public void setName(String s) {
		name=s;
		
		details.setActionCommand("indAss " + name);
		
	}
	public void setWeight(double w) {
		weight=w;
	}
}
