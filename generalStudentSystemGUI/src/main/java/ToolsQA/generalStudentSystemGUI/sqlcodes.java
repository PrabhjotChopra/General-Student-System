package ToolsQA.generalStudentSystemGUI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class sqlcodes {
	public static String[][] downloadTable(String tableName, Connection con) throws SQLException {
		// Execute SQL query
		Statement statement = con.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName);

		// Get metadata for the result set
		ResultSetMetaData data = rs.getMetaData();
		int columnCount = data.getColumnCount();

		// Create 2D array to store data
		String[][] infos = new String[100][columnCount]; // assuming max 100 rows
		
		for (int col = 1; col <= columnCount; col++) {
	        infos[0][col-1] = data.getColumnName(col);
	    }
		// Populate array with data
		int row = 1;
		while (rs.next()) {
			for (int col = 1; col <= columnCount; col++) {
				infos[row][col - 1] = rs.getString(col);
			}
			row++;
		}
		// Close result set and statement
		rs.close();
		statement.close();
		return infos;
	}
	

	public static void printArray(String[][] infos) {
	    // Count and print the number of rows with data
	    for (int i = 0; i < infos.length; i++) {
	        if (infos[i][0] != null) {
	            for (int j = 0; j < infos[i].length; j++) {
	                if (infos[i][j] != null) {
	                    System.out.print(infos[i][j] + "\t\t");
	                }
	            }
	            System.out.println();
	        }
	    }
	}

	public static void uploadArray(String[][] infos, Connection con, String tableName) throws SQLException {
	    // Create SQL statement
	    for (int i = 1; i < infos.length; i++) {
	    	StringBuilder sql = new StringBuilder();
	        if (infos[i][0] != null) {
	        	
	            sql.append("UPDATE " + tableName + " SET ");
	            for (int j = 1; j < infos[i].length; j++) {
	                sql.append(infos[0][j] + " = '" + infos[i][j] + "'");
	                if (j != infos[i].length - 1) {
	                    sql.append(", ");
	                }
	            }
	            sql.append(" WHERE (" + infos[0][0] + " = '" + (i) + "'); ");
	            Statement statement = con.createStatement();
	    	    statement.executeUpdate(sql.toString());
	    	    statement.close();
	        }
	    }
	    // Create statement and execute
	}

	public static void createTable(String tableName, String[] columnTitles, Connection con) throws SQLException {
		// Create SQL statement
		StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (");
		for (int i = 0; i < columnTitles.length; i++) {
			sql.append(columnTitles[i] + " VARCHAR(30) NOT NULL");
			if (i != columnTitles.length - 1) {
				sql.append(", ");
			}
		}
		sql.append(", PRIMARY KEY ("+ columnTitles[0] +"));");
		
		// Create statement and execute
		//CREATE TABLE `SCEM DB`.`new_table` (
		//  `Student_id` VARCHAR(30) NOT NULL,
		 // `first name` VARCHAR(30) NOT NULL,
		  //`last name` VARCHAR(30) NOT NULL,
		  //PRIMARY KEY (`Student_id`);
		Statement statement = con.createStatement();
		statement.executeUpdate(sql.toString());
		statement.close();
	}

	public static void addColumn(String tableName, String columnName, Connection con) throws SQLException {
		// Create SQL statement
		String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + "VARCHAR(30)";
		// Create statement and execute
		Statement statement = con.createStatement();
		statement.executeUpdate(sql);
		statement.close();
	}
	
	public static void main(String[] args) {
        final String USERNAME = "root";// DBMS connection username
        final String PASSWORD = "!Sjsshs1177";// DBMS connection password
        final String URL = "jdbc:mysql://localhost:3306/SCEM DB";// DBMS connection URL

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to SQL Server
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
            // Download table data into 2D array
            String[][] infos = downloadTable("Attendance_ics4u1", con);
            
            String [] info = {"Student_ID", "Name", "Attendance", "late", "reason"};
            // Print array elements in table format
            printArray(infos);
          //addColumn("stu", "Student_schesdule",con);
           // createTable ("ICS3U1_Marks", info, con);
            uploadArray(infos, con, "ICS3U1_Marks");
            // Close resources
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
