package ToolsQA.generalStudentSystemGUI;

import java.awt.*;
import java.util.LinkedList;

import javax.swing.*;
import com.formdev.flatlaf.FlatDarkLaf;

public class Teacher {
	private LinkedList<String> teachables;
	private ClassCourse[] classes = new ClassCourse[6];
	private int ID;
	private String firstName;
	private String lastName;
	private JPanel header;
	private JButton sem1;
	private JButton sem2;
	private JPanel courses;

	public Teacher(String f, String l, int ID, LinkedList<String> teachables) {
		this.ID = ID;
		this.teachables = teachables;
		firstName = f;
		lastName = l;

		FlatDarkLaf.setup();
		

		sem1 = new JButton("Semester 1");
		sem1.setBounds(100, 180, 200, 75);
		sem1.setFont(School.buttonFont);
		sem1.addActionListener(new School());

		sem2 = new JButton("Semester 2");
		sem2.setBounds(500, 160, 200, 75);
		sem2.setFont(School.buttonFont);
		sem2.addActionListener(new School());

		header = new JPanel();
		header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));

		header.setBounds(100, 0, School.rect.width - 200, 150);
		header.setBorder(BorderFactory.createLineBorder(Color.black));

		JTextArea welcome = new JTextArea("Welcome, " + firstName.charAt(0) + ". " + lastName);
		

		welcome.setFont(new Font("Arial", 1, 50));
		header.add(welcome);
		welcome.setAlignmentX(0);
		welcome.setEditable(false);
		
		JButton classes = new JButton("Classes");
		classes.addActionListener(new School());
		classes.setBounds(School.rect.width - 400, 0,200, 100);
		
		classes.setFont(new Font("Arial", 1, 20));
		header.add(classes);
		classes.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		
		JButton exit = new JButton("Close app");
		exit.addActionListener(new School());
		exit.setSize(200,100);
		exit.setFont(new Font("Arial", 1, 20));
		header.add(exit);
		exit.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		

		courses = new JPanel();
		courses.setLayout(new BoxLayout(courses, BoxLayout.Y_AXIS));
		courses.setBounds(100, 255, School.rect.width - 200, School.rect.height - 355);
		courses.setBorder(BorderFactory.createLineBorder(Color.black));

	}
	
	
	// add courses in order in the teacher's courses array
	public boolean addClass(ClassCourse c, int sem) {
		sem--;
		sem *= 3;

		int endSem = sem + 3;
		for (int i = sem; i < endSem; i++) {
			
			if (classes[i] == null) {
				
				classes[i] = c;

				courses.add(c.getBaseDisplay());
				return true; // there was room to add the class in the teacher's schedule
			}
			else {
				if(classes[i].getPeriod() == c.getPeriod()) {
					// if the teacher is alr teaching a class in that period then return false
					return false;
				}
			}
		}
		return false; // no room to add a class to teacher's schedule
	}

	public void switchSem(int sem) {
		if (sem == 1 && sem2.getY() - sem1.getY() == 20) {
			sem1.setLocation(sem1.getX(), sem1.getY() + 20);
			sem2.setLocation(sem2.getX(), sem2.getY() - 20);

			courses.removeAll();
			for (int i = 0; i < 3; i++) {
				if (classes[i] != null) {
					courses.add(classes[i].getBaseDisplay());
				}

			}
			courses.revalidate();
			courses.repaint();

		} else if (sem == 2 && sem1.getY() - sem2.getY() == 20) {
			sem1.setLocation(sem1.getX(), sem1.getY() - 20);
			sem2.setLocation(sem2.getX(), sem2.getY() + 20);
			courses.removeAll();
			for (int i = 0; i < 3; i++) {
				if(classes[i+3]!=null) {
					courses.add(classes[i + 3].getBaseDisplay());
				}
				
			}
			courses.revalidate();
			courses.repaint();
		}

	}

	public JPanel getCourses() {
		return courses;
	}

	public String getLastName() {
		return lastName;
	}

	public int getID() {
		return ID;
	}

	public JPanel getHeader() {
		return header;
	}

	public JButton getSem1() {
		return sem1;
	}

	public JButton getSem2() {
		return sem2;
	}

	public ClassCourse[] getClasses() {
		return classes;
	}
}
