package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.MessageService;
import Service.UserService;

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
        app.get("/accounts/{account_id}/messages", this::GetAllAccountMessages);
        app.patch("/messages/{message_id}", this::PatchRewriteMessage);

        return app;
    }

        //Check wheter a user can log in
    private void PostLoginUser(Context context) throws JsonProcessingException {
            //Create an object wrapper to read the json text into the login variable
        ObjectMapper mapper = new ObjectMapper();
        Account login = mapper.readValue(context.body(), Account.class);

            //Run the login method from accountService to return with the account from the DB
        Account account = accountService.login(login.account_id);  
        
            //If the account returns and the login password is correct, then return true
//TODO: Try and encrypt the password???
        if(account != null && login.password == account.password){
            //return true
        } else {
//TODO: Send an error message [Check which one]
            context.json("sample text");
        }
    }

        //Register a new user given everything, but the id
    private void PostRegisterUser(Context context) {
        context.json("sample text");
    }

        //Add a new message given everything, but the id
    private void PostNewMessage(Context context) {
        context.json("sample text");
    }

        //Return all messages
    private void GetAllMessages(Context context) {
        context.json("sample text");
    }

        //Get this specific message give by the id
    private void GetSpecificMessage(Context context) {
        context.json("sample text");
    }

        //Get the messages of this account by the ID
    private void GetAllAccountMessages(Context context) {
        context.json("sample text");
    }

        //Edit the body of a certain message [Find more info]
    private void PatchRewriteMessage(Context context) {
        context.json("sample text");
    }

}