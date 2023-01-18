
    package ToolsQA.generalStudentSystemGUI;
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.ResultSet;
    import java.sql.ResultSetMetaData;
    import java.sql.SQLException;
    import java.sql.Statement;
/**
 * @author songdanny
 *
 */
public class connector {
      public static void main(String[] args) {
		//please note that this version of the branch was being tested on Danny's macbook, which will require redirect of the connect j library, in order to be ran on prabhjot's pc
	    final String USERNAME = "root";//DBMS접속 시 아이디
	    final String PASSWORD = "329228654sql";//DBMS접속 시 비밀번호
	    final String URL = "jdbc:mysql://localhost:3306/sys";//DBMS접속할 db명

            try {
                // Load JDBC driver
	            Class.forName("com.mysql.cj.jdbc.Driver");

                // Connect to SQL Server
                Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                // Execute SQL query
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM Student");

                // Get metadata for the result set
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Create 2D array to store data
                String[][] data = new String[100][columnCount]; // assuming max 100 rows

                // Populate array with data
                int row = 0;
                while (rs.next()) {
                    for (int col = 1; col <= columnCount; col++) {
                        data[row][col - 1] = rs.getString(col);
                    }
                    row++;
                }

                // Print array elements in table format
                for (int i = 0; i < columnCount; i++) {
                    System.out.print(metaData.getColumnName(i + 1) + "\t\t");
                }
                System.out.println();
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < columnCount; j++) {
                        System.out.print(data[i][j] + "\t\t");
                    }
                    System.out.println();
                }

                // Close resources
                rs.close();
                statement.close();
                con.close();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    
}
