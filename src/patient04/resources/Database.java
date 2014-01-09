package patient04.resources;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Wilco
 */
public class Database {
    
    public Database() {
        // Create database if it does not exist        
        try {
            Class.forName("org.sqlite.JDBC");
            
            Connection c = DriverManager.getConnection("jdbc:sqlite:scores.db");
            
            Statement s = c.createStatement();
            
            s.executeUpdate("CREATE TABLE IF NOT EXISTS "
                          + "highscore (playerName STRING, time FLOAT);");
            
            s.close(); c.close();
        } catch(Exception e) { e.printStackTrace(); }
    }
    
    public void addTime(String name, float time) {     
        try {
            Connection c = DriverManager.getConnection("jdbc:sqlite:scores.db");
            
            PreparedStatement s = c.prepareStatement(
                    "INSERT INTO highscore (playerName, time) VALUES (?, ?);");
            s.setString(1, name);
            s.setFloat(2, time);
            s.executeUpdate();
            
            s.close(); c.close();
        } catch(SQLException e) { e.printStackTrace(); }
    }
    
    public ArrayList<String> getTopTimes() {
        ArrayList<String> times = new ArrayList<>();
        
        try {
            Connection c = DriverManager.getConnection("jdbc:sqlite:scores.db");
            
            Statement s = c.createStatement();
            
            ResultSet r = s.executeQuery(
                        "SELECT * FROM highscore ORDER BY time ASC LIMIT 5;");
            
            while (r.next())
                times.add(r.getString("playerName")+ ": " + r.getFloat("time"));
            
            r.close(); s.close(); c.close();
        } catch(Exception e) { e.printStackTrace(); }
        
        return times;
    }
    
}
