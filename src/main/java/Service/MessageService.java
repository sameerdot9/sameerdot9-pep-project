package Service;

import java.sql.SQLException;
import java.util.List;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;
    public MessageService() {
        messageDAO = new MessageDAO();}
    
        public MessageService(MessageDAO messageDAO) {
            this.messageDAO = messageDAO;
        }
    
        public Message insertMessage(Message message) throws SQLException {
            return messageDAO.createPostMessage(message);
        }
    
        public List<Message> getAllMessages() throws SQLException {
            return messageDAO.getAllMessages();
        }
    
        public Message getMessageById(String message_id) throws SQLException {
            int m = Integer.parseInt(message_id);
            return messageDAO.getMessageById(m);
        }
    
        public Message getDeletedMessage(String message) {
            int m = Integer.parseInt(message);
            return messageDAO.getDeletedMessage(m);
        }
    
        public Message getUpdatedMessage(int message_id, String message){
            if(message!="" && message.length()<=255)
               return messageDAO.getUpdatedMessage(message_id, message);
            return null;
    
        }
    
        public List<Message> getAllMessagesUsingAccount(String id){
            int sid = Integer.parseInt(id);
            return messageDAO.getAllMessagesUsingAccount(sid);
        }
}
