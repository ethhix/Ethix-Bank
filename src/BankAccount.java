import java.util.Random;

public class BankAccount {
    
private Random rand = new Random();

BankDatabaseConnector bankDatabaseConnector = new BankDatabaseConnector();

createAccountPageController createAccountPageController = new createAccountPageController();

private long accountNumber = 0;

    public BankAccount(createAccountPageController createAccountPageController) {
        this.createAccountPageController = createAccountPageController;
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
}
