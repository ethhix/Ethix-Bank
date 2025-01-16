import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class TransactionService {

    createAccountPageController createAccountPageController = new createAccountPageController();

    public List<Transaction> getTransactionsByCustomerID(int customerID) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT transactionID, transactionTitle, customerID, transactionDate, amount, transactionType FROM transactions WHERE customerID = ?";

        try (Connection conn = BankDatabaseConnector.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerID);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                String transactionTitle = resultSet.getString("transactionTitle");
                java.sql.Date sqlDate = resultSet.getDate("transactionDate");
                LocalDate transactionDate = sqlDate.toLocalDate();
                double amount = resultSet.getDouble("amount");
                String transactionType = resultSet.getString("transactionType");

                Transaction transaction = new Transaction(customerID, transactionTitle, amount,
                        transactionType, transactionDate);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        }
        return transactions;
    }

    public void insertTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (transactionTitle, amount, transactionType, transactionDate, customerID) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = BankDatabaseConnector.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, transaction.getTransactionTitle());
            pstmt.setDouble(2, transaction.getAmount());
            pstmt.setString(3, transaction.getTransactionType());
            pstmt.setDate(4, java.sql.Date.valueOf(transaction.getTransactionDate()));

            pstmt.setInt(5, transaction.getCustomerID());

            pstmt.executeUpdate();
        }
    }
}
