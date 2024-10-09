package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class MessageDAO {

    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
           
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

       
        preparedStatement.setInt(1, message.getPosted_by());
        preparedStatement.setString(2, message.getMessage_text());
        preparedStatement.setLong(3, message.getTime_posted_epoch());

        
        preparedStatement.executeUpdate();

        // Get the generated primary key (message_id)
        ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
        if (pkeyResultSet.next()) {
            int generatedMessageId = pkeyResultSet.getInt(1);

            // Return the newly created message object with the generated message_id
            return new Message(generatedMessageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return null;
}

public List<Message> getAllMessages() {
    Connection connection = ConnectionUtil.getConnection();
    List<Message> messages = new ArrayList<>();
    try {
        String sql = "SELECT * FROM message";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int message_id = resultSet.getInt("message_id");
            int posted_by = resultSet.getInt("posted_by");
            String message_text = resultSet.getString("message_text");
            long time_posted_epoch = resultSet.getLong("time_posted_epoch");

            Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);
            messages.add(message);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return messages;
}

public Message getMessageById(int message_id) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "SELECT * FROM message WHERE message_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, message_id);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int retrievedMessageId = resultSet.getInt("message_id");
            int posted_by = resultSet.getInt("posted_by");
            String message_text = resultSet.getString("message_text");
            long time_posted_epoch = resultSet.getLong("time_posted_epoch");
            return new Message(retrievedMessageId, posted_by, message_text, time_posted_epoch);
        } else {
            return null; 
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    } 
}

public Message deleteMessageById(int message_id) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        // First, retrieve the message details before deleting it
        String selectSql = "SELECT * FROM message WHERE message_id = ?";
        PreparedStatement selectStatement = connection.prepareStatement(selectSql);
        selectStatement.setInt(1, message_id);
        
        ResultSet resultSet = selectStatement.executeQuery();
        if (resultSet.next()) {
            int retrievedMessageId = resultSet.getInt("message_id");
            int posted_by = resultSet.getInt("posted_by");
            String message_text = resultSet.getString("message_text");
            long time_posted_epoch = resultSet.getLong("time_posted_epoch");
            
            // Now delete the message
            String deleteSql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
            deleteStatement.setInt(1, message_id);
            deleteStatement.executeUpdate();  

            
            return new Message(retrievedMessageId, posted_by, message_text, time_posted_epoch);
        } else {
            return null; 
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}

public boolean updateMessage(Message message) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, message.getMessage_text());
        preparedStatement.setInt(2, message.getMessage_id());
        int rowsAffected = preparedStatement.executeUpdate(); 
        return rowsAffected > 0; 
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public List<Message> getMessagesByAccountId(int accountId) {
    Connection connection = ConnectionUtil.getConnection();
    List<Message> messages = new ArrayList<>();

    try {
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, accountId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int messageId = resultSet.getInt("message_id");
            int posted_by = resultSet.getInt("posted_by");
            String message_text = resultSet.getString("message_text");
            long time_posted_epoch = resultSet.getLong("time_posted_epoch");
            Message message = new Message(messageId, posted_by, message_text, time_posted_epoch);
            messages.add(message);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return messages;
}



}