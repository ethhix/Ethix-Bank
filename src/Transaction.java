import java.time.LocalDate;

public class Transaction {
    private int transactionID;
    private int customerID;
    private String transactionTitle;
    private double amount;
    private String transactionType;
    private LocalDate transactionDate;

    public Transaction(int customerID, String transactionTitle, double amount,
            String transactionType, LocalDate transactionDate) {
        this.customerID = customerID;
        this.transactionTitle = transactionTitle;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getTransactionTitle() {
        return transactionTitle;
    }

    public double getAmount() {
        return amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionTitle(String title) {
        transactionTitle = title;
    }

    public void setAmount(double amount) {
        amount = this.amount;
    }

    public void setTransactionType(String type) {
        transactionType = type;
    }

    public void setTransactionDate(LocalDate date) {
        transactionDate = date;
    }

    public boolean isValid() {

        if (transactionTitle == null || transactionTitle.isEmpty() || transactionTitle.length() < 3) {
            System.out.println("Invalid Title: The title should be at least 3 characters long.");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Invalid Amount: The amount should be greater than 0.");
            return false;
        }

        if (transactionType == null || transactionType.isEmpty()) {
            System.out.println("Invalid Transaction Type: A valid transaction type must be selected.");
            return false;
        }

        if (transactionDate == null || transactionDate.isAfter(LocalDate.now())) {
            System.out.println("Invalid Date: The transaction date is either missing or in the future.");
            return false;
        }
        return true;
    }
}
