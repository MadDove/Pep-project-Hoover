package DAO;

import java.util.List;
import java.util.ArrayList;
import Model.Account;
import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;

public class UserDAO {
    
        //Retrieve user by ID
    public Account getUser(int userId){
        Connection connection = ConnectionUtil.getConnection();
        Account user = new Account(); //get connection and create the answer variable
        
        try {
            String sql = "Select * FROM account WHERE account.account_id = ?"; //Select all columns from the account table, where accountId = userId

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);  
            ResultSet rs = preparedStatement.executeQuery(); //Create fill and run a prepared statement

            while(rs.next()){ //Sets the user to the retrieved data and returns
                user = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"), 
                    rs.getString("password"));
                return user;
            }
            
        }catch(SQLException e){ //If it fails, run the standard sqlexception
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public Account getUser(String username){
        Connection connection = ConnectionUtil.getConnection();
        Account user = new Account(); //get connection and create the answer variable
        
        try {
            String sql = "Select * FROM account WHERE account.username = ?"; //Select all columns from the account table, where accountId = userId

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);  
            ResultSet rs = preparedStatement.executeQuery(); //Create fill and run a prepared statement

            System.out.println("Prepping statement, Executed");

            while(rs.next()){ //Sets the user to the retrieved data and returns
                user = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"), 
                    rs.getString("password"));
                System.out.println("Getting user");
                return user;
            }
        } catch(SQLException e){ System.out.println(e.getMessage());}//If it fails, run the standard sqlexception

        return null;
    }

    public Account addUser(Account newUser){
        if(ValidateUser(newUser)){
                //Retrieve connection to database
            Connection connection = ConnectionUtil.getConnection();
                //Instatntiate the user variable
            Account user = new Account();
                //Attempt to do sql
            try {

                String sql = "INSERT INTO account(username, password) VALUES(?, ?)";
                    //Create and fill a prepared sql statement
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, newUser.username);
                preparedStatement.setString(2, newUser.password);
            
                    //Run the prepared statement, returning a result set which should only have the generated key 
                preparedStatement.executeUpdate();
                ResultSet rs = preparedStatement.getGeneratedKeys();
                    //Add the account id and other statements to user
                while(rs.next()){
                    System.out.println("Getting generated Id");
                    int generated_account_id = (int) rs.getLong(1);
                    System.out.print("Id = ");
                    System.out.println(generated_account_id);
                    user = new Account(generated_account_id, newUser.username, newUser.password);
                    return user;
                }
                //If it fails, run the standard sqlexception
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }            
        } 
        return null;//If it fails validation, return null
    }
    
    public boolean ValidateUser(Account account){
            //The password must be 4 or more characters
        if(account.password.length() < 4) {
            System.out.println("Password Invalid");
            return false;
        } 
            //The password cannot contain spaces (to prevent sql injection)
        if(account.password.contains(" ")){
            System.out.println("Password Invalid");
            return false;
        }
            //The username is not blank nor contains spaces
        if(account.username == "" || account.username == " " || account.username.contains(" ")){
            System.out.println("Username Invalid");
            return false;
        }
        if(getUser(account.username) == null) {
            System.out.println("All clear");
            return true;
        }
        System.out.println("Username In Use");
        return false;
        
    }

    public List<Message> getUserMessages(int userID){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> message = new ArrayList<>(); //get connection and create the answer variable
        
        try {
            String sql = "Select * FROM message WHERE posted_by = ?"; //Select all columns from the account table, where accountId = userId

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userID);
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
    
}
