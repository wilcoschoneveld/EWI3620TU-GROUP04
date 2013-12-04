/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.Database;

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
            
// create statement
            stat = conn.createStatement();
            stat.executeUpdate("DROP TABLE IF EXISTS highscore;");
            stat.executeUpdate("CREATE TABLE highscore (playerName STRING, score INT);");   
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addScore(int score, String name){
        try {
            // create prepared Statement
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
        //stat2 = conn.createStatement();
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
}
