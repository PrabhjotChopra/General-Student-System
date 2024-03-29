package ToolsQA.generalStudentSystemGUI;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

public class School implements ActionListener, FocusListener {

	public static Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDefaultConfiguration().getBounds();
	public static LocalTime[] periodStarts = { LocalTime.of(8, 15), LocalTime.of(9, 37), LocalTime.of(11, 58),
			LocalTime.of(13, 20) };

	public static Font buttonFont = new Font("Arial", 1, 20);

	public static int sem1days = 90;
	public static int sem2days = 90;

	static LinkedList<Student> students = new LinkedList<Student>();
	static LinkedList<Teacher> teachers = new LinkedList<Teacher>();
	static LinkedList<Course> courseOfferings = new LinkedList<Course>();
	static LinkedList<ClassCourse> currentClasses = new LinkedList<ClassCourse>();
	static LinkedList<String> rooms = new LinkedList<String>();
	static final String USERNAME = "root";// DBMS connection username
	static final String PASSWORD = "329228654sql";// DBMS connection password
	static final String URL = "jdbc:mysql://localhost:3306/finals";// DBMS connection URL

	static boolean teacher;
	static boolean student;

	static JButton tLogin;
	static JButton aLogin;
	static JButton submitTeacherLogin;
	static JButton submitstudentLogin;

	static JTextField ID;
	static JTextField lastName;
	static JTextField password;

	static JPanel loginChoice;
	static JPanel teacherLogin;
	static JPanel studentLogin;
	static JFrame window;

	static JTextArea incorrect;
	static JPanel dashboard;

	static Teacher currProf;
	static ClassCourse currClass;
	static Student currKid;

	// Adding and removing students from the students list.
	public void addStudent(Student s) {
		students.add(s);
	}

	public void removeStudent(Student s) {
		students.remove(s);
	}

	@Override
	// Checking if the user is a teacher, and if they are, it is checking if the user has clicked on a
	// class. If they have, it is setting the current class to the class that was clicked on.
	public void actionPerformed(ActionEvent e) {
		if (teacher) {
			for (int i = 0; i < 8; i++) {
				if (currProf.getClasses()[i] != null) {
					if (e.getSource() == currProf.getClasses()[i].getBaseDisplay()) {
						currClass = currProf.getClasses()[i];

						dashboard.removeAll();
						currClass.goDash();
						dashboard.add(currClass.getTab());

						window.revalidate();
						window.repaint();
						
					}
				}
			}
		}
		// Checking if the user is a student and if the user is a student, it is checking if the user clicked
		// on a class. If the user clicked on a class, it is setting the current class to the class that the
		// user clicked on.
		else if (student) {
			for (int i = 0; i < 8; i++) {
				if (currKid.getClasses()[i] != null) {
					if (e.getSource() == currKid.getClasses()[i].getBaseDisplay()) {
						currClass = currKid.getClasses()[i];

						dashboard.removeAll();
						currClass.goStuDash();
						dashboard.add(currClass.getTab());

						window.revalidate();
						window.repaint();
					}
				}
			}
		}

		// adds login screens based on the login selection
		if (e.getSource() == tLogin) {

			window.remove(loginChoice);
			window.add(teacherLogin);
			window.revalidate();
			window.repaint();

		} else if (e.getSource() == aLogin) {

			window.remove(loginChoice);
			window.add(studentLogin);
			window.revalidate();
			window.repaint();
		} else if (e.getSource() == submitTeacherLogin) { // edit to check login credentials
			Teacher loginner = teacherLogin();

			// if a teacher with those credentials is found, their data is added and their interface is opened
			if (loginner != null) {
				currProf = loginner;
				window.setExtendedState(JFrame.MAXIMIZED_BOTH);
				window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				window.remove(teacherLogin);
				window.revalidate();
				window.repaint();
				teacher = true;
				dashboard = new JPanel(null);

				window.add(currProf.getHeader());

				JButton b = currProf.getSem1();
				b.setBackground(Color.decode("#42a1e1"));
				dashboard.add(b);
				dashboard.add(currProf.getSem2());
				dashboard.add(currProf.getCourses(1));
				window.add(dashboard);

				window.revalidate();
				window.repaint();

			} else {
				incorrect.setText("Login information incorrect");
				teacherLogin.add(incorrect);
			}
			window.revalidate();
			window.repaint();
		} else if (e.getSource() == submitstudentLogin) {

			if (studentLogin() != null) {
				currKid = studentLogin();

				window.setExtendedState(JFrame.MAXIMIZED_BOTH);
				window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				window.remove(studentLogin);
				window.revalidate();
				window.repaint();
				student = true;
				dashboard = new JPanel(null);

				window.add(currKid.getHeader());

				JButton b = currKid.getSem1();
				b.setBackground(Color.decode("#42a1e1"));
				dashboard.add(b);
				dashboard.add(currKid.getSem2());
				dashboard.add(currKid.getCourses(1));
				window.add(dashboard);

				window.revalidate();
				window.repaint();
				System.out.println();

				
			} else {
				incorrect.setText("No student found");
				studentLogin.add(incorrect);
			}

			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("Semester 2")) {
			currProf.switchSem(2);
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("Semester 1")) {
			currProf.switchSem(1);
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("studsem1")) {
			currKid.switchSem(1);
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("studsem2")) {
			currKid.switchSem(2);
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("Close app")) {
			   
				if(currClass!=null) {
					try {

		                Class.forName("com.mysql.cj.jdbc.Driver");
		                Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);


		                Student ex = null;

		                String[][] attendance = null;
		                String[][] Marks = null;
		                String[][] weights = null;
		                String[] att = null;
		                String[] ltime = null;
		                String[] areason= null;
		                attendance = connectioncheck.downloadTableAtt("attendance_" + currClass.getCode().toLowerCase(), con);
		                
		                for (int x = 1; x < currClass.getStudents().size(); x++) {
		                    ex = currClass.getStudents().get(x-1);
		                    
		                    Attend[] test = currClass.getAttendance(ex);
		                    att = attendToatt(test);
		                    ltime = resize(attendTolatet(test));
		                    areason = resize(attendToareason(test));
		                    attendance[x][1]=arrayToString(att);
		                    attendance[x][2]=arrayToString(ltime);
		                    attendance[x][3]=arrayToString(areason);
		                   
		                }
		                
		                connectioncheck.uploadArray(attendance,con,"attendance_" + currClass.getCode().toLowerCase());

		                //updating marks
		                
		                
		                
		                Marks = new String[currClass.getStudents().size()+1][currClass.getassignments().size()+2];
		                Marks[0][0] = "Student_Id";
		                Marks[0][1] = "Midterm";
		                
		                
		                for(int i=2;i<currClass.getassignments().size()+2;i++) {
		                	Marks[0][i] = currClass.getassignments().get(i-2).getName();
		                }
		                connectioncheck.printArray(Marks);

		                
		                for (int x = 1; x < currClass.getStudents().size()+1; x++) {
		                    ex = currClass.getStudents().get(x-1);
		                    Marks[x][0] = String.valueOf(ex.getStudentNumber());
		                    Marks[x][1] = String.valueOf(currClass.getmideterms().get(ex));
		                    for(int j = 0; j<currClass.getassignments().size(); j++) {
		                    	Marks[x][2+j] = String.valueOf(currClass.getgrades().get(currClass.getassignments().get(j)).get(ex));

		                    }
		                    
		                }
		         
		                connectioncheck.uploadArray(Marks,con, currClass.getCode().toLowerCase()+"_marks");
		           

		                
		                
		                
		                
		                

		            } catch (ClassNotFoundException |

		                    SQLException e1) {
		                // TODO Auto-generated catch block
		                e1.printStackTrace();
		            }
		            // Connect to SQL Server
				}
				
			System.exit(0);
		} else if (e.getActionCommand().equals("Classes")) {

			dashboard.removeAll();
			dashboard.add(currProf.getSem1());
			dashboard.add(currProf.getSem2());
			dashboard.add(currProf.getCourses(1));
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("studclasses")) {
			dashboard.removeAll();
			dashboard.add(currKid.getSem1());
			dashboard.add(currKid.getSem2());
			dashboard.add(currKid.getCourses(1));
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("Daily Attendance")) {

			currClass.addDailys();
			currClass.changeAttDay(1);
			window.add(dashboard);

			window.revalidate();
			window.repaint();

		} // Checking if the user wants to go back to the dashboard
		else if (e.getActionCommand().equals("Dashboard")) {
			currClass.goDash();

			window.revalidate();
			window.repaint();
		} // Checking if the button that was pressed is the present button. If it is, it will set the student
		// to present.
		else if (e.getActionCommand().equals("Present")) {
			for (int i = 0; i < currClass.getStudents().size(); i++) {
				if (e.getSource() == currClass.getStudents().get(i).getPresent()) {
					Student temp = currClass.getStudents().get(i);
					currClass.setPresent(temp);
				}
			}
		}// Checking if the button is clicked and if it is, it will set the student to absent.
		 else if (e.getActionCommand().equals("Absent")) {
			for (int i = 0; i < currClass.getStudents().size(); i++) {
				if (e.getSource() == currClass.getStudents().get(i).getAbsent()) {
					Student temp = currClass.getStudents().get(i);
					currClass.setAbsent(temp);
				}
			}
		} // Checking if the button is pressed and if it is, it will add the student to the late list.
		else if (e.getActionCommand().equals("Late")) {
			for (int i = 0; i < currClass.getStudents().size(); i++) {
				if (e.getSource() == currClass.getStudents().get(i).getLate()) {
					Student temp = currClass.getStudents().get(i);
					currClass.setLate(temp);
				}
			}
		} else if (e.getActionCommand().equals("Day Choice")) {
			currClass.changeAttDay(1);
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("Submit Attendance")) {
			currClass.submitAttendance();
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("Overall Attendance")) {

			currClass.overallAtt();
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("Until day x")) {
			currClass.changeAttDay(2);
			window.revalidate();
			window.repaint();

		}

		else if (e.getActionCommand().split(" ")[0].equals("overAtt")) {
			currClass.indAtt(Integer.parseInt(e.getActionCommand().split(" ")[1]));
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("indAtt")) {
			currClass.changeAttDay(3);
			window.revalidate();
			window.repaint();

		} else if (e.getActionCommand().equals("dmarks")) {

			currClass.goMarks();
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("marksDash")) {
			currClass.studentMarks();
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().split(" ")[0].equals("studentGrades")) {
			currClass.indStudentMarks(Integer.parseInt(e.getActionCommand().split(" ")[1]));
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("back to dash")) {
			currClass.goDash();
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().split(" ")[0].equals("submitMarks")) {
			currClass.submitIndGrades(Integer.parseInt(e.getActionCommand().split(" ")[1]));
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("assDash")) {
			currClass.assMarks();
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().split(" ")[0].equals("submitAssMarks")) {
			currClass.submitAssGrades(e.getActionCommand().substring(e.getActionCommand().indexOf(" ") + 1));
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().split(" ")[0].equals("indAss")) {
			currClass.indAss(e.getActionCommand().substring(e.getActionCommand().indexOf(" ") + 1));
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("addAss")) {
			currClass.addAss();
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("removeAss")) {
			currClass.removeAss();
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("add assessment")) {
			currClass.addAssFromGUI();
			window.revalidate();
			window.repaint();
		} else if (e.getActionCommand().equals("remove assessment")) {
			currClass.removeAssFromGUI();
			window.revalidate();
			window.repaint();
		}
		else if(e.getActionCommand().equals("indstudatt")) {
			currClass.indAtt(currKid.getStudentNumber());
			window.revalidate();
			window.repaint();
		}
		else if(e.getActionCommand().equals("indstudmark")) {
			currClass.indStudentMarks(currKid.getStudentNumber());
			window.revalidate();
			window.repaint();
		}
		else if(e.getActionCommand().equals("studash")) {
			currClass.goStuDash();
			window.revalidate();
			window.repaint();
		}
		

	}

	@SuppressWarnings("rawtypes")
	public static void initialize() throws SQLException { // initializes login selection and login interface
		// Load JDBC driver
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Connect to SQL Server
			Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

			// adding teachers
			Teacher teac = null;
			Course cor = null;
			ClassCourse ourClass = null;
			String currentclass = null;
			String[][] coursez = connectioncheck.downloadTable("course", con);
			String[][] teacherinfo = connectioncheck.downloadTable("teachers", con);
			String[][] running = connectioncheck.downloadTable("running_course", con);
			String[][] stu = connectioncheck.downloadTableAtt("students", con);
			String[][] attendance = null;
			String[][] Marks;
			String[][] weights;

			for (int l = 1; l < teacherinfo.length; l++) {
				if (teacherinfo[l][1] != null) {
					String first = teacherinfo[l][1].substring(0, teacherinfo[l][1].indexOf(" "));
					String last = teacherinfo[l][1].substring(teacherinfo[l][1].indexOf(" ") + 1);
					int teacher_id = Integer.parseInt((teacherinfo[l][0]));
					LinkedList teachables = populateList(teacherinfo[l][2]);
					teac = (new Teacher(first, last, teacher_id, teachables));
					for (int i = 1; i < running.length; i++) {
						if (running[i][1] != null && Integer.parseInt(running[i][1]) == (teac.getID())) {
							currentclass = null;
							if (running[i][1] != null) {
								// Downloading the attendance, marks and weights of the current class.
								currentclass = running[i][0].toLowerCase();
								attendance = connectioncheck.downloadTableAtt("attendance_" + currentclass, con);
								Marks = connectioncheck.downloadTableAtt(currentclass + "_marks", con);
								connectioncheck.printArray(Marks);
								weights = connectioncheck.downloadTable(currentclass + "_weights", con);
								connectioncheck.printArray(weights);

								// Creating a new course object and adding it to the courseOfferings list.
								for (int k = 1; k < coursez.length; k++) {
									if (coursez[k][0] != null) {
										String Course_id = coursez[k][0];
										String coursename = coursez[k][1];
										String dpt = coursez[k][2];
										int pri = Integer.parseInt(coursez[k][3]);
										LinkedList rms = populateList(coursez[k][4]);
										cor = new Course(coursename, Course_id + "1", dpt, rms, pri, 30, 1, 15);
										courseOfferings.add(cor);
									}

									// Checking if the current class is not null and if the coursez[k][1] is not
									// null and if the
									// coursez[k][0] is equal to the current class.
									if (currentclass != null && coursez[k][1] != null
											&& coursez[k][0].toLowerCase().equals(currentclass.substring(0, 5))) {
										ourClass = new ClassCourse(1, teac, running[1][2],
												Integer.parseInt(running[1][3]), cor);
									}
								}
								// Adding the assessment to the class.

								for (int z = 1; z < weights.length - 1; z++) {

									if (weights[z][0] != null) {

										ourClass.addAssessment(weights[z][0], Double.parseDouble(weights[z][1]),
												Integer.parseInt(weights[z][2]));
										// ourClass.setGrade(ex,weights[z][0], Double.parseDouble(Marks[a][z]) );
									}
								}

								for (int j = 1; j < stu.length; j++) {
									for (int a = 1; a < attendance.length; a++) {
										// Adding the student to the class.
										if (stu[j][1] != null) {
											if (attendance[a][0] != null && attendance[a][0].equals(stu[j][0])) {
												int id = Integer.parseInt(stu[j][0]);
												String firsts = stu[j][1].substring(0, stu[j][1].indexOf(" ") + 1);
												String lasts = stu[j][1].substring(stu[j][1].indexOf(" ") + 1);
												int grade = Integer.parseInt(stu[j][2]);
												Student ex = new Student(new Hashtable<Course, Boolean>(), grade,
														firsts, lasts, id);
												String[] sch = populateArray(stu[j][4]);
												for (int q = 0; q < sch.length; q++) {
													if (currentclass.equals(sch[q].toLowerCase() + "1")) {
														ex.addClass(ourClass);
													}

												}

												ourClass.addStudent(ex);

												if (studentsearch(students, ex.getFirstName()) == false) {
													students.add(ex);

												}

												// Populating the attendance array with the attendance data from the
												// excel file.
												Attend[] test = ourClass.getAttendance(ex);
												String[] att = populateArray(attendance[a][1]);
												String[] ltime = populateArray(attendance[a][2]);
												String[] areason = populateArray(attendance[a][3]);
												test = presents(att, ltime, areason);

												ourClass.setAttendance(test, ex);
												ourClass.setMidterm(ex, Double.parseDouble(Marks[a][1]));
												for (int z = 1; z < weights.length - 1; z++) {
													if (weights[z][0] != null) {
														ourClass.setGrade(ex, weights[z][0],
																Double.parseDouble(Marks[a][z + 1]));
													}
												}
											}
										}

									}

								}
								currentClasses.add(ourClass);
								teac.addClass(ourClass, 2);
								teachers.add(teac);
							}
						}
					}

				}
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// initializing base non-specific gui components (all of the non-full-screen
		// ones)
		FlatDarkLaf.setup();

		window = new JFrame("General Student System");

		window.setSize(500, 300);
		window.setLocationRelativeTo(null);

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setResizable(false);

		loginChoice = new JPanel();
		loginChoice.setLayout(new GridLayout(2, 1));

		tLogin = new JButton("Click here for the teacher login");
		aLogin = new JButton("Click here for the student login");
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
		ID = new JTextField("Enter your teacher ID here");
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

		JTextField pass = new JTextField("Enter your student number to login");
		pass.setEditable(false);

		studentLogin = new JPanel();
		studentLogin.setLayout(new BoxLayout(studentLogin, BoxLayout.PAGE_AXIS));

		pass.setFont(new Font("Serif", Font.PLAIN, 34));
		pass.setMaximumSize(new Dimension(500, 50));

		submitstudentLogin = new JButton("Submit student login");
		submitstudentLogin.addActionListener(new School());
		submitstudentLogin.setMaximumSize(new Dimension(250, 50));
		submitstudentLogin.setFont(new Font("Arial", Font.PLAIN, 18));

		password = new JTextField("Enter your student number here");
		password.addFocusListener(new School());
		password.setMaximumSize(new Dimension(250, 30));
		password.setFont(new Font("Arial", Font.PLAIN, 15));

		incorrect = new JTextArea("");
		incorrect.setFont(new Font("Arial", Font.PLAIN, 20));
		incorrect.setAlignmentX(Component.CENTER_ALIGNMENT);
		incorrect.setMaximumSize(new Dimension(250, 30));

		studentLogin.add(pass);
		studentLogin.add(Box.createRigidArea(new Dimension(0, 20)));
		studentLogin.add(password);
		studentLogin.add(Box.createRigidArea(new Dimension(0, 10)));
		studentLogin.add(submitstudentLogin);
		submitstudentLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
		studentLogin.add(Box.createRigidArea(new Dimension(0, 20)));
	}

	/**
	 * This function takes a linked list of students and a name and returns true if
	 * the name is in the list and false otherwise
	 * 
	 * @param students LinkedList of Student objects
	 * @param name     The name of the student you want to search for.
	 * @return A boolean value.
	 */
	public static boolean studentsearch(LinkedList<Student> students, String name) {
		for (Student student : students) {
			if (student.getFirstName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public void focusGained(FocusEvent e) {

		if (teacher) {
			for (int i = 0; i < currClass.getStudents().size(); i++) {
				Student temp = currClass.getStudents().get(i);
				if (e.getSource() == temp.getMinsLate() && temp.getMinsLate().getText().equals("Time arrived")) {
					currClass.getStudents().get(i).setMinsLate("");

				}

			}
			if (currClass.getAssName().getText().equals("Assessment name")
					&& e.getComponent().equals(currClass.getAssName())) {
				currClass.getAssName().setText("");
			}
			if (currClass.getAssWeight().getText().equals("Assessment weight factor")
					&& e.getComponent().equals(currClass.getAssWeight())) {
				currClass.getAssWeight().setText("");
			}
			if (currClass.getTotalAssMarks().getText().equals("Total # of marks")
					&& e.getComponent().equals(currClass.getTotalAssMarks())) {
				currClass.getTotalAssMarks().setText("");
			}
		}

		if (lastName.getText().equals("Enter your last name here") && e.getComponent() == lastName) {
			lastName.setText("");
		}

		if (ID.getText().equals("Enter your teacher ID here") && e.getComponent() == ID) {
			ID.setText("");
		}
		if (password.getText().equals("Enter your student number here") && e.getComponent() == password) {
			password.setText("");
		}

	}

	public void focusLost(FocusEvent e) {
		if (teacher) {
			for (int i = 0; i < currClass.getStudents().size(); i++) {
				Student temp = currClass.getStudents().get(i);
				if (e.getSource() == temp.getMinsLate() && temp.getMinsLate().getText().equals("")) {
					currClass.getStudents().get(i).setMinsLate("Time arrived");
				}

			}
			if (currClass.getAssName().getText().equals("") && e.getComponent().equals(currClass.getAssName())) {
				currClass.getAssName().setText("Assessment name");
			}
			if (currClass.getAssWeight().getText().equals("") && e.getComponent().equals(currClass.getAssWeight())) {
				currClass.getAssWeight().setText("Assessment weight factor");
			}
			if (currClass.getTotalAssMarks().getText().equals("")
					&& e.getComponent().equals(currClass.getTotalAssMarks())) {
				currClass.getTotalAssMarks().setText("Total # of marks");
			}
		}

		if (lastName.getText().equals("") && e.getComponent() == lastName) {
			lastName.setText("Enter your last name here");
		}
		if (ID.getText().equals("") && e.getComponent() == ID) {
			ID.setText("Enter your teacher ID here");
		}
		if (password.getText().equals("") && e.getComponent() == password) {
			password.setText("Enter your student number here");
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

	/**
	 * It checks if the student number entered by the user matches any of the
	 * student numbers in the arraylist. If it does, it returns the student object.
	 * If it doesn't, it returns null
	 * 
	 * @return The student object is being returned.
	 */
	public Student studentLogin() {

		if (students.isEmpty()) {
			return null;
		}
		int enterNum;
		try {
			enterNum = Integer.parseInt(password.getText().strip());
		} catch (NumberFormatException e) {
			return null;
		}

		for (int i = 0; i < students.size(); i++) {

			if (students.get(i).getStudentNumber() == enterNum) {
				return students.get(i);
			}
		}
		return null;

	}

	/**
	 * It takes a string, splits it into words, and returns a linked list of those
	 * words
	 * 
	 * @param input The string to be converted to a LinkedList
	 * @return A LinkedList of Strings
	 */
	public static LinkedList<String> populateList(String input) {
		LinkedList<String> list = new LinkedList<>();
		String[] words = input.split(" ");
		for (String word : words) {
			list.add(word);
		}
		return list;
	}

	/**
	 * It takes a string, splits it into an array of strings, and returns the array
	 * 
	 * @param input The string to be split.
	 * @return The method is returning an array of strings.
	 */
	public static String[] populateArray(String input) {
		return input.split(" ");
	}

	public static String[][] removecols(String[][] array) {
		// Get number of non-null columns
		int columnCount = 0;
		for (int i = 0; i < array[0].length; i++) {
			boolean isNonNull = false;
			for (int j = 0; j < array.length; j++) {
				if (array[j][i] != null) {
					isNonNull = true;
					break;
				}
			}
			if (isNonNull) {
				columnCount++;
			}
		}

		// Create new array with the appropriate size
		String[][] newArray = new String[array.length][columnCount];

		// Copy non-null values to new array
		int newCol = 0;
		for (int oldCol = 0; oldCol < array[0].length; oldCol++) {
			boolean isNonNull = false;
			for (int row = 0; row < array.length; row++) {
				if (array[row][oldCol] != null) {
					isNonNull = true;
					break;
				}
			}
			if (isNonNull) {
				for (int row = 0; row < array.length; row++) {
					newArray[row][newCol] = array[row][oldCol];
				}
				newCol++;
			}
		}
		return newArray;
	}

	public static String[] compareAndReturnMissing(LinkedList<Assessment> list2, String[] array) {
		LinkedList<String> list = null;
		for (int i = 0; i < list2.size(); i++) {
			list.add(list2.get(i).getName());
		}
		ArrayList<String> missing = new ArrayList<>();
		for (String s : array) {
			if (!list.contains(s)) {
				missing.add(s);
			}
		}
		return missing.toArray(new String[missing.size()]);
	}

	/**
	 * It takes in three arrays of strings, and returns an array of Attend objects
	 * 
	 * @param in       an array of strings that represent the attendance status of
	 *                 each day of the month.
	 * @param late     an array of strings that contains the number of minutes late
	 *                 for each student
	 * @param abreason String[]
	 * @return An array of Attend objects
	 */
	public static Attend[] presents(String[] in, String[] late, String[] abreason) {
		boolean present = false;
		boolean late2 = false;
		int minutesLate = 0;
		int lcount = -1;
		int acount = -1;
		String reason = "";
		Attend[] att = new Attend[in.length];
		for (int i = 0; i < in.length; i++) {
			present = false;
			late2 = false;
			if (in[i].equals("0") || in[i].equals("")) {
				present = true;
				late2 = false;
				att[i] = new Attend(present, late2, minutesLate, reason);
			}

			if (in[i].equals("1")) {
				lcount++;
				present = true;
				late2 = true;
				minutesLate = Integer.parseInt(late[lcount]);

				att[i] = new Attend(present, late2, minutesLate, reason);

			}

			if (in[i].equals("2")) {
				acount++;
				switch (abreason[acount]) {
				case "1":
					reason = "Illness or injury";
					break;
				case "2":
					reason = "Appointment";
					break;
				case "3":
					reason = "Religious day";
					break;
				case "4":
					reason = "Bereavement";
					break;
				case "5":
					reason = "School transportation cancellation";
					break;
				case "6":
					reason = "Parent-approved absence";
					break;
				default:
					reason = "Not approved by parent";
					break;
				}
				att[i] = new Attend(present, late2, minutesLate, reason);

			}

		}
		return att;

	}

	public static String[] resize(String[] arr) {
		int newSize = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != null) {
				newSize++;
			}
		}
		String[] resizedArray = new String[newSize];
		int index = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != null) {
				resizedArray[index] = arr[i];
				index++;
			}
		}
		return resizedArray;
	}

	public static String[] attendToatt(Attend[] attend) {
		String[] result = new String[attend.length];
		for (int i = 0; i < attend.length; i++) {
			if (attend[i] == null) {
				continue;
			}
			if (attend[i].getPresent() && !attend[i].getLate()) {
				result[i] = "0";
			} else if (attend[i].getPresent() && attend[i].getLate()) {
				result[i] = "1";
			} else {
				result[i] = "2";
			}
		}
		return result;
	}

	public static String[] attendTolatet(Attend[] attend) {
		String[] latetime = new String[attend.length];
		for (int i = 0; i < attend.length; i++) {
			if (attend[i] == null) {
				continue;
			}
			if (attend[i].getPresent() && !attend[i].getLate()) {
			} else if (attend[i].getPresent() && attend[i].getLate()) {
				latetime[i] = Integer.toString(attend[i].getMinutesLate());
			} else {
			}
		}
		return latetime;
	}

	public static String[] attendToareason(Attend[] attend) {
		String[] areason = new String[attend.length];
		String re;
		for (int i = 0; i < attend.length; i++) {
			if (attend[i] == null) {
				continue;
			}
			if (attend[i].getPresent() && !attend[i].getLate()) {

			} else if (attend[i].getPresent() && attend[i].getLate()) {

			} else {
				re = attend[i].getReason();

				switch (re) {
				case "Illness or injury":
					areason[i] = "1";
					break;
				case "Appointment":
					areason[i] = "2";
					break;
				case "Religious day":
					areason[i] = "3";
					break;
				case "Bereavement":
					areason[i] = "4";
					break;
				case "School transportation cancellation":
					areason[i] = "5";
					break;
				case "Parent-approved absence":
					areason[i] = "6";
					break;

				case "Not approved by parent":
					areason[i] = "0";
				default:
					break;

				}
			}
		}
		return areason;
	}

	public static String arrayToString(String[] arr) {
		String x = "";
		for (String s : arr) {
			x += (s + " ");
		}
		return x.trim();
	}

	public static void main(String[] args) throws SQLException {
		initialize();
	}

}