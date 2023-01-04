package ToolsQA.generalStudentSystemGUI;

import java.util.LinkedList;

public class Course {
	private String name;
	private String code;
	private String type;
	private LinkedList<String> usableRooms;
	private int priority;
	
	
	public Course(String name, String code, String type, LinkedList<String> rooms, int priority) {
		this.name = name;
		this.code = code;
		this.type = type;
		usableRooms = rooms;
		this.priority = priority;
	}
	
	public Course(Course c) { // super constructor for ClassCourse object
		this.name = c.name;
		this.code = c.code;
		this.type = c.type;
		this.usableRooms = new LinkedList<String>(c.usableRooms);
		this.priority = c.priority;
	}
	
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
}
