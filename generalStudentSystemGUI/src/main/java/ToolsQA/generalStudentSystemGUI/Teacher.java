package ToolsQA.generalStudentSystemGUI;

import java.util.LinkedList;

public class Teacher {
	private LinkedList<String> teachables;
	private ClassCourse[] classes = new ClassCourse[6];
	private int ID;
	private String firstName;
	private String lastName;
	
	
	public Teacher(String f, String l, int ID, LinkedList<String> teachables) {
		this.ID=ID;
		this.teachables=teachables;
		firstName=f;
		lastName=l;
		
	}
	
	public boolean addClass(ClassCourse c, int sem) {
		sem--;
		sem*=3;
		
		int endSem = sem+3;
		for(int i=sem; i<endSem; i++) {
			if (classes[i]==null) {
				classes[i] = c;
				return true; // there was room to add the class in the teacher's schedule
			}
		}
		return false; // no room to add a class to teacher's schedule
	}

	public String getLastName() {
		return lastName;
	}

	public int getID() {
		return ID;
	}
	
	
}
