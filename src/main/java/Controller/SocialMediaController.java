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

        //Add a new message given everything, but the id
    private void PostNewMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class); //Create an object wrapper to read the json text into the register variable

        message = messageService.addMessage(message);  //Run the register method from accountService to add the new account to the db
        
        if(message != null){ //If the account returns it means it was sucessfully added to the database
            //context.status(200);
            System.out.println("Success in adding message");
            context.json(mapper.writeValueAsString(message));
        } else {
            context.status(400);
        } 
    }

        //Return all messages
    private void GetAllMessages(Context context) throws JsonProcessingException  {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> allMessages = messageService.getMessages();
        context.json(mapper.writeValueAsString(allMessages));
    }

        //Get this specific message give by the id
    private void GetSpecificMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_ID = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessage(message_ID);
        if(message != null){context.json(mapper.writeValueAsString(message));};
    }

        //Get the messages of this account by the ID
    private void GetAllAccountMessages(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> accountMessages = messageService.getAccountMessages(account_id);
        context.json(mapper.writeValueAsString(accountMessages));
    }

        //Edit the body of a certain message [Find more info]
    private void PatchRewriteMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class); //Create an object wrapper to read the json text into the register variable
        int message_ID = Integer.parseInt(context.pathParam("message_id"));

        message = messageService.editMessage(message_ID, message.message_text);  //Run the register method from accountService to add the new account to the db
        
        if(message != null && message.message_id == message_ID){ //If the account returns it means it was sucessfully added to the database
            //context.status(200);
            System.out.println("Success in adding message");
            context.json(mapper.writeValueAsString(message));
        } else {
            context.status(400);
        }
    }

    private void DeleteMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_ID = Integer.parseInt(context.pathParam("message_id"));

        Message message = messageService.DeleteMessage(message_ID);
        
        if(message.message_id == message_ID){context.json(mapper.writeValueAsString(message));}
        
    }
}