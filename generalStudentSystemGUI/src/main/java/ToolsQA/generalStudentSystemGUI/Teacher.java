package ToolsQA.generalStudentSystemGUI;

import java.awt.*;
import java.util.LinkedList;

import javax.swing.*;
import com.formdev.flatlaf.FlatDarkLaf;

public class Teacher {
	private LinkedList<String> teachables;
	private ClassCourse[] classes = new ClassCourse[8];
	private int ID;
	private String firstName;
	private String lastName;
	private JPanel header;
	private JButton sem1;
	private JButton sem2;
	private JPanel courses;

	public Teacher() {
	}

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

		classes.setFont(new Font("Arial", 1, 40));
		header.add(classes);
		classes.setAlignmentX(Component.RIGHT_ALIGNMENT);
		header.add(Box.createRigidArea(new Dimension(20, 0)));

		JButton exit = new JButton("Close app");
		exit.addActionListener(new School());

		exit.setFont(new Font("Arial", 1, 40));
		header.add(exit);
		exit.setAlignmentX(Component.RIGHT_ALIGNMENT);

		courses = new JPanel();
		courses.setLayout(new BoxLayout(courses, BoxLayout.Y_AXIS));
		courses.setBounds(100, 255, School.rect.width - 200, School.rect.height - 355);
		courses.setBorder(BorderFactory.createLineBorder(Color.black));

	}

	// add courses in order in the teacher's courses array
	public boolean addClass(ClassCourse c, int sem) throws NullPointerException {
		sem--;
		sem *= 4;

		int endSem = sem + 4;
		int coursesThisSem = 0;
		for (int i = sem; i < endSem; i++) {
			if(classes[i]!=null) {
				coursesThisSem++;
			}
		}

		if (classes[c.getPeriod() - 1] == null && coursesThisSem != 3) { // if the teacher is free the period the course
																			// is running
			
			classes[c.getPeriod() - 1] = c;

			return true; // there was room to add the class in the teacher's schedule and it was added
		}

		return false; // no room to add a class to teacher's schedule
	}

	public void switchSem(int sem) {
		if (sem == 1 && sem2.getY() - sem1.getY() == 20) {
			sem1.setLocation(sem1.getX(), sem1.getY() + 20);
			sem2.setLocation(sem2.getX(), sem2.getY() - 20);

			courses.removeAll();
			for (int i = 0; i < 4; i++) {
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
			for (int i = 4; i < 8; i++) {
				if (classes[i] != null) {
					courses.add(classes[i].getBaseDisplay());
				}

			}
			courses.revalidate();
			courses.repaint();
		}

	}

	public JPanel getCourses(int sem) {
		sem--;
		sem *= 4;
		for (int i = sem; i < sem + 4; i++) {
			if (classes[i] != null) {
				courses.add(classes[i].getBaseDisplay());
			}

		}
		return courses;
	}

	public String getLastName() {
		return lastName;
	}
	public String getFirstName() {
		return firstName;
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

	public LinkedList<String> getTeachables() {
		return teachables;
	}
}
