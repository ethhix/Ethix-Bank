import java.util.Random;

public class BankAccount {
    
private Random rand = new Random();

BankDatabaseConnector bankDatabaseConnector = new BankDatabaseConnector();

createAccountPageController createAccountPageController = new createAccountPageController();
BankApplicationController bankApplicationController = new BankApplicationController();

private long accountNumber;
private String fullName;
private double balance;
private String accountType;

    public BankAccount() {
    }

    public void main(String[] args) {
        
    }

    public long createAccountNumber() {
            System.out.println("Account number for this account is:");
            accountNumber = 100_000_000L + (Math.abs(rand.nextLong()) % 900_000_000L);
            System.out.println(accountNumber);
            return accountNumber;
    }

    public String retrieveFullName() {
        return createAccountPageController.getFirstName() + " " + createAccountPageController.getLastName();
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String retrieveAccountType() {
        return createAccountPageController.getAccountType();
    }

    public String retrieveCreationDate() {
        return java.time.LocalDate.now().toString();
    }

    public double retrieveBalance() {
        return 0.00;
    }

    public long retrieveAccountNumber() {
        return accountNumber;
    }

    public void clearUserDetails() {
        fullName = null;
        accountNumber = 0;
        balance = 0.0;
        accountType = null;
    }
}
