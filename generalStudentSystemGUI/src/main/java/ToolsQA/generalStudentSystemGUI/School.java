package ToolsQA.generalStudentSystemGUI;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Hashtable;
import java.util.LinkedList;

public class School implements ActionListener, FocusListener {

	public static Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
	public static LocalTime[] periodStarts = {LocalTime.of(8, 15), LocalTime.of(9, 37), LocalTime.of(11, 58), LocalTime.of(13, 20)};

	public static Font buttonFont = new Font("Arial", 1, 20);
	
	public static int sem1days = 90;
	public static int sem2days = 90;
	

	static LinkedList<Student> students = new LinkedList<Student>();
	static LinkedList<Teacher> teachers = new LinkedList<Teacher>();
	static LinkedList<Course> courseOfferings = new LinkedList<Course>();
	static LinkedList<ClassCourse> currentClasses = new LinkedList<ClassCourse>();
	static boolean teacher;
	static boolean admin;

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
	static JPanel dashboard;

	static Teacher currProf;
	static ClassCourse currClass;

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
		if (teacher) {
			for (int i = 0; i < 6; i++) {
				if (currProf.getClasses()[i] != null) {
					if (e.getSource() == currProf.getClasses()[i].getBaseDisplay()) {
						currClass = currProf.getClasses()[i];
						
						
						dashboard.removeAll();
						dashboard.add(currClass.getMain());
						dashboard.add(currClass.getDaily());
						dashboard.add(currClass.getOverallAtt());
						currClass.goDash();
						dashboard.add(currClass.getTab());
						dashboard.revalidate();
						dashboard.repaint();
						
						window.repaint();
					}
				}
			}
		}

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
			
			if (loginner != null) {
				currProf = loginner;
				window.setExtendedState(JFrame.MAXIMIZED_BOTH);
				window.remove(teacherLogin);
				window.revalidate();
				window.repaint();
				teacher = true;
				dashboard = new JPanel(null);

				window.add(currProf.getHeader());
				
				dashboard.add(currProf.getSem1());
				dashboard.add(currProf.getSem2());
				dashboard.add(currProf.getCourses());
				window.add(dashboard);

				window.revalidate();
				window.repaint();

			} else {
				incorrect.setText("Login information incorrect");
				teacherLogin.add(incorrect);
			}
			window.revalidate();
			window.repaint();
		} else if (e.getSource() == submitAdminLogin) {

			if (password.getText().equals(adminPass)) {
				window.setExtendedState(JFrame.MAXIMIZED_BOTH);
			} else {
				incorrect.setText("Password incorrect");
				adminLogin.add(incorrect);
			}

			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("Semester 2")) {
			currProf.switchSem(2);

		} else if (e.getActionCommand().equals("Semester 1")) {
			currProf.switchSem(1);
		} else if (e.getActionCommand().equals("Close app")) {
			System.exit(0);
		} else if (e.getActionCommand().equals("Classes")) {

			dashboard.removeAll();
			dashboard.add(currProf.getSem1());
			dashboard.add(currProf.getSem2());
			dashboard.add(currProf.getCourses());
			window.revalidate();
			window.repaint();
		}
		else if (e.getActionCommand().equals("Daily Attendance")) {
			
			dashboard.add(currClass.getDayChoice());
			
			dashboard.add(currClass.getSubAtt());
			
			dashboard.add(currClass.addDailys());
			currClass.changeAttDay(1);
			window.add(dashboard);
			
			window.revalidate();
			window.repaint();
			
			
			
			}
		else if (e.getActionCommand().equals("Dashboard")) {
			currClass.goDash();
			dashboard.remove(currClass.getDayChoice());
			
			dashboard.remove(currClass.getSubAtt());
			window.revalidate();
			window.repaint();
		}
		else if (e.getActionCommand().equals("Present")) {
			for(int i=0;i<currClass.getStudents().size();i++) {
				if(e.getSource() == currClass.getStudents().get(i).getPresent()) {
					Student temp = currClass.getStudents().get(i);
					currClass.setPresent(temp);
				}
			}
		}
		else if (e.getActionCommand().equals("Absent")) {
			for(int i=0;i<currClass.getStudents().size();i++) {
				if(e.getSource() == currClass.getStudents().get(i).getAbsent()) {
					Student temp = currClass.getStudents().get(i);
					currClass.setAbsent(temp);
				}
			}
		}
		else if (e.getActionCommand().equals("Late")) {
			for(int i=0;i<currClass.getStudents().size();i++) {
				if(e.getSource() == currClass.getStudents().get(i).getLate()) {
					Student temp = currClass.getStudents().get(i);
					currClass.setLate(temp);
				}
			}
		}
		else if(e.getActionCommand().equals("Day Choice")) {
			currClass.changeAttDay(1);
			
		}
		else if(e.getActionCommand().equals("Submit Attendance")) {
			currClass.submitAttendance();
		}
		else if (e.getActionCommand().equals("Overall Attendance")) {
			
			dashboard.remove(currClass.getSubAtt());
			
			
			dashboard.add(currClass.overallAtt());
		
			
			window.revalidate();
			window.repaint();
		}
		else if (e.getActionCommand().equals("Until day x")) {
			currClass.changeAttDay(2);
			window.revalidate();
			window.repaint();
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
		
		if(teacher) {
			for(int i=0;i<currClass.getStudents().size();i++) {
				Student temp = currClass.getStudents().get(i);
				if(e.getSource() == temp.getMinsLate() && temp.getMinsLate().getText().equals("Time arrived")) {
					currClass.getStudents().get(i).setMinsLate("");
					
				}
				else if(e.getSource() == temp.getAbsentReason() && temp.getAbsentReason().getText().equals("Reason for absence")) {
					currClass.getStudents().get(i).setAbsentReason("");
				}
			}
		}
		
		
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
		if(teacher) {
			for(int i=0;i<currClass.getStudents().size();i++) {
				Student temp = currClass.getStudents().get(i);
				if(e.getSource() == temp.getMinsLate() && temp.getMinsLate().getText().equals("")) {
					currClass.getStudents().get(i).setMinsLate("Time arrived");
				}
				else if(e.getSource() == temp.getAbsentReason() && temp.getAbsentReason().getText().equals("")) {
					currClass.getStudents().get(i).setAbsentReason("Reason for absence");
				}
			}
		}
		
		
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
		if (teachers.isEmpty()) {
			return null;
		}

		for (int i = 0; i < teachers.size(); i++) {

			// checks if the inputted last name and id match the current teacher in the
			// linked list
			if (teachers.get(i).getLastName().equals(lastName.getText())
					&& String.valueOf(teachers.get(i).getID()).equals(ID.getText())) {

				return teachers.get(i);
			}
		}

		return null;

	}

	public static void main(String[] args) {

		// test data
		Teacher mckay = new Teacher("Kyle", "McKay", 12345, new LinkedList<String>());
		Course ics4u = new Course("Grade 12 Computer Science", "ICS4U1", "Computer Studies", new LinkedList<String>(), 5, 30,1,15);
		ClassCourse ourClass = new ClassCourse(1, mckay, "129", 2, ics4u);
		
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Prabhjot", "Chopra", 1));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Danny", "Song", 2));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Lawrence", "Huang", 3));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Doris", "Zhang", 4));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Isa", "Alif", 5));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Naheen", "Mahboob", 6));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Ahmed", "Sinjab", 7));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Dayeon", "Choi", 8));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Calvin", "Cao", 9));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Varun", "Basdeo", 10));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Abdulmuhaimin", "Ali", 11));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Trevor", "Bliss", 12));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Victor", "Reznov", 13));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Sofia", "Odegaard", 14));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Tanya", "Poulouchina", 15));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Minjae", "Kim", 16));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Mashrur", "Khandaker", 17));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Cason", "Cook", 18));
		ourClass.addStudent(new Student(new Hashtable<Course, Boolean>(), 12, "Nathan", "Killinger", 19));
		
		mckay.addClass(ourClass, 1);

		teachers.add(mckay);

		initialize();

	}

	
	

}
