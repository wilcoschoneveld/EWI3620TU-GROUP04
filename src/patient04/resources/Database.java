package patient04.resources;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Kaj Dreef
 */
public class Database {
    
    /** Database constructor
     * 
     */
    public Database() {
        // Create database if it does not exist        
        try {
            // Make sure static JDBC class is loaded
            Class.forName("org.sqlite.JDBC");
            
            // Connect to database
            Connection c = DriverManager.getConnection(
                                             "jdbc:sqlite:res/scores.db");
            
            // Create a new statement
            Statement s = c.createStatement();
            
            // Execute an update statement
            s.executeUpdate("CREATE TABLE IF NOT EXISTS "
                          + "highscore (playerName STRING, time FLOAT);");
            
            // Close connection
            s.close(); c.close();
        } catch(Exception e) { e.printStackTrace(); }
    }
    
    /** Adds time to database
     * 
     * @param name
     * @param time 
     */
    public void addTime(String name, float time) {     
        try {
            // Connect to database
            Connection c = DriverManager.getConnection(
                                             "jdbc:sqlite:res/scores.db");
            
            // Prepare and executre an insert statement
            PreparedStatement s = c.prepareStatement(
                    "INSERT INTO highscore (playerName, time) VALUES (?, ?);");
            s.setString(1, name);
            s.setFloat(2, time);
            s.executeUpdate();
            
            // Close connection
            s.close(); c.close();
        } catch(SQLException e) { e.printStackTrace(); }
    }
    
    /** Get best times from database
     * 
     * @return ArrayList times
     */
    public ArrayList<String> getTopTimes() {
        ArrayList<String> times = new ArrayList<>();
        
        try {
            // Connect to database
            Connection c = DriverManager.getConnection(
                                             "jdbc:sqlite:res/scores.db");
            
            // Create a new statement
            Statement s = c.createStatement();
            
            // Execute a query
            ResultSet r = s.executeQuery(
                        "SELECT * FROM highscore ORDER BY time ASC LIMIT 5;");
            
            // Add results to arraylist
            while (r.next()) {
                String name = r.getString("playerName");
                float time = r.getFloat("time");
                times.add(name + ": " + String.format("%.2f s", time));
            }
            
            // Close connection
            r.close(); s.close(); c.close();
        } catch(Exception e) { e.printStackTrace(); }
        
        return times;
    }
    
}
