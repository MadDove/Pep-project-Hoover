package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService{
    MessageDAO messageDAO = new MessageDAO();

    public Message addMessage(Message message){
        return messageDAO.AddMessage(message);
    }
    public Message getMessage(int message_ID){
        return messageDAO.getMessage(message_ID);
    }
    public List<Message> getAccountMessages(int userID){
        return messageDAO.getUserMessages(userID);
    }
    public Message editMessage(int message_ID, String newMessage){
        Message message = getMessage(message_ID);
        if(message != null && messageDAO.ValidateString(newMessage)) {//We need to make sure the message exists and the new message is valid
            return messageDAO.ReWriteMessage(message_ID, newMessage);
        } 
        return null;
    }
    public List<Message> getMessages(){
        return messageDAO.GetAllMessages();
    }
    public Message DeleteMessage(int message_ID){
        if(getMessage(message_ID) != null){
            return messageDAO.DeleteMessage(message_ID); //To delete the message we must make sure it exists, if it doesn't null it out.
        } else {
            return null;
        }
    }
}