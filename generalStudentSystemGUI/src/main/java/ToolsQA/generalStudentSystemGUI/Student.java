package ToolsQA.generalStudentSystemGUI;

import java.util.Hashtable;

public class Student {
	private ClassCourse[] classes;
	private Hashtable<Course, Boolean> courseReqs;
	private int year;
	private String firstName;
	private String lastName;
	private int studentNumber;
	
	public Student(Hashtable<Course, Boolean> requests, int grade, String first, String last, int id) {
		courseReqs=requests;
		year=grade;
		firstName=first;
		lastName=last;
		studentNumber=id;
	}
	
	public void addClass(ClassCourse c) {
		classes[c.getPeriod()-1] = c;
	}
	public void removeClass(int period) {
		classes[period-1] = null;
	}
}
