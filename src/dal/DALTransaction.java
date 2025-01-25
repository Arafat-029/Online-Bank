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
        String query = "INSERT INTO transactions (transaction_id, amount, type, date, account_from, account_to) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, transaction.getTransactionId());
            statement.setDouble(2, transaction.getAmount());
            statement.setString(3, transaction.getType()); 
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(transaction.getDate()));
            statement.setString(5, transaction.getAccountFrom() != null ? transaction.getAccountFrom().getAccountNumber() : null);
            statement.setString(6, transaction.getAccountTo() != null ? transaction.getAccountTo().getAccountNumber() : null);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    resultSet.getString("transaction_id"), 
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