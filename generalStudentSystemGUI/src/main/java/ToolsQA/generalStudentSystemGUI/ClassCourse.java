package ToolsQA.generalStudentSystemGUI;

import java.util.LinkedList;
import java.util.Enumeration;
import java.util.Hashtable;

public class ClassCourse extends Course {
	private int courseNum;
	private LinkedList<Student> students;
	private Hashtable<Assessment, Hashtable<Student, Double>> grades;
	private Hashtable<Student, Double> midterms;
	private Hashtable<Student, Double> finals;
	private Hashtable<Student, Double> current;
	private Hashtable<Student, LinkedList<Attend>> attendance;
	private Teacher prof;
	private String room;
	private int period;
	private double totalWeight=0.0;
	
	public ClassCourse(int n, Teacher t, String s, int p, Course c) {
		super(c);
		courseNum=n;
		prof=t;
		room=s;
		period=p;
	}
	
	public void addStudent(Student s) {
		students.add(s);
		// add to attendance, and make all the days before they joined as absent (in the case of course changes)
		// add to grades 
	}
	public void removeStudent(Student s) {
		students.remove(s);
		// remove from attendance
		// remove from grades 
	}
	public void setGrade(Student s, Assessment assign, Double marksEarned){
		Hashtable<Student, Double> marks = grades.get(assign);
		marks.replace(s, marksEarned/ (double) assign.getTotalMarks());
		
		current.replace(s, gradeCalc(s));
		grades.replace(assign, marks);
	}
	
	
	public void addAssessment(String name, double weight, int totalMarks) {
		Assessment assess = new Assessment(name, weight, totalMarks);
		Hashtable<Student, Double> marks = new Hashtable<Student, Double>();
		totalWeight+=weight;
		for(Student s: students) {
			marks.put(s, 0.0);
		}
		
		grades.put(assess, marks);
	}
	
	public void removeAssessment(Assessment assess) {
		totalWeight-= assess.getWeight();
		grades.remove(assess);
		
		Enumeration<Student> e = current.keys();
		while (e.hasMoreElements()) {
			Student kid = e.nextElement();
			current.replace(kid, gradeCalc(kid));	
		}
		
	}
	
	public double gradeCalc(Student s) {
		double grade= 0.0;
		Enumeration<Assessment> e = grades.keys();
		while (e.hasMoreElements()) {
			Assessment assign = e.nextElement();
			double mark = grades.get(assign).get(s) / (double) assign.getTotalMarks(); // student marks/total marks for that assessment
			double weight = assign.getWeight() / totalWeight; // how much that assessment is currently worth
			grade += mark*weight;
			
		}
		return grade;
	}
	
	public int getPeriod() {
		return period;
	}
}
