package DAO;

import java.util.List;
import Model.Account;
import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;

public class MessageDAO {
    UserDAO userDAO = new UserDAO();

    public Message getMessage(int message_ID){
        Connection connection = ConnectionUtil.getConnection();
        Message message = new Message(); //get connection and create the answer variable
        
        try {
            String sql = "Select * FROM message WHERE message.message_id = ?"; //Select all columns from the account table, where accountId = userId

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_ID);  
            ResultSet rs = preparedStatement.executeQuery(); //Create fill and run a prepared statement


            while(rs.next()){ //Sets the user to the retrieved data and returns
                message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                
            }
        } catch(SQLException e){ System.out.println(e.getMessage());}//If it fails, run the standard sqlexception

        return message;
    }
    
    
    public List<Message> GetAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> message = new ArrayList<>(); //get connection and create the answer variable
        
        try {
            String sql = "Select * FROM message"; //Select all columns from the account table, where accountId = userId

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(); //Create fill and run a prepared statement


            while(rs.next()){ //Sets the user to the retrieved data and returns
                message.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")));
            } 
        } catch(SQLException e){ System.out.println(e.getMessage());}//If it fails, run the standard sqlexception

        return message;
    }
    
    
    public Message ReWriteMessage(int message_ID, String messageUpdate){
        if(ValidateString(messageUpdate)){
            Connection connection = ConnectionUtil.getConnection(); //get connection
        
            try {
                String sql = "UPDATE message SET message_text = ? WHERE message.message_id = ?"; //Select all columns from the account table, where accountId = userId

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(2, message_ID);  
                preparedStatement.setString(1, messageUpdate);
                preparedStatement.executeUpdate(); //Create fill and run a prepared statement

                return getMessage(message_ID);
            } catch(SQLException e){ System.out.println(e.getMessage());}//If it fails, run the standard sqlexception
        }
        return null;
    }
    
    
    public Message DeleteMessage(int message_ID){
        Connection connection = ConnectionUtil.getConnection();
        Message message = getMessage(message_ID); //get connection and create the answer variable
        
        try {
            String sql = "DELETE FROM message WHERE message.message_id = ?"; //Select all columns from the account table, where accountId = userId

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_ID);  
            preparedStatement.executeQuery(); //Create fill and run a prepared statement

        } catch(SQLException e){ System.out.println(e.getMessage());}//If it fails, run the standard sqlexception

        return message;
    }
    
    public Message AddMessage(Message newMessage){
        if(ValidateString(newMessage.message_text) && ValidateUser(newMessage.posted_by)){
            Connection connection = ConnectionUtil.getConnection();
            Message message = new Message(); //get connection and create the answer variable
        
            try {
                String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)"; //Select all columns from the account table, where accountId = userId

                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, newMessage.posted_by);
                preparedStatement.setString(2, newMessage.message_text);
                preparedStatement.setLong(3, newMessage.time_posted_epoch);
                
                preparedStatement.executeUpdate(); //Create fill and run a prepared statement
                ResultSet rs = preparedStatement.getGeneratedKeys(); //Get the keys

                while(rs.next()){ //Sets the user to the retrieved data and returns
                    int generated_message_id = (int) rs.getLong(1);
                    message = new Message(generated_message_id, newMessage.posted_by,
                        newMessage.message_text, newMessage.time_posted_epoch);
                    return message;
                }
            } catch(SQLException e){ System.out.println(e.getMessage());}//If it fails, run the standard sqlexception
        }
        return null;
    }
    
    public boolean ValidateString(String message){ if(message.length() < 255 && message != "" && message != " "){return true;} //If length is <255 and not empty true
        return false;
    }
    public boolean ValidateUser(int userId){
        if(userDAO.getUser(userId)!= null) {return true;}
        return false;
    }
}
