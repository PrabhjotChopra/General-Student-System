package ToolsQA.generalStudentSystemGUI;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class School implements ActionListener, FocusListener {
	
	public static Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
	
	static LinkedList<Student> students = new  LinkedList<Student>();
	static LinkedList<Teacher> teachers = new  LinkedList<Teacher>();
	static LinkedList<Course> courseOfferings = new  LinkedList<Course>();
	static LinkedList<ClassCourse> currentClasses = new  LinkedList<ClassCourse>();

	static JButton tLogin;
	static JButton aLogin;
	static JButton submitTeacherLogin;
	static JButton submitAdminLogin;

	static JTextField ID;
	static JTextField lastName;
	static JTextField password;

	static JPanel loginChoice;
	static JPanel teacherLogin;
	static JPanel adminLogin;
	static JFrame window;
	static String adminPass = "123tester"; // hardcoded since database isn't finished
	static JTextArea incorrect;
	
	static Teacher currProf;

	
	public void addStudent(Student s) {
		students.add(s);
	}

	public void removeStudent(Student s) {
		students.remove(s);
	}

	public void addTeacher(Teacher t) {
		teachers.add(t);
	}

	public void removeTeacher(Teacher t) {
		teachers.remove(t);
	}

	public void addCourse(Course c) {
		courseOfferings.add(c);
	}

	public void removeCourse(Course c) {
		courseOfferings.remove(c);
	}

	public void addClass(ClassCourse c) {
		currentClasses.add(c);
	}

	public void removeClass(ClassCourse c) {
		currentClasses.remove(c);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String a = e.getActionCommand();
		
		if (e.getSource() == tLogin) {

			window.remove(loginChoice);
			window.add(teacherLogin);
			window.revalidate();
			window.repaint();

		} else if (e.getSource() == aLogin) {

			window.remove(loginChoice);
			window.add(adminLogin);
			window.revalidate();
			window.repaint();
		} else if (e.getSource() == submitTeacherLogin) { // edit to check login credentials
			Teacher loginner = teacherLogin();
			if(loginner!=null) {
				currProf = loginner;
				window.setExtendedState(JFrame.MAXIMIZED_BOTH);
				window.remove(teacherLogin);
				window.revalidate();
				window.repaint();
				window.setLayout(null);
				
				window.add(currProf.getHeader());
				window.add(currProf.getSem1());
				window.add(currProf.getSem2());
				window.add(currProf.getCourses());
				
				window.revalidate();
				window.repaint();
				
			}
			else {
				incorrect.setText("Login information incorrect");
				teacherLogin.add(incorrect);
			}
			window.revalidate();
			window.repaint();
		} else if (e.getSource() == submitAdminLogin) { 
			
			if(password.getText().equals(adminPass)) {
				window.setExtendedState(JFrame.MAXIMIZED_BOTH);
			}
			else {
				incorrect.setText("Password incorrect");
				adminLogin.add(incorrect);
			}
			
			window.revalidate();
			window.repaint();
		}
		else if (e.getActionCommand().equals("Semester 2")) {
			currProf.switchSem(2);
			
		}
		else if (e.getActionCommand().equals("Semester 1")) {
			currProf.switchSem(1);
		}
		

	}

	public static void initialize() { // initializes login selection and login interface
		FlatDarkLaf.setup();

		window = new JFrame("General Student System");

		window.setSize(500, 300);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		loginChoice = new JPanel();
		loginChoice.setLayout(new GridLayout(2, 1));

		tLogin = new JButton("Click here for the teacher login");
		aLogin = new JButton("Click here for the administrator login");
		tLogin.setFont(new Font("Serif", Font.PLAIN, 20));
		aLogin.setFont(new Font("Serif", Font.PLAIN, 20));

		loginChoice.add(tLogin);
		loginChoice.add(aLogin);

		tLogin.addActionListener(new School());
		aLogin.addActionListener(new School());

		window.getContentPane().add(loginChoice);

		teacherLogin = new JPanel();
		teacherLogin.setLayout(new BoxLayout(teacherLogin, BoxLayout.PAGE_AXIS));

		JTextArea login = new JTextArea("Enter your login information in the boxes below");
		login.setEditable(false);
		lastName = new JTextField("Enter your last name here");
		ID = new JTextField("Enter your 5-digit teacher ID here");
		lastName.addFocusListener(new School());
		ID.addFocusListener(new School());

		submitTeacherLogin = new JButton("Submit teacher login");
		submitTeacherLogin.addActionListener(new School());

		login.setFont(new Font("Serif", Font.PLAIN, 24));
		login.setMaximumSize(new Dimension(500, 50));

		lastName.setMaximumSize(new Dimension(250, 30));
		lastName.setFont(new Font("Arial", Font.PLAIN, 15));
		
		ID.setMaximumSize(new Dimension(250, 30));
		ID.setFont(new Font("Arial", Font.PLAIN, 15));
		
		submitTeacherLogin.setMaximumSize(new Dimension(250, 50));
		submitTeacherLogin.setFont(new Font("Arial", Font.PLAIN, 18));
		
		teacherLogin.add(login);
		teacherLogin.add(Box.createRigidArea(new Dimension(0, 10)));
		teacherLogin.add(lastName);
		teacherLogin.add(Box.createRigidArea(new Dimension(0, 20)));
		teacherLogin.add(ID);
		teacherLogin.add(Box.createRigidArea(new Dimension(0, 15)));
		teacherLogin.add(submitTeacherLogin);
		submitTeacherLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
		teacherLogin.add(Box.createRigidArea(new Dimension(0, 20)));
		
		JTextArea pass = new JTextArea("Enter the school-assigned password to login");
		pass.setEditable(false);
		adminLogin = new JPanel();
		adminLogin.setLayout(new BoxLayout(adminLogin, BoxLayout.PAGE_AXIS));
		
		pass.setFont(new Font("Serif", Font.PLAIN, 26));
		pass.setMaximumSize(new Dimension(500, 50));
		
		submitAdminLogin = new JButton("Submit administrator login");
		submitAdminLogin.addActionListener(new School());
		submitAdminLogin.setMaximumSize(new Dimension(250, 50));
		submitAdminLogin.setFont(new Font("Arial", Font.PLAIN, 18));
		
		password = new JTextField("Enter the password here");
		password.addFocusListener(new School());
		password.setMaximumSize(new Dimension(250, 30));
		password.setFont(new Font("Arial", Font.PLAIN, 15));
		
		incorrect = new JTextArea("");
		incorrect.setFont(new Font("Arial", Font.PLAIN, 20));
		incorrect.setAlignmentX(Component.CENTER_ALIGNMENT);
		incorrect.setMaximumSize(new Dimension(250, 30));
		
		adminLogin.add(pass);
		adminLogin.add(Box.createRigidArea(new Dimension(0, 10)));
		adminLogin.add(password);
		adminLogin.add(Box.createRigidArea(new Dimension(0, 10)));
		adminLogin.add(submitAdminLogin);
		submitAdminLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
		adminLogin.add(Box.createRigidArea(new Dimension(0, 20)));
		
	}

	public void focusGained(FocusEvent e) {
		if (lastName.getText().equals("Enter your last name here") && e.getComponent() == lastName) {
			lastName.setText("");
		}

		if (ID.getText().equals("Enter your 5-digit teacher ID here") && e.getComponent() == ID) {
			ID.setText("");
		}
		if (password.getText().equals("Enter the password here") && e.getComponent() == password) {
			password.setText("");
		} 
	}

	public void focusLost(FocusEvent e) {
		if (lastName.getText().equals("") && e.getComponent() == lastName) {
			lastName.setText("Enter your last name here");
		}
		if (ID.getText().equals("") && e.getComponent() == ID) {
			ID.setText("Enter your 5-digit teacher ID here");
		}
		if (password.getText().equals("") && e.getComponent() == password) {
			password.setText("Enter the password here");
		} 
	}
	
	public Teacher teacherLogin() {
		
		// this is mainly for testing, you assume there's gonna be teachers via the UML
		if(teachers.isEmpty()) {
			return null;
		}
		
		for (int i=0;i<teachers.size();i++) {
			
			
			// checks if the inputted last name and id match the current teacher in the linked list
			if (teachers.get(i).getLastName().equals(lastName.getText()) && String.valueOf(teachers.get(i).getID()).equals(ID.getText())) {
				
				return teachers.get(i);
			}
		}
		
		return null;
		
	}

	public static void main(String[] args) {
		
		//test data
		Teacher mckay = new Teacher("Kyle", "McKay", 12345, new LinkedList<String>());
		Course ics4u = new Course("Grade 12 Computer Science", "ICS4U1", "programming", new LinkedList<String>(), 5);
		ClassCourse ourClass = new ClassCourse(1, mckay, "129", 2, ics4u);
		
		mckay.addClass(ourClass, 1);
		teachers.add(mckay);
		
		
		
		initialize();
		
		
		
		
		
	}

}
