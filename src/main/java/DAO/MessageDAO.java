package DAO;

import java.util.*;
import java.sql.*;
import Model.*;
import Util.ConnectionUtil;

public class MessageDAO {
    public Message createPostMessage(Message message) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        if (message.message_text != "" && message.message_text.length() < 255) {
            try {
                String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, message.posted_by);
                preparedStatement.setString(2, message.message_text);
                preparedStatement.setLong(3, message.time_posted_epoch);

                preparedStatement.executeUpdate();
                ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
                if (pkeyResultSet.next()) {
                    int generated_flight_id = (int) pkeyResultSet.getLong(1);
                    return new Message(generated_flight_id, message.getPosted_by(), message.getMessage_text(),
                            message.getTime_posted_epoch());
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            // Write SQL logic here
            String sql = "SELECT * FROM message";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Write SQL logic here
            String sql = "SELECT * FROM message WHERE message_id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new Message(rs.getInt(1), rs.getInt(2),
                        rs.getString(3), rs.getLong(4));

            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message getDeletedMessage(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Write SQL logic here
            Message msg = getMessageById(message_id);
            String sql = "DELETE FROM message WHERE message_id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            int r = preparedStatement.executeUpdate();
            if (r > 0) {
                return new Message(msg.getMessage_id(), msg.getPosted_by(), msg.getMessage_text(),
                        msg.getTime_posted_epoch());
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message getUpdatedMessage(int mid, String message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Write SQL logic here
            String sql = "UPDATE message SET message_text=? WHERE posted_by=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, message);
            preparedStatement.setInt(2, mid);
            int r = preparedStatement.executeUpdate();
            Message m = getMessageById(mid);
            System.out.println(r);
            if (r > 0) {
                return m;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessagesUsingAccount(int id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            // Write SQL logic here
            String sql = "SELECT * FROM message WHERE posted_by=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt(1), rs.getInt(2),
                        rs.getString(3), rs.getLong(4));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
