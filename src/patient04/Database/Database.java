/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS levels (levelName STRING UNIQUE, fileData BLOB);");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addLevel(String level){
        File file = new File(level);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            try{
            // create prepared Statement
            prepStat = conn.prepareStatement("INSERT INTO levels (levelName, fileData) VALUES (?, ?);");
            
            prepStat.setString(1, level);
            prepStat.setAsciiStream(2, (InputStream)inputStream, (int)file.length());

            prepStat.executeUpdate();
            prepStat.close();
            
        }catch(Exception e){
      System.err.println(e);
        }
    }
    
    public void getLevel(String level){
        try {
            prepStat = conn.prepareStatement("SELECT * FROM levels WHERE levelName = ? ORDER BY levelName ASC");
            prepStat.setString(1, level);
            rs = prepStat.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        String levelName;        
        InputStream inputStream;
        
        int read;
	byte[] bytes = new byte[1024];
        
        OutputStream outputStream = null;
        
        try {
            outputStream = new FileOutputStream( new File("loadLevel.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while( rs.next()){
                levelName = rs.getString("levelName");
                System.out.println("levelName = " + levelName);
                inputStream = rs.getBinaryStream("fileData");
                try {
                    while((read = inputStream.read(bytes))!= -1){
                        outputStream.write(bytes, 0, read);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet getAllLevels() throws SQLException, FileNotFoundException, IOException{
        rs = stat.executeQuery("SELECT * FROM levels ORDER BY levelName ASC");
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
    
    public ResultSet getScoreTable(){
        try {
            rs = stat.executeQuery("SELECT * FROM highscore ORDER BY score DESC");


            while( rs.next()){
                int score = rs.getInt("score");
                String name = rs.getString("playerName");
                System.out.println("score = " + score + ":  name = " + name);
            }
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
    
    public void resetLevelDB(){
        try {
            stat.executeUpdate("DROP TABLE IF EXISTS levels;");
            stat.executeUpdate("CREATE TABLE levels (levelName STRING UNIQUE, fileData FILE)");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
}
