package Service;

import java.util.List;

import DAO.MessageDAO;
import DAO.UserDAO;
import Model.Message;

public class MessageService{
    MessageDAO messageDAO = new MessageDAO();
    UserDAO userDAO = new UserDAO();

    public Message addMessage(Message message){
        if(messageDAO.ValidateString(message.message_text) && messageDAO.ValidateUser(message.posted_by)) {
            return messageDAO.AddMessage(message);
        } else {
            return null;
        }
    }
    public Message getMessage(int message_ID){
        Message message = messageDAO.getMessage(message_ID);
        if(message_ID == message.message_id)
            return message;
        return null;
    }
    public List<Message> getAccountMessages(int userID){
        return userDAO.getUserMessages(userID);
    }
    public Message editMessage(int message_ID, String newMessage){
        Message message = getMessage(message_ID);
        if(message !=null && message.message_id == message_ID) 
            {return messageDAO.ReWriteMessage(message_ID, newMessage);} 
        return new Message();
    }
    public List<Message> getMessages(){
        return messageDAO.GetAllMessages();
    }
    public Message DeleteMessage(int message_ID){
        if(getMessage(message_ID) == null){System.out.println("No message found"); return new Message();}
        return messageDAO.DeleteMessage(message_ID);
    }
}