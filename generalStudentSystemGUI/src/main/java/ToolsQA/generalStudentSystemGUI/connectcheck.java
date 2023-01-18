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
public class connectcheck {
      public static void main(String[] args) {
  	   final String USERNAME = "root";//DBMS접속 시 아이디
  	  final String PASSWORD = "329228654sql";//DBMS접속 시 비밀번호
  	   final String URL = "jdbc:mysql://localhost:3306/boarddb";//DBMS접속할 db명

            try {
                // Load JDBC driver
	            Class.forName("com.mysql.cj.jdbc.Driver");

                // Connect to SQL Server
                Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                // Execute SQL query
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM board");

                // Get metadata for the result set
                ResultSetMetaData data = rs.getMetaData();
                int columnCount = data.getColumnCount();

                // Create 2D array to store data
                String[][] infos = new String[500][columnCount]; // assuming max 100 rows

                // Populate array with data
                int row = 0;
                while (rs.next()) {
                    for (int col = 1; col <= columnCount; col++) {
                        infos[row][col - 1] = rs.getString(col);
                    }
                    row++;
                }

                // Print array elements in table format
                for (int i = 0; i < columnCount; i++) {
                    System.out.print(data.getColumnName(i + 1) + "\t\t");
                }
                System.out.println();
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < columnCount; j++) {
                        System.out.print(infos[i][j] + "\t\t");
                    }
                    System.out.println();
                }

                // Close resources
                rs.close();
                statement.close();
                conn.close();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    
}