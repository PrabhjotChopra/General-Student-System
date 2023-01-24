package ToolsQA.generalStudentSystemGUI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class connectioncheck {
	public static String[][] downloadTable(String tableName, Connection con) throws SQLException {
		// Execute SQL query
		Statement statement = con.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName);

		// Get metadata for the result set
		ResultSetMetaData data = rs.getMetaData();
		int columnCount = data.getColumnCount();
		// Create 2D array to store data
		int i =30;
		if (tableName.equals("teachers"))
			i = 40;
		if (tableName.equals("students"))
			i = 300;

		if (tableName.equals("course"))
			i = 100;
		
		
		String[][] infos = new String[i][columnCount]; // assuming max 100 rows
		
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
	        if ((infos[i][0]!= null)&&(infos[i][0].equals("0"))== false) {
	            for (int j = 0; j < infos[i].length; j++) {
	            	System.out.print(infos[i][j] + "\t\t");
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
	        }
	    }
	    // Create statement and execute
	}
	
	
	public static void createTable(String tableName, String[] columnTitles, Connection con) throws SQLException {
	    // Create SQL statement
	    StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + "(");
	    for (int i = 0; i < columnTitles.length; i++) {
	        sql.append(columnTitles[i] + " VARCHAR(30) ");
	        if(i==0) sql.append(" PRIMARY KEY ");
	        sql.append(" NOT NULL ");
	        if (i != columnTitles.length - 1) {
	            sql.append(",");
	        }
	    }
	    sql.append(")");
	    // Create statement and execute
	    Statement statement = con.createStatement();
	    statement.executeUpdate(sql.toString());
	}

	//cannot add a new row where the primary key is 0
	public static void addRow(String tableName, String[] values, Connection con) throws SQLException {
	    // Create SQL statement
	    StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " VALUES(");
	    for (int i = 0; i < values.length; i++) {
	        sql.append("'");
	        if (values[i] == null) {
	            sql.append("0");
	        } else {
	            sql.append(values[i]);
	        }
	        sql.append("'");
	        if (i != values.length - 1) {
	            sql.append(",");
	        }
	    }
	    sql.append(")");
	    // Create statement and execute
	    Statement statement = con.createStatement();
	    System.out.println(sql);
	    statement.executeUpdate(sql.toString());
	    
	    statement.close();
	}

	public static void addColumn(String tableName, String columnName, Connection con) throws SQLException {
	    // Create SQL statement
	    String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + "VARCHAR(30)";
	    // Create statement and execute
	    Statement statement = con.createStatement();
	    statement.executeUpdate(sql);
	    // Pad 0 into the newly added column
	    statement.executeUpdate("UPDATE " + tableName + " SET " + columnName + " = '0'");
	    statement.close();
	}
	
	
    
}
