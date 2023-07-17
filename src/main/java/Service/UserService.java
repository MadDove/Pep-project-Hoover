package Service;

import DAO.UserDAO;
import Model.Account;

public class UserService {
    //Initialize the DAO classes as private
    private UserDAO accountDAO;

    //create initializers for new DAO's
    public UserService() {
        this.accountDAO = new UserDAO();
    }

    public Account login(String username){
        return accountDAO.getUser(username);
    }  
    public Account register(Account newUser){
        return accountDAO.addUser(newUser);
    }

}
