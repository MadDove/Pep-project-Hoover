package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.MessageService;
import Service.UserService;

import java.util.List;
import java.util.ArrayList;

import org.h2.expression.function.JsonConstructorFunction;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private MessageService messageService;
    private UserService accountService;

    public SocialMediaController(){
        this.messageService = new MessageService();
        this.accountService = new UserService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/login", this::PostLoginUser);
        app.post("/register", this::PostRegisterUser);
        app.post("/messages", this::PostNewMessage);
        app.get("/messages", this::GetAllMessages);
        app.get("/messages/{message_id}", this::GetSpecificMessage);
        app.delete("/messages/{message_id}", this::DeleteMessage);
        app.get("/accounts/{account_id}/messages", this::GetAllAccountMessages);
        app.patch("/messages/{message_id}", this::PatchRewriteMessage);

        return app;
    }


    private void PostLoginUser(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account login = mapper.readValue(context.body(), Account.class); //Create an object wrapper to read the json text into the login variable

        Account account = accountService.login(login.username);  //Run the login method from accountService to return with the account from the DB
        
//TODO: Try and encrypt the password???
        if(account != null && login.password.compareTo(account.password) == 0){ //If the account returns and the login password is correct, then return true
            context.status(200);
            context.json(mapper.writeValueAsString(account));
        } else {
//TODO: Send an error message [Check which one]
            context.status(401);
        }
    }

    private void PostRegisterUser(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account register = mapper.readValue(context.body(), Account.class); //Create an object wrapper to read the json text into the register variable

        Account account = accountService.register(register);  //Run the register method from accountService to add the new account to the db
        
        if(account != null){ //If the account returns it means it was sucessfully added to the database
            //context.status(200);
            System.out.println("Sucess in Registering");
            context.json(mapper.writeValueAsString(account));
        } else {
            context.status(400);
        } 
    }

        //These two return a json on sucess and 400 on failure NewMessage adds a message and Rewrite changes the text of a message
    private void PostNewMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class); //Create an object wrapper to read the json text into the register variable

        message = messageService.addMessage(message);  //Run the register method from accountService to add the new account to the db
        
        JsonSucess(context, message);
    }
    private void PatchRewriteMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class); //Create an object wrapper to read the json text into the register variable
        int message_ID = Integer.parseInt(context.pathParam("message_id"));

        message = messageService.editMessage(message_ID, message.message_text);  //Run the register method from accountService to add the new account to the db
        
        JsonSucess(context, message);
    }
        
        //Return all messages on success, are empty on not finding one, etc.
    private void GetAllMessages(Context context) throws JsonProcessingException  {
        List<Message> allMessages = messageService.getMessages();
        JsonAddFound(context, allMessages);
    }
    private void GetAllAccountMessages(Context context) throws JsonProcessingException {
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> accountMessages = messageService.getAccountMessages(account_id);
        JsonAddFound(context, accountMessages);
    }
        

    //These two methods take in an ID and always are 200, will return empty if the message doesn't exist, and has the json on a success
    private void DeleteMessage(Context context) throws JsonProcessingException{
        int message_ID = Integer.parseInt(context.pathParam("message_id"));

        Message message = messageService.DeleteMessage(message_ID);
        
        JsonAddFound(context, message);
    }
    private void GetSpecificMessage(Context context) throws JsonProcessingException {
        int message_ID = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessage(message_ID);
        JsonAddFound(context, message);
    }

    private void JsonSucess(Context context, Message message)throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        if(message != null){
            context.json(mapper.writeValueAsString(message));
        } else{
            context.status(400);
        }
    }
    private void JsonAddFound(Context context, Message message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if(message != null){
            context.json(mapper.writeValueAsString(message));
        }
    } 
    private void JsonAddFound(Context context, List<Message> messages) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        if(messages != null){
            context.json(mapper.writeValueAsString(messages));
        } else {
            context.json(mapper.writeValueAsString(new ArrayList<Message>()));
        }
    }

}