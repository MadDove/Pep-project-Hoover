package DAO;

import java.util.List;
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
            String sql = "Select * FROM message WHERE message.message_id = ?"; //Select all columns from the message table, where message = message_id

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_ID);  
            ResultSet rs = preparedStatement.executeQuery(); //Create fill and run a prepared statement


            while(rs.next()){ //Sets the message to the retrieved data and returns
                message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch(SQLException e){ System.out.println(e.getMessage());}//If it fails, run the standard sqlexception

        return null;
    }
    
    
    public List<Message> GetAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> message = new ArrayList<>(); //get connection and create the returning list
        
        try {
            String sql = "Select * FROM message"; //Select all columns

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(); //Create, fill, and run the prepared statement


            while(rs.next()){ //Sets the message to the retrieved data and returns
                message.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")));
            } 
        } catch(SQLException e){ System.out.println(e.getMessage());}//If it fails, run the standard sqlexception
        if(message.isEmpty()) {return null;} else {return message;}
    }
    
    
    public Message ReWriteMessage(int message_ID, String messageUpdate){
        if(ValidateString(messageUpdate)){
            Connection connection = ConnectionUtil.getConnection(); //get connection
        
            try {
                String sql = "UPDATE message SET message_text = ? WHERE message.message_id = ?"; //Its sql, where I rewrite the message.

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(2, message_ID);  
                preparedStatement.setString(1, messageUpdate);
                preparedStatement.executeUpdate(); //Create fill and run a prepared statement

            } catch(SQLException e){ System.out.println(e.getMessage());}//If it fails, run the standard sqlexception
        }
        return getMessage(message_ID); //As we validate it exist in MessageService, this will always return a message, though in this case it may not strictly be necessary with smarter construction.
    }

    public List<Message> getUserMessages(int userID){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> message = new ArrayList<>(); //get connection and create the answer variable
        
        try {
            String sql = "Select * FROM message WHERE posted_by = ?"; //Select all columns from the message table posted by this user

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userID);
            ResultSet rs = preparedStatement.executeQuery(); //Create fill and run a prepared statement


            while(rs.next()){ //Sets the messages to the retrieved data
                message.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")));
            }
        } catch(SQLException e){ System.out.println(e.getMessage());}//If it fails, run the standard sqlexception
        if(message.isEmpty()){return null;} else {return message;} //Return null if there is nothing, and return the message if there is something
    }
    
    
    public Message DeleteMessage(int message_ID){
        Connection connection = ConnectionUtil.getConnection();
        Message message = getMessage(message_ID); //get connection and copy the message
        
        try {
            String sql = "DELETE FROM message WHERE message.message_id = ?"; //Delete the message that matches the ID

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_ID);  
            preparedStatement.executeUpdate(); //Create fill and run a prepared statement

        } catch(SQLException e){ System.out.println(e.getMessage());}//If it fails, run the standard sqlexception

        return message; //As we check the validity of the message existing in Message service, this will always return the message.
    }
    
    public Message AddMessage(Message newMessage){
        if(ValidateString(newMessage.message_text) && ValidateUser(newMessage.posted_by)){//Validate both the message and the user
            Connection connection = ConnectionUtil.getConnection();//Get connection

            try {
                String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)"; //Insert into the tables, the values given

                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, newMessage.posted_by);
                preparedStatement.setString(2, newMessage.message_text);
                preparedStatement.setLong(3, newMessage.time_posted_epoch);
                preparedStatement.executeUpdate(); //Create fill and run a prepared statement
                
                ResultSet rs = preparedStatement.getGeneratedKeys(); //Get the keys

                rs.next(); 
                int generated_message_id = (int) rs.getLong(1); //Retrieve the key
                newMessage.setMessage_id(generated_message_id);//Set and return the message
                return newMessage;
                
            } catch(SQLException e){ System.out.println(e.getMessage());}//If it fails, run the standard sqlexception
        }
        return null; //Else return null
    }
    
    //I need to validate the message to ensure it is correct
    public boolean ValidateString(String message){ 
            if(message.length() < 255 && message != "" && message != " ")
                {return true;} //If length is <255 and not empty return true
            return false; //Else false
    }
    //I need to validate the user to see if they exist
    public boolean ValidateUser(int userId){
        if(userDAO.getUser(userId)!= null) {return true;} //If they exist, return true
        return false; // Else false
    }
}
