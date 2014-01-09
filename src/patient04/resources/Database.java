/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.resources;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author kajdreef
 */
public class Database {
    
    private Connection conn;
    private Statement stat;
    private PreparedStatement prepStat;
    private ResultSet rs;
    
    public Database(){
        initialize();
    }
    
    public void initialize(){
        try{
            
            // Connect to database
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
            
            stat = conn.createStatement();
            // create highscore table with attributes playerName and score only if the table doesn't allready exists
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS highscore (playerName STRING, score INT);");
            // Create levels table with attribute levelName and fileData only if the table doesn't allready exists
//            stat.executeUpdate("CREATE TABLE IF NOT EXISTS levels (levelName STRING UNIQUE, fileData BLOB);");
        }
        catch(ClassNotFoundException | SQLException e) {
        }
    }
        
    public void addScore(int score, String name){
        try {
            // create prepared Statement to add name and score to highscore
            prepStat = conn.prepareStatement("INSERT INTO highscore (playerName, score) VALUES (?, ?);");

            prepStat.setString(1, name);
            prepStat.setInt(2, score);
            
            prepStat.executeUpdate();
            prepStat.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet getScoreTable(){
        try {
            rs = stat.executeQuery("SELECT * FROM highscore ORDER BY score DESC");

            while( rs.next()){
                int score = rs.getInt("score");
                String name = rs.getString("playerName");
                System.out.println("score = " + score + ":  name = " + name);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }    
        return rs;
    }
    
    public void resetHighscoreDB(){
        try {
            stat.executeUpdate("DROP TABLE IF EXISTS highscore;");
            stat.executeUpdate("CREATE TABLE highscore (playerName STRING, score INT);"); 
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public void destroy(){
        try{
            stat.close();
            conn.close();
        } 
        catch(SQLException e) {
        }
    }
    
}
