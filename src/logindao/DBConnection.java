package logindao;
import java.sql.*;

public class DBConnection {

    // path of database
    private static final String path = "C:/Users/Muhammad Hasan/OneDrive/Password-Manager-and-Analyzer/database/LoginCredentials.accdb";
    private static final String url = "jdbc:ucanaccess://" + path;

    // method to get connection
    public static Connection getConnection() throws SQLException{

        try{
            // load the driver (it can throw exception)
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            return DriverManager.getConnection(url);
        }
        catch (ClassNotFoundException e) {
            System.out.println("Not able to load the UCanAccess driver.");

            // as we are not able to load the driver, and this method also throws exception, so throwing that exception
            throw new SQLException("Driver not found");
        }
    }

    // test connection
    public static boolean testConnection(){
        Connection con = null;

        try{
            con = getConnection();
            if(con != null){
                System.out.println("Loaded!");
                return true;
            }
        }
        catch (SQLException e){
            System.out.println("Not loaded");
            System.out.println(e.getMessage());
            return false;
        }
        finally{
            if(con != null){
                /*
                 what if when you reach to finally block to close connection and something got happened to connection?
                 that's why try catch.
                 */
                try{
                    con.close();
                }
                catch (SQLException e) {
                    System.out.println("Something happened before closing");
                    e.printStackTrace();// the only thing I can do in this scenario

                }
            }
        }
        return false;
    }

}
