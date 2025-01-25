package dal;

import models.Transaction;
import models.Account;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DALTransaction {

    public void addTransaction(Transaction transaction) {
        String query = "INSERT INTO transactions (amount, type, date, account_from, account_to) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            Integer accountFromId = getAccountIdByAccountNumber(transaction.getAccountFrom() != null ? transaction.getAccountFrom().getAccountNumber() : null, connection);
            Integer accountToId = getAccountIdByAccountNumber(transaction.getAccountTo() != null ? transaction.getAccountTo().getAccountNumber() : null, connection);
            
            statement.setDouble(1, transaction.getAmount());
            statement.setString(2, transaction.getType());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(transaction.getDate()));
            statement.setObject(4, accountFromId, java.sql.Types.INTEGER); 
            statement.setObject(5, accountToId, java.sql.Types.INTEGER);   
    
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private Integer getAccountIdByAccountNumber(String accountNumber, Connection connection) throws SQLException {
        if (accountNumber == null) {
            return null; 
        }
        String query = "SELECT id FROM accounts WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, accountNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        }
        return null; 
    }
    
    

    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE account_from = ? OR account_to = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, accountNumber);
            statement.setString(2, accountNumber);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                    resultSet.getString("id"), 
                    resultSet.getDouble("amount"),
                    resultSet.getString("type"),
                    resultSet.getTimestamp("date").toLocalDateTime(),
                    new Account(resultSet.getString("account_from"), 0.0, null, null),
                    new Account(resultSet.getString("account_to"), 0.0, null, null)
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
}