import java.time.LocalDateTime; // used for grabbing time and date for transaction history
import java.time.format.DateTimeFormatter; // used for formatting date and time
import java.util.ArrayList; // used for transaction history
import java.util.Scanner; // used for user input

/* ATM Simulation by Carlo Dingle
 * Features:
 * - Two predefined accounts (Carlo Dingle and Sebastian Vettel)
 * - PIN authentication with 3 attempts
 * - Check balance, deposit, withdraw, transaction history
 * - Change PIN, Fast Cash, Transfer Funds
 * - Input validation and error handling
 * - Colored console output for better UX
 * - Simple loading animations
 * - Receipt generation for transactions
 */

public class ATM {
    public static void main(String[] args) throws InterruptedException {
        Scanner input = new Scanner(System.in);

        // ANSI Color codes
        final String blueBackground = "\u001B[44m";
        final String purple = "\u001B[35m";
        final String red = "\u001B[31m";
        final String green = "\u001B[32m";
        final String blue = "\u001B[36m";
        final String white = "\u001B[37m";
        final String reset = "\u001B[0m";

        // Welcome Messages (unused, but kept)
        String message = "Welcome to GridLine Bank ATM!";
        String messageString = "where every account starts on pole.";

        clearScreen();
        printWithBorder(blue + message + reset + "\n" + blue + messageString + reset, blue); 
        Thread.sleep(2000);

        // Create accounts
        Account account = new Account("Carlo Dingle", 0.0, 2007);
        Account account1 = new Account("Sebastian Vettel", 130000.0, 1987);

        // Choose account section

        boolean quitATM = false;

        OUTER:
        while (!quitATM) {
            clearScreen();
            System.out.println();
            System.out.println(blue + "=========================" + reset);
            System.out.println(white + "Choose account to log in:" + reset);
            System.out.println(white + "[1] Carlo Dingle" + reset);
            System.out.println(white + "[2] Sebastian Vettel" + reset);
            System.out.println(white + "[3] Quit ATM" + reset);
            System.out.println(blue + "=========================" + reset);
            
            int accountChoice = safeNextInt(input, "Enter choice (1-3): ", red, reset);

            Account activeAccount;
            switch (accountChoice) {
                case 3 -> {
                    quitATM = true;
                    break OUTER;
                }
                case 1 -> {
                    activeAccount = account;
                    loading(activeAccount.getName());
                }
                case 2 -> {
                    activeAccount = account1;
                    loading(activeAccount.getName());
                }
                default -> {
                    System.out.println(red + "Invalid choice. Try again. (1-3)" + reset);
                    Thread.sleep(1000);
                    continue;
                }
            }
            if (!authenticateLogin(input, activeAccount, red, green, reset)) {
                System.out.println(red + "Too many failed attempts. Returning to account selection..." + reset);
                Thread.sleep(1500);
                continue;
            }

            // Main ATM Menu Loop

            boolean logout = false;
            while (!logout) {
                clearScreen();
                System.out.println("\n" + blueBackground + "===================================");
                System.out.println("        GridLine Bank ATM          ");
                System.out.println("===================================" + reset);
                System.out.printf(purple + "Current Balance: " + reset + green + "PHP %.2f%n" + reset, activeAccount.getBalance());
                System.out.println(blue + "---- ATM Menu ----" + reset);
                System.out.println(blue + "[1] Check Balance" + reset);
                System.out.println(blue + "[2] Deposit" + reset);
                System.out.println(blue + "[3] Withdraw" + reset);
                System.out.println(blue + "[4] Transaction History" + reset);
                System.out.println(blue + "[5] Change PIN" + reset);
                System.out.println(blue + "[6] Fast Cash" + reset);
                System.out.println(blue + "[7] Transfer Funds" + reset);
                System.out.println(blue + "[8] Logout" + reset);
                System.out.println(blue + "-------------------" + reset);
                int choice = safeNextInt(input, "Please enter your choice (1-8): ", red, reset);
                switch (choice) {
                    case 1 -> checkBalance(activeAccount, blue, green, reset);
                    case 2 -> deposit(input, activeAccount, red, green, blue, reset);
                    case 3 -> withdraw(input, activeAccount, red, green, blue, reset);
                    case 4 -> transactionHistory(activeAccount, red, blue, reset);
                    case 5 -> changePin(input, activeAccount, red, green, reset);
                    case 6 -> fastCash(input, activeAccount, red, green, blue, reset);
                    case 7 -> transferFunds(input, activeAccount, account, account1, red, green, blue, reset);
                    case 8 -> {
                        System.out.println(green + "Logging out from " + activeAccount.getName() + "..." + reset);
                        Thread.sleep(1000);
                        logout = true;
                    }
                    default -> System.out.println(red + "Invalid choice. Try again. (1-8)" + reset);
                }

                if (!logout) {
                    boolean anotherTx = safeYesNo(input, "\nWould you like another transaction? (Y/N): ", red, reset);
                    if (!anotherTx) {
                        System.out.println(green + "Logging out from " + activeAccount.getName() + "..." + reset);
                        Thread.sleep(1000);
                        logout = true;
                    }
                }
            }
        }

        clearScreen();
        printWithBorder("\u001B[36mThank you for using GridLine Bank. Goodbye!\u001B[0m", "\u001B[36m");
        String buffer = "|/-\\";
        for (int i = 0; i < 100; i++) {
            System.out.print("\r" + " Exiting... " + buffer.charAt(i % buffer.length()));
            try {
                Thread.sleep(40); // Simulate work
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        input.close();
    }

    // --- Method Definitions ---

    // Simple loading animation
    private static void loading(String accountName) throws InterruptedException {
        System.out.print("Loading account " + accountName);
        for (int i = 0; i < 5; i++) { // 5 dots
            Thread.sleep(400); // 0.4 second delay
            System.out.print(".");
        }
        System.out.println(); // move to next line after loading
    }

    // Overloaded version without color
    public static void printWithBorder(String text) {
        printWithBorder(text, "\u001B[37m"); // default white
    }

    // prints texts with border with color
    public static void printWithBorder(String text, String color) {
        String reset = "\u001B[0m";

        // Split text into multiple lines
        String[] lines = text.split("\n");

        // Find the longest line to size the border correctly
        int maxLength = 0;
        for (String line : lines) {
            int plainLength = line.replaceAll("\u001B\\[[;\\d]*m", "").length(); // ignore color codes
            if (plainLength > maxLength) {
                maxLength = plainLength;
            }
        }

        // Create the border line
        String border = color + "+" + "=".repeat(maxLength + 5) + "+" + reset;
        System.out.println(border);

        // Print each line with padding
        for (String line : lines) {
            int plainLength = line.replaceAll("\u001B\\[[;\\d]*m", "").length();
            int padding = maxLength - plainLength;
            System.out.println(color + "|   " + reset + line + " ".repeat(padding) + color + "  |" + reset);
        }

        System.out.println(border);
    }

    // Clear Screen Method (clears the console for better readability)
    private static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Ignore errors
        }
    }

    // PIN authentication for logging in (uses safeNextInt for consistency)
    private static boolean authenticateLogin(Scanner input, Account account, String red, String green, String reset) throws InterruptedException {
        int attempts = 0;
        while (attempts < 3) {
            int enteredPin = safeNextInt(input, "Enter your PIN: ", red, reset);
            if (enteredPin == account.getPin()) {
                System.out.println(green + "Login successful." + reset);
                System.out.print("Loading");
                for (int i = 0; i < 3; i++) {
                    Thread.sleep(400); // 0.4 second delay
                    System.out.print(".");
                }
                System.out.println();
                return true;
            } else {
                System.out.println(red + "Incorrect PIN." + reset);
                attempts++;
            }
        }
        System.out.println(red + "Too many failed attempts." + reset);
        return false;
    }

    // Reusable PIN authentication method
    private static boolean authenticatePin(Scanner input, Account account, String red, String reset) {
        int attempts = 0;
        while (attempts < 3) {
            int enteredPin = safeNextInt(input, "Re-enter your PIN: ", red, reset);
            if (enteredPin == account.getPin()) {
                return true;
            } else {
                System.out.println(red + "Incorrect PIN." + reset);
                attempts++;
            }
        }
        System.out.println(red + "Too many failed attempts." + reset);
        return false;
    }

    // Check Balance
    private static void checkBalance(Account account, String blue, String green, String reset) throws InterruptedException {
        clearScreen();
        System.out.printf(blue + "----- Current Balance -----%n" + reset);
        System.out.printf(green + account.showBalance() + "%n" + reset);
        System.out.printf(blue + "---------------------------%n" + reset);
        System.out.print("Loading");
                for (int i = 0; i < 4; i++) {
                    Thread.sleep(400); // 0.4 second delay
                    System.out.print(".");
                }
        System.out.println(); 
    }

    // Deposit Money
    private static void deposit(Scanner input, Account account, String red, String green, String blue, String reset) {
        clearScreen();
        if (authenticatePin(input, account, red, reset)) {
            double amount = safeNextDouble(input, "Enter amount to deposit: ", red, reset);
            account.deposit(amount);

            // Add date and time to transaction history
            String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
            account.addTransaction(String.format("Deposited: +PHP %.2f | Date: %s", amount, dateTime));

            // Display success
            System.out.println(green + "Deposit successful." + reset);
            System.out.printf("You deposited: %sPHP %.2f%s%n", green, amount, reset);

            // Receipt
            boolean printReceipt = safeYesNo(input, "Print receipt? (Y/N): ", red, reset);
            if (printReceipt) {
                System.out.println("\n----- Deposit Receipt -----");
                System.out.printf("Amount: PHP %.2f%n", amount);
                System.out.printf("Date/Time: %s%n", dateTime);
                System.out.printf("New Balance: %sPHP %.2f%s%n", green, account.getBalance(), reset);
                System.out.println("--------------------------");
            }
        } else {
            System.out.println(red + "Authentication failed. Returning to menu." + reset);
        }
    }

    // Withdraw Money (fixed logic, indentation, and braces)
    private static void withdraw(Scanner input, Account account, String red, String green, String blue, String reset) {
        clearScreen();
        if (authenticatePin(input, account, red, reset)) {
            double amount = safeNextDouble(input, "Enter amount to withdraw: ", red, reset);
            if (amount % 100 != 0) {
                System.out.println(red + "Invalid amount. Must be a positive multiple of 100." + reset);
                return;
            }
            if (account.withdraw(amount)) {
                String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
                account.addTransaction(String.format("Withdrew: -PHP %.2f | Date: %s", amount, dateTime));

                System.out.println(green + "Withdrawal successful." + reset);
                System.out.printf("%sYou withdrew: PHP %.2f%s%n", green, amount, reset);

                boolean printReceipt = safeYesNo(input, "Print receipt? (Y/N): ", red, reset);
                if (printReceipt) {
                    System.out.println("\n----- Withdrawal Receipt -----");
                    System.out.printf("Amount: PHP %.2f%n", amount);
                    System.out.printf("Date/Time: %s%n", dateTime);
                    System.out.printf("New Balance: %sPHP %.2f%s%n", green, account.getBalance(), reset);
                    System.out.println("-----------------------------");
                }
            } else {
                System.out.println(red + "Insufficient balance." + reset);
            }
        } else {
            System.out.println(red + "Authentication failed. Returning to menu." + reset);
        }
    }

    // Transaction History
    private static void transactionHistory(Account account, String red, String blue, String reset) throws InterruptedException {
        clearScreen();
        ArrayList<String> history = account.getTransactionHistory();
        if (history.isEmpty()) {
            System.out.println(red + "No transactions yet." + reset);
        } else {
            System.out.println(blue + "Transaction History:" + reset);
            for (String record : history) {
                System.out.println(record);
            }
        }
        Thread.sleep(2000);  // Pause to read
    }

    // Change PIN
    private static void changePin(Scanner input, Account account, String red, String green, String reset) {
        clearScreen();
        int changePinAttempts = 0;
        boolean changePinAuthenticated = false;
        while (changePinAttempts < 3) {
            int changePin = safeNextInt(input, "Please re-enter your PIN to change it: ", red, reset);
            if (changePin == account.getPin()) {
                changePinAuthenticated = true;
                break;
            } else {
                System.out.println(red + "Incorrect PIN." + reset);
                changePinAttempts++;
            }
        }

        if (changePinAuthenticated) {
            boolean pinValid = false;
            while (!pinValid) {
                  
                    int newPin = safeNextInt(input, "Enter new PIN: ", red, reset);
                    int confirmPin = safeNextInt(input, "Confirm new PIN: ", red, reset);
                    if (newPin == confirmPin) {
                        account.setPin(newPin);  // This may throw IllegalArgumentException
                        System.out.println(green + "PIN successfully changed." + reset);
                        pinValid = true;  // Exit loop
                    } else {
                        System.out.println(red + "New PIN and confirmation do not match. Try again." + reset);
                    }
            }
        } else {
            System.out.println(red + "Too many incorrect PIN attempts. PIN change cancelled." + reset);
        }
    }

    // Fast Cash
    private static void fastCash(Scanner input, Account account, String red, String green, String blue, String reset) throws InterruptedException {
        clearScreen();
        int fastCashAttempts = 0;
        boolean fastCashAuthenticated = false;
        while (fastCashAttempts < 3) {
            int fastCashPin = safeNextInt(input, "Please re-enter your PIN for Fast Cash: ", red, reset);
            if (fastCashPin == account.getPin()) {
                fastCashAuthenticated = true;
                break;
            } else {
                System.out.println(red + "Incorrect PIN." + reset);
                fastCashAttempts++;
            }
        }
        if (fastCashAuthenticated) {
            System.out.println("Fast Cash Options:");
            System.out.printf(green + "[1] PHP %.2f%n" + reset, 100.00);
            System.out.printf(green + "[2] PHP %.2f%n" + reset, 500.00);
            System.out.printf(green + "[3] PHP %.2f%n" + reset, 1000.00);
            System.out.printf(green + "[4] PHP %.2f%n" + reset, 2000.00);
            System.out.println(blue + "[5] Cancel Fast Cash" + reset);
            int fastChoice = safeNextInt(input, "Choose an option (1-5): ", red, reset);
            if (fastChoice == 5) {
                System.out.println(red + "Fast Cash cancelled. Returning to menu." + reset);
                Thread.sleep(2000);
                return;
            }
            double fastAmount = 0.0;

            switch (fastChoice) {
                case 1 -> fastAmount = 100.0;
                case 2 -> fastAmount = 500.0;
                case 3 -> fastAmount = 1000.0;
                case 4 -> fastAmount = 2000.0;
                default -> {
                    System.out.println(red + "Invalid Fast Cash option. (1-4)" + reset);
                    return;
                }
            }
            if (fastAmount <= account.getBalance()) {
                String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
                account.addTransaction(String.format("Withdrew (Fast Cash): -PHP %.2f | Date: %s", fastAmount, dateTime));
                account.withdraw(fastAmount);
                System.out.printf("%sYou have successfully withdrawn amount of: PHP %.2f%s%n", green, fastAmount, reset);

                // Receipt generation
                boolean printReceipt = safeYesNo(input, "\nWould you like a receipt? (Y/N): ", red, reset);
                if (printReceipt) {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
                    String formattedDateTime = now.format(formatter);

                    System.out.println("\n----- Fast Cash Receipt -----");
                    System.out.printf("Date & Time: %s%n", formattedDateTime);
                    System.out.printf("Transaction Type: %s%n", "Fast Cash");
                    System.out.printf("Withdrew Amount: " + green + "PHP %.2f" + reset + "%n", fastAmount);
                    System.out.printf("New Balance: " + green + "PHP %.2f" + reset + "%n", account.getBalance());
                    System.out.println("--------------------------------");
                }
            } else {
                System.out.println(red + "Insufficient Balance for Fast Cash." + reset);
            }
        } else {
            System.out.println(red + "Incorrect PIN. Fast Cash cancelled." + reset);
        }
    }

        // Transfer Funds
    private static void transferFunds(Scanner input, Account activeAccount, Account account, Account account1, String red, String green, String blue, String reset) throws InterruptedException {
        clearScreen();
        
        // Step 1: Re-authenticate
        int attempts = 0;
        boolean authenticated = false;
        while (attempts < 3) {
            int enteredPin = safeNextInt(input, "Please re-enter your PIN: ", red, reset);
            if (enteredPin == activeAccount.getPin()) {
                authenticated = true;
                break;
            } else {
                System.out.println(red + "Incorrect PIN." + reset);
                attempts++;
            }
        }

        if (!authenticated) {
            System.out.println(red + "Too many failed attempts. Returning to menu..." + reset);
            Thread.sleep(1500);
            return;
        }

        clearScreen();

        // Step 2: Identify sender and receiver automatically
        Account targetAccount;
        switch (activeAccount.getName()) {
            case "Carlo Dingle" -> {
                targetAccount = account1; // Send to Vettel
                System.out.println(blue + "Transferring from Carlo Dingle → Sebastian Vettel" + reset);
            }
            case "Sebastian Vettel" -> {
                targetAccount = account; // Send to Carlo
                System.out.println(blue + "Transferring from Sebastian Vettel → Carlo Dingle" + reset);
            }
            default -> {
                System.out.println(red + "Unknown account." + reset);
                return;
            }
        }

        // Step 3: Ask amount
        double amount = safeNextDouble(input, blue + "Enter amount to transfer: " + green + "PHP " + reset, red, reset);

        // Step 4: Validate and perform transfer
        if (amount > activeAccount.getBalance()) {
            System.out.println(red + "Insufficient balance!" + reset);
            return;
        }

        // Step 5: Transfer funds
        activeAccount.setBalance(activeAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        // Step 6: Log transaction history (with date for consistency)
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        activeAccount.addTransaction(String.format("Transferred: -PHP %.2f to %s | Date: %s", amount, targetAccount.getName(), dateTime));
        targetAccount.addTransaction(String.format("Received: +PHP %.2f from %s | Date: %s", amount, activeAccount.getName(), dateTime));

        // Step 7: Successful transfer mini statement / receipt
        clearScreen();
        System.out.println("----- Transfer Successful! -----");
        System.out.printf("You transferred: %sPHP%s %.2f%n", green, reset, amount);
        System.out.println("From: " + activeAccount.getName());
        System.out.println("To: " + targetAccount.getName());
        System.out.printf("Your new balance: %sPHP%s %.2f%n", green, reset, activeAccount.getBalance());
        System.out.println("--------------------------------");
        Thread.sleep(2000);  // Pause to read
    }

    // Helper to safely read an integer (e.g., for PIN or menu choices)
    private static int safeNextInt(Scanner input, String prompt, String red, String reset) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println(red + "No input provided. Please enter a number." + reset);
                continue;
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println(red + "Invalid input. Please enter a valid integer (e.g., for PIN or menu)." + reset);
            }
        }
    }

    // Helper to safely read a double (e.g., for amounts)
    private static double safeNextDouble(Scanner input, String prompt, String red, String reset) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println(red + "No input provided. Please enter an amount." + reset);
                continue;
            }
            try {
                double value = Double.parseDouble(line);
                if (value <= 0) { 
                    System.out.println(red + "Amount must be positive." + reset);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println(red + "Invalid input. Please enter a valid number (e.g., 100.50)." + reset);
            }
        }
    }

    // Helper for Y/N choices (simple string, no parsing needed)
    private static boolean safeYesNo(Scanner input, String prompt, String red, String reset) {
        while (true) {
            System.out.print(prompt);
            String response = input.nextLine().trim().toUpperCase();  // Read and normalize
            switch (response) {
                case "Y", "YES" -> {
                    return true;
                }
                case "N", "NO" -> {
                    return false;
                }
                default -> System.out.println(red + "Please enter Y (yes) or N (no)." + reset);
            }
        }
    }
}