package Controller;

import java.sql.SQLException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.*;
import Service.*;
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
    AccountService accService;    
    MessageService msgService;    
     public SocialMediaController(){
        accService = new AccountService();
        msgService = new MessageService(); 
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register",this::postAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessage);
        app.get("/messages", this::getMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteById);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesHandler);


        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */

    private void postAccountHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(),Account.class);
        Account addedAccount = accService.addAccount(account);
        if(addedAccount == null){
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginAccount = accService.loginAccount(account);
        if (loginAccount == null) {
            ctx.status(401);
        } else {
            ctx.json(mapper.writeValueAsString(loginAccount));
        }

    }

    private void postMessage(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createMessage = msgService.insertMessage(message);
        if (createMessage == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(createMessage));
        }
    }

    private void getMessageHandler(Context ctx) throws SQLException {
        ctx.json(msgService.getAllMessages());

    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException, SQLException{
        ObjectMapper mapper = new ObjectMapper();
        Message createMessage = msgService.getMessageById(ctx.pathParam("message_id"));
        if (createMessage == null) {
            ctx.json("");
        } else {
            ctx.json(mapper.writeValueAsString(createMessage));
        }
           
    }

    private void deleteById(Context ctx) throws JsonProcessingException, SQLException{
        ObjectMapper mapper = new ObjectMapper();
        Message deletedMessage = msgService.getDeletedMessage(ctx.pathParam("message_id"));
        if(deletedMessage==null){
            ctx.json("");
        }
        else{
            ctx.json(mapper.writeValueAsString(deletedMessage));
        }
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException, SQLException{
        ObjectMapper mapper = new ObjectMapper();
        
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updateMessage = msgService.getUpdatedMessage(message_id, message.message_text);
        if(updateMessage==null){
            ctx.status(400);
        }
        else{
            ctx.json(mapper.writeValueAsString(updateMessage));
        }
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException, SQLException{
        ctx.json(msgService.getAllMessagesUsingAccount(ctx.pathParam("account_id")));
    }

}