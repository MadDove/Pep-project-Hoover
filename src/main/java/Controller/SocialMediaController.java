package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/login", this::LoginUser);
        app.post("/register", this::RegisterUser);
        app.post("/messages", this::PostNewMessage);
        app.get("/messages", this::GetAllMessages);
        app.get("/messages/{message_id}", this::GetSpecificMessage);
        app.get("/accounts/{account_id}/messages", this::GetAllAccountMessages);
        app.patch("/messages/{message_id}", this::RewriteMessage);

        return app;
    }

    private void LoginUser(Context context) {
        context.json("sample text");
    }
    private void RegisterUser(Context context) {
        context.json("sample text");
    }
    private void PostNewMessage(Context context) {
        context.json("sample text");
    }
    private void GetAllMessages(Context context) {
        context.json("sample text");
    }
    private void GetSpecificMessage(Context context) {
        context.json("sample text");
    }
    private void GetAllAccountMessages(Context context) {
        context.json("sample text");
    }
    private void RewriteMessage(Context context) {
        context.json("sample text");
    }

}