/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kajdreef
 */
public class Database {
    
    private Statement stat;
    private PreparedStatement prepStat;
    private Connection conn;
    private ResultSet rs;
    
    public Database(){
        initialize();
    }
    
    public void initialize(){
        try{
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
            System.out.println("****** Succesfull Connection ******");
            

            stat = conn.createStatement();
            // create highscore table with attributes playerName and score only if the table doesn't allready exists
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS highscore (playerName STRING, score INT);");
            // Create levels table with attribute levelName and fileData only if the table doesn't allready exists
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS levels (levelName STRING, fileData TEXT);");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addLevel(String level) throws FileNotFoundException{
        File file = new File(level);
        InputStream value = null;
        int fileLength = (int) file.length();

        FileInputStream fiStream= null;
        fiStream = new FileInputStream(file);
        
        value = (InputStream)fiStream;
        try{
            // create prepared Statement
            prepStat = conn.prepareStatement("INSERT INTO levels (levelName, fileData) VALUES (?, ?);");
            prepStat.setString(1, level);
            prepStat.setAsciiStream(2, value, fiStream.available());
         //   fiStream.
            prepStat.executeUpdate();
            prepStat.close();
            
        }catch(Exception e){
      System.err.println(e);
        }
    }
    
    public ResultSet getLevel() throws SQLException{
        rs = stat.executeQuery("SELECT levelName FROM levels ORDER BY levelName ASC");
        String levelName;
        while( rs.next()){
            levelName = rs.getString("levelName");
            System.out.println("levelName = " + levelName);
        }
        return rs;
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
    
    public ResultSet getScoreTable() throws SQLException{
        rs = stat.executeQuery("SELECT * FROM highscore ORDER BY score DESC");
        while( rs.next()){
            int score = rs.getInt("score");
            String name = rs.getString("playerName");
            System.out.println("score = " + score + ":  name = " + name);
        }
        return rs;
    }
    
    public void destroy(){
        try{
            rs.close();
            stat.close();
            conn.close();
        } 
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void resetHighscoreDB() throws SQLException{
        stat.executeUpdate("DROP TABLE IF EXISTS highscore;");
        stat.executeUpdate("CREATE TABLE highscore (playerName STRING, score INT);"); 
    }
    
    public void resetLevelDB() throws SQLException{
        stat.executeUpdate("DROP TABLE IF EXISTS levels;");
        stat.executeUpdate("CREATE TABLE levels levels (levelName STRING, fileData FILE)"); 
    }
    
}
