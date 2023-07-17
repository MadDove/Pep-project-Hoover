package DAO;

import java.util.List;
import Model.Account;
import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;

public class UserDAO {
    
        //Retrieve user by ID
    public Account getUser(int userId){
            //Retrieve connection to database
        Connection connection = ConnectionUtil.getConnection();
            //Instatntiate the user variable
        Account user = new Account();
            //Attempt to do sql
        try {
                //Select all columns from the account table, where accountId = userId
            String sql = "Select * FROM account WHERE account.account_id = ?";
                //Create and fill a prepared sql statement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
                //Run the prepared statement, returning a result set    
            ResultSet rs = preparedStatement.executeQuery();
                //While the next value exists, (should be only one but lets not test it) set user equal to the next line.
            while(rs.next()){
                user = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"), 
                    rs.getString("password"));
            }
            //If it fails, run the standard sqlexception
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //Return the user
        return user;
    }


    public Account addUser(){
        return null;
    }
    public List<Message> getUserMessages(){
        return null;
    }
    public boolean ValidateUser(){
        return false;
    }
    
}
