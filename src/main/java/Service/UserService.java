package Service;

import java.util.List;

import DAO.MessageDAO;
import DAO.UserDAO;
import Model.Account;
import Model.Message;

public class UserService {
    //Initialize the DAO classes as private
    private MessageDAO messageDAO;
    private UserDAO accountDAO;

    //create initializers for new DAO's
    public UserService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new UserDAO();
    }

    public Account login(int userId){
        return accountDAO.getUser(userId);
    }  
    public Account register(Account newUser){
        return null;
    }

}
