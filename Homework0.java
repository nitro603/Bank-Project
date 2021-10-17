import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
//import java.util.Calendar;

public class Homework0{

	public static void main(String[] args) throws IOException
	{
		//variable declarations
		Bank bank = new Bank();								//instantiate the Bank
	    char choice;										//menu item selected
	    boolean notDone = true;								//loop control flag

	    // open input test cases file
	    //File input = new File("testcases.txt");
	    
	    //create Scanner object
		File input = new File("input.txt");
	    Scanner filescan = new Scanner(new FileInputStream(input));

	    Scanner kybd = new Scanner(new File("testcases.txt"));
		//Scanner kybd = new Scanner(System.in);

	    // open the output file
	    PrintWriter outFile = new PrintWriter("output.txt");
	    //PrintWriter outFile = new PrintWriter(System.out);

	    /* first part */
	    /* fill and print initial database */
	    readAccts(bank, filescan);
	    printAccts(bank, outFile);
		printAcctsByAcctNumSortKey(bank, outFile);
		printAcctsBySSNSortKey(bank, outFile);
		printAcctsByNameSortKey(bank, outFile);
	    
	    /* second part */
	    /* prompts for a transaction and then */
	    /* call functions to process the requested transaction */
		System.out.println("Welcome to the Bank system! Below is the menu selection");
	    do
	    {
	        menu();
	        choice = kybd.next().charAt(0);
	        try
	        {
	        	switch(choice)
	        	{
	            	case 'q':
	            	case 'Q':
	            		notDone = false;
	            		printAccts(bank, outFile);
						printAcctsByAcctNumSortKey(bank, outFile);
						printAcctsBySSNSortKey(bank, outFile);
						printAcctsByNameSortKey(bank, outFile);
	            		break;
	            	case 'b':
	            	case 'B':
	            		balance(bank,outFile,kybd);
	            		break;
	            	case 'd':
	            	case 'D':
	            		deposit(bank,outFile,kybd);
	            		break;
	            	case 'w':
	            	case 'W':
	            		withdrawal(bank,outFile,kybd);
	            		break;
	            	case 'c':
	            	case 'C':
	            		clearCheck(bank,outFile,kybd);
	            		break;
	            	case 'n':
	            	case 'N':
	            		newAcct(bank,outFile,kybd);
	            		break;
	            	case 's':
	            	case 'S':
	            		closeAcct(bank,outFile,kybd);
	            		break;
	            	case 'r':
	            	case 'R':
	            		reopenAcct(bank,outFile,kybd);
	            		break;
	            	case 'x':
	            	case 'X':
	            		deleteAcct(bank,outFile,kybd);
	            		break;
	            	case 'i':
	            	case 'I':
	            		acctInfo(bank,outFile,kybd);
	            		break;
	            	case 'h':
	            	case 'H':
	            		acctInfoWithTransactionHistory(bank,outFile,kybd);
	            		break;
	            	default:
	            		throw new InvalidMenuSelectionException(choice);
        		}
	        }
	        catch (InvalidMenuSelectionException ex) {
        		
        		outFile.println(ex.getMessage());
        		outFile.println();
        		outFile.flush();
	        }
	        // give user a chance to look at output before printing menu
	        //pause(kybd);
	    } while (notDone);
	    
	    //close the output file
	    outFile.close();
	    
	    //close the test cases input file
	    kybd.close();
	    
	    outFile.println();
	    outFile.println("The program is terminating");
	}

	/* Method readAccts()
	 * Input:
	 *  bank - reference to the Bank object
	 *  maxAccts - maximum number of active accounts allowed
	 * Process:
	 *  Reads the initial database of accounts
	 * Output:
	 *  Fills in the initial array of Account objects within the Bank object
	 *  and also set the inital number of active accounts (stored in the Bank object)
	 */
	public static void readAccts(Bank bank, Scanner fileScan) throws IOException{
		//loop runs until there is no more input in the input file
		while(fileScan.hasNext()){
			//first initializes through readScan into an Account object
			Account person = Account.readScan(fileScan);
			TransactionTicket ticket = new TransactionTicket(Calendar.getInstance(), "Opening new Account", 0, 0);
			
			//Then get categorized into it's specific class
			if(person.getAcctType().equals("Savings")){
				SavingsAccount object = new SavingsAccount(person);
				object.addTransaction(new TransactionReceipt(ticket, true, "none", object.getAccBalance(), object.getAccBalance()));

				bank.addAccount(object);
				bank.totalsManipulation("Savings", 'd', object.getAccBalance());
			}else if(person.getAcctType().equals("Checking")){
				CheckingsAccount object = new CheckingsAccount(person);
				object.addTransaction(new TransactionReceipt(ticket, true, "none", object.getAccBalance(), object.getAccBalance()));

				bank.addAccount(object);
				bank.totalsManipulation("Checking", 'd', object.getAccBalance());
			}else if(person.getAcctType().equals("CD")){

				String date = fileScan.next();
				CDAccount object = new CDAccount(person, CDAccount.stringToCalendar(date));
				object.addTransaction(new TransactionReceipt(ticket, true, "none", object.getAccBalance(), object.getAccBalance()));
				
				bank.addAccount(object);
				bank.totalsManipulation("CD", 'd', object.getAccBalance());
			}

		}
	}

	/* Method printAccts:
	 * Input:
	 *  bank - reference to the Bank object
	 *  outFile - reference to the output file
	 * Process:
	 *  Prints the database of accounts
	 * Output:
	 *  Prints the database of accounts
	*/
	public static void printAccts(Bank bank, PrintWriter outFile) throws IOException
	{
		outFile.println("                          Database of Accounts");
		outFile.println("\nFirst Name | Last Name |       SSN       |    Account Number    | Account Type | Account Status |    Balance    | Maturity Date");
		for(int i = 0; i < bank.getNumAccts(); i++){
			Account object = bank.getAcct(i);
			Name name = object.getDepositor().getName();
			int ssn = object.getDepositor().getSSN();
			int accountNumber = object.getAccNum();
			String accType = object.getAcctType();
			Boolean status = object.getStatus();
			MonetaryValue balance = object.getAccBalance();

			if(!accType.equals("CD")){
				outFile.print(String.format("%-15s %-11s %-20s %-18s %-15s %-15s %s", "   " +  name.getFirst(), name.getLast(), ssn, accountNumber,
							accType, status? "Open":"Closed", balance.toString()));
			}else{
				CDAccount account = (CDAccount)object;
				outFile.print(String.format("%-15s %-11s %-20s %-18s %-15s %-15s %-15s %s", "   " +  name.getFirst(), name.getLast(), ssn, accountNumber,
							accType, status? "Open":"Closed", balance.toString(), Bank.dateToString(account.getMaturityDate())));
			}
			
			outFile.println();
        }
		outFile.println("\nTotal Amount in all Savings Accounts: " + bank.getTotal("Savings").toString());
		outFile.println("Total Amount in all Checking Accounts: " + bank.getTotal("Checking").toString());
		outFile.println("Total Amount in all CD Accounts: " + bank.getTotal("CD").toString());
		outFile.println("Total Amount in all Accounts: " + bank.getTotal("Total").toString());
		outFile.println();

	}
	
	/* Method printAcctsByAcctNumSortKey:
	 * Input:
	 *  bank - reference to the Bank object
	 *  outFile - reference to the output file
	 * Process:
	 *  Prints the database of accounts in the order of AcctNumSortKey
	 * Output:
	 *  Prints the database of accounts in the order of AcctNumSortKey
	*/
	public static void printAcctsByAcctNumSortKey(Bank bank, PrintWriter outFile) throws IOException
	{	
		outFile.println("                       Database of Accounts by Account Number");
		outFile.println("\nFirst Name | Last Name |       SSN       |    Account Number    | Account Type | Account Status |    Balance    | Maturity Date");
		ArrayList<Integer> positions = bank.getAcctNumSortKey();
		for(int i = 0; i < positions.size(); i++){
			Account object = bank.getAcct(positions.get(i));
			Name name = object.getDepositor().getName();
			int ssn = object.getDepositor().getSSN();
			int accountNumber = object.getAccNum();
			String accType = object.getAcctType();
			Boolean status = object.getStatus();
			MonetaryValue balance = object.getAccBalance();

			if(!accType.equals("CD")){
				outFile.print(String.format("%-15s %-11s %-20s %-18s %-15s %-15s %s", "   " +  name.getFirst(), name.getLast(), ssn, accountNumber,
							accType, status? "Open":"Closed", balance.toString()));
			}else{
				CDAccount account = (CDAccount)object;
				outFile.print(String.format("%-15s %-11s %-20s %-18s %-15s %-15s %-15s %s", "   " +  name.getFirst(), name.getLast(), ssn, accountNumber,
							accType, status? "Open":"Closed", balance.toString(), Bank.dateToString(account.getMaturityDate())));
			}
			
			outFile.println();
        }
		outFile.println();

	}

	/* Method printAcctsBySSNSortKey:
	 * Input:
	 *  bank - reference to the Bank object
	 *  outFile - reference to the output file
	 * Process:
	 *  Prints the database of accounts in the order of SSNSortKey
	 * Output:
	 *  Prints the database of accounts in the order of SSNSortKey
	*/
	public static void printAcctsBySSNSortKey(Bank bank, PrintWriter outFile) throws IOException
	{
		outFile.println("                       Database of Accounts by SSN");
		outFile.println("\nFirst Name | Last Name |       SSN       |    Account Number    | Account Type | Account Status |    Balance    | Maturity Date");
		ArrayList<Integer> positions = new ArrayList<Integer>(bank.getSSNSortKey());
		for(int i = 0; i < positions.size(); i++){
			Account object = bank.getAcct(positions.get(i));
			Name name = object.getDepositor().getName();
			int ssn = object.getDepositor().getSSN();
			int accountNumber = object.getAccNum();
			String accType = object.getAcctType();
			Boolean status = object.getStatus();
			MonetaryValue balance = object.getAccBalance();

			if(!accType.equals("CD")){
				outFile.print(String.format("%-15s %-11s %-20s %-18s %-15s %-15s %s", "   " +  name.getFirst(), name.getLast(), ssn, accountNumber,
							accType, status? "Open":"Closed", balance.toString()));
			}else{
				CDAccount account = (CDAccount)object;
				outFile.print(String.format("%-15s %-11s %-20s %-18s %-15s %-15s %-15s %s", "   " +  name.getFirst(), name.getLast(), ssn, accountNumber,
							accType, status? "Open":"Closed", balance.toString(), Bank.dateToString(account.getMaturityDate())));
			}
			
			outFile.println();
        }
		outFile.println();
	}

	/* Method printAcctsByNameSortKey:
	 * Input:
	 *  bank - reference to the Bank object
	 *  outFile - reference to the output file
	 * Process:
	 *  Prints the database of accounts in the order of NameSortKey
	 * Output:
	 *  Prints the database of accounts in the order of NameSortKey
	*/
	public static void printAcctsByNameSortKey(Bank bank, PrintWriter outFile) throws IOException
	{
		outFile.println("                       Database of Accounts by Name");
		outFile.println("\nFirst Name | Last Name |       SSN       |    Account Number    | Account Type | Account Status |    Balance    | Maturity Date");
		ArrayList<Integer> positions = new ArrayList<Integer>(bank.getNameSortKey());
		for(int i = 0; i < positions.size(); i++){
			Account object = bank.getAcct(positions.get(i));
			Name name = object.getDepositor().getName();
			int ssn = object.getDepositor().getSSN();
			int accountNumber = object.getAccNum();
			String accType = object.getAcctType();
			Boolean status = object.getStatus();
			MonetaryValue balance = object.getAccBalance();

			if(!accType.equals("CD")){
				outFile.print(String.format("%-15s %-11s %-20s %-18s %-15s %-15s %s", "   " +  name.getFirst(), name.getLast(), ssn, accountNumber,
							accType, status? "Open":"Closed", balance.toString()));
			}else{
				CDAccount account = (CDAccount)object;
				outFile.print(String.format("%-15s %-11s %-20s %-18s %-15s %-15s %-15s %s", "   " +  name.getFirst(), name.getLast(), ssn, accountNumber,
							accType, status? "Open":"Closed", balance.toString(), Bank.dateToString(account.getMaturityDate())));
			}
			
			outFile.println();
        }

	}
	/* Method menu()
	 * Input:
	 *  none
	 * Process:
	 *  Prints the menu of transaction choices
	 * Output:
	 *  Prints the menu of transaction choices
	 */
	public static void menu()
	{
		System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
            System.out.println("(W) - Withdrawal");
            System.out.println("(D) - Deposit");
            System.out.println("(C) - Clear Check");
            System.out.println("(N) - New Account");
            System.out.println("(B) - Balance Lookup");
            System.out.println("(I) - Account Info (Without Transaction History)");
            System.out.println("(H) - Account Info plus Account Transaction History");
			System.out.println("(S) - Close Account(close(shut), but do not delete the account)");
			System.out.println("(R) - Reopen a closed account");
			System.out.println("(X) - Delete Account(close and delete the account from the database)");
			System.out.println("(Q) - Quit");
            System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
            System.out.print("Choice(uppercase or lowercase): ");
	}
	
	/* Method closeAcct():
	 * Input:
	 *  bank - reference to the Bank object
	 *  outFile - reference to the output file
	 *  kybd - reference to the "test cases" input file
	 * Process:
	 *  Prompts for the requested account
	 *  Creates TransactionTicket object
	 *  Calls bank.closeAccount() to execute the transaction
	 *  If the account exists, the account is closed (or stays closed)
	 *  Otherwise, an error message is printed
	 * Output:
	 *  If the account exists, the account is closed (or stays closed)
	 *  Otherwise, an error message is printed
	 */
	public static void closeAcct(Bank bank, PrintWriter outFile, Scanner kybd) throws IOException
	{
		TransactionTicket ticket = new TransactionTicket();
		TransactionReceipt receipt = new TransactionReceipt();

		int accountNumber = 0;
		Account object = new Account();

		try{
			ticket = new TransactionTicket(Calendar.getInstance(), "Close Account", 0, 0);
			//prompt for Account information

			accountNumber = kybd.nextInt();
			//Initializing index with findAcct() to get accountClosedException
			object = bank.getAcct(bank.findAcct(accountNumber));

			//Process Transaction
			
			receipt = new TransactionReceipt(object.closeAcct(ticket));
			object.addTransaction(receipt);
		}catch(InvalidAccountException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
		}
		receipt.setAccNum(accountNumber);
		outFile.println(receipt.toString());
	}
	
	/* Method ReopenAcct():
	 * Input:
	 *  bank - reference to the Bank object
	 *  outFile - reference to the output file
	 *  kybd - reference to the "test cases" input file
	 * Process:
	 *  Prompts for the requested account
	 *  Creates TransactionTicket object
	 *  Calls bank.reopenAccount() to execute the transaction
	 *  If the account exists, the account is reopened (or remains open)
	 *  Otherwise, an error message is printed
	 * Output:
	 *  If the account exists, the account is reopened (or remains open)
	 *  Otherwise, an error message is printed
	 */
	public static void reopenAcct(Bank bank, PrintWriter outFile, Scanner kybd) throws IOException
	{
		TransactionTicket ticket = new TransactionTicket();
		TransactionReceipt receipt = new TransactionReceipt();

		int accountNumber = 0;
		Account object = new Account();

		try{
			ticket = new TransactionTicket(Calendar.getInstance(), "Reopen Account", 0, 0);
			//prompt for Account information
			accountNumber = kybd.nextInt();
			object = bank.getAcct(bank.findAcct(accountNumber));

			//Process Transaction
			
			receipt = object.reopenAcct(ticket);
			object.addTransaction(receipt);
		}catch(InvalidAccountException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
		}
		receipt.setAccNum(accountNumber);
		outFile.println(receipt.toString());
		
	}
	
	/* Method balance:
	 * Input:
	 *  bank - reference to the Bank object
	 *  outFile - reference to the output file
	 *  kybd - reference to the "test cases" input file
	 * Process:
	 *  Prompts for the requested account
	 *  Creates TransactionTicket object
	 *  Calls bank.getBalance() to execute the transaction
	 *  If the account exists, the balance is printed
	 *  Otherwise, an error message is printed
	 * Output:
	 *  If the account exists, the balance is printed
	 *  Otherwise, an error message is printed
	 */
	public static void balance(Bank bank, PrintWriter outFile, Scanner kybd) throws IOException
	{
		TransactionTicket ticket = new TransactionTicket();
		TransactionReceipt receipt = new TransactionReceipt();

		int accountNumber = 0;
		Account object = new Account();

		try{
			ticket = new TransactionTicket(Calendar.getInstance(), "Balance Inquiry", 0, 0);
			//prompt for Account information
			accountNumber = kybd.nextInt();
			//Account object copy
			object = bank.getAcct(bank.findAcct(accountNumber));

			//processing the Transaction
			
			receipt = new TransactionReceipt(object.getBalance(ticket, object.getAccBalance()));
			object.addTransaction(receipt);
		}catch(InvalidAccountException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
		}
		receipt.setAccNum(accountNumber);
		outFile.println(receipt.toString());
	}

	/* Method deposit:
	 * Input:
	 *  bank - reference to the Bank object
	 *  outFile - reference to the output file
	 *  kybd - reference to the "test cases" input file
	* Process:
	 *  Prompts for the requested account
	 *  Prompts for the amount to deposit
	 *  Creates TransactionTicket object
	 *  Calls bank.makeDeposit() to execute the transaction
	 *  If the transaction is valid, it makes the deposit and prints the new balance
	 *  Otherwise, an error message is printed
	 * Output:
	 *  For a valid deposit, the deposit transaction is printed
	 *  Otherwise, an error message is printed
	 */
	public static void deposit(Bank bank, PrintWriter outFile, Scanner kybd) throws IOException
	{
		TransactionTicket ticket = new TransactionTicket();
		TransactionReceipt receipt = new TransactionReceipt();
		
		Account object = new Account();
		double Deposit = 0;

		int accNum = 0;
		String type = "";

		try{
			type = "Deposit";
			ticket = new TransactionTicket(Calendar.getInstance(), type, Deposit, 0);
			//prompt for account
			accNum = kybd.nextInt();
			object = bank.getAcct(bank.findAcct(accNum));
			//prompt for Deposit amount
			Deposit = kybd.nextDouble();

			//processing Transaction	
			ticket = new TransactionTicket(Calendar.getInstance(), "Deposit", Deposit, 0);
			receipt = new TransactionReceipt(object.makeDeposit(ticket, new MonetaryValue(Deposit)));

			//finalizing
			object.addTransaction(receipt);
			bank.totalsManipulation(object.getAcctType(), 'd', new MonetaryValue(Deposit));

			//prompts the user for a new maturity date
			if(object.getAcctType().equals("CD")){
				CDAccount account = (CDAccount) bank.getAcct(bank.findAcct(accNum));
				int choice = kybd.nextInt();
				
				type = "CDdeposit";
				account.setMaturityDate(choice);
				receipt.setPostTransactionMaturityDate(account.getMaturityDate());	
			}
		}catch(InvalidAccountException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
		}catch(InvalidAmountException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
			object.addTransaction(receipt);
		}catch(AccountClosedException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
			object.addTransaction(receipt);
		}catch(CDMaturityDateException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
			object.addTransaction(receipt);
		}
		receipt.setAccNum(accNum);
		receipt.alterationString(type);
		outFile.println(receipt.toString());
	}

	/* Method withdrawal:
	 * Input:
	 *  bank - reference to the Bank object
	 *  outFile - reference to the output file
	 *  kybd - reference to the "test cases" input file
	* Process:
	 *  Prompts for the requested account
	 *  Prompts for the amount to withdraw
	 *  Creates TransactionTicket object
	 *  Calls bank.makeWithdrawal() to execute the transaction
	 *  If the transaction is valid, it makes the withdrawal and prints the new balance
	 *  Otherwise, an error message is printed
	 * Output:
	 *  For a valid withdrawal, the withdrawal transaction is printed
	 *  Otherwise, an error message is printed
	 */
	public static void withdrawal(Bank bank, PrintWriter outFile, Scanner kybd) throws IOException
	{
		TransactionTicket ticket = new TransactionTicket();
		TransactionReceipt receipt = new TransactionReceipt();
		
		Account object = new Account();
		double Withdrawal = 0;

		int accNum = 0;
		String type = "";

		try{
			type = "Withdrawal";
			ticket = new TransactionTicket(Calendar.getInstance(), type, Withdrawal, 0);
			//prompt for account
			accNum = kybd.nextInt();
			object = bank.getAcct(bank.findAcct(accNum));
			
			//prompt for withdrawal amount
			Withdrawal = kybd.nextDouble();

			//processing Transaction
			ticket = new TransactionTicket(Calendar.getInstance(), type, Withdrawal, 0);
			receipt = new TransactionReceipt(object.makeWithdrawal(ticket, new MonetaryValue(Withdrawal)));
			//Finalizing 
			object.addTransaction(receipt);
			bank.totalsManipulation(object.getAcctType(), 'W', new MonetaryValue(Withdrawal));

			//prompts the user for a new maturity date if CD
			if(object.getAcctType().equals("CD")){
				CDAccount account = (CDAccount) object;
				int choice = kybd.nextInt();

				type = "CDwithdrawal";
				account.setMaturityDate(choice);
				receipt.setPostTransactionMaturityDate(account.getMaturityDate());
			}
		//series of catch blocks to intercept exceptions
		}catch(InvalidAccountException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
		}catch(InvalidAmountException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
			object.addTransaction(receipt);
		}catch(AccountClosedException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
			object.addTransaction(receipt);
		}catch(InsufficientFundsException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
			object.addTransaction(receipt);
		}catch(CDMaturityDateException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
			object.addTransaction(receipt);
		}
		receipt.setAccNum(accNum);
		receipt.alterationString(type);
		outFile.println(receipt.toString());
		
	}

	/* Method clearCheck:
	 * Input:
	 *  bank - reference to the Bank object
	 *  outFile - reference to the output file
	 *  kybd - reference to the "test cases" input file
	* Process:
	 *  Prompts for the requested account
	 *  Prompts for the amount to withdraw
	 *  Creates a Check object
	 *  Creates TransactionTicket object
	 *  Calls bank.clearCheck() to execute the transaction
	 *  If the transaction is valid, it makes the withdrawal and prints the new balance
	 *  Otherwise, an error message is printed
	 * Output:
	 *  For a valid withdrawal, the withdrawal transaction is printed
	 *  Otherwise, an error message is printed
	 */
	public static void clearCheck(Bank bank, PrintWriter outFile, Scanner kybd) throws IOException
	{
		TransactionTicket ticket = new TransactionTicket();
		TransactionReceipt receipt = new TransactionReceipt();
		
		Account object = new Account();
		double Withdrawal = 0;

		int accNum = 0;
		String type = "";

		try{
			//initialize the variables that make up the check object utilizing the kybd input	
			accNum =  kybd.nextInt();
			object = bank.getAcct(bank.findAcct(accNum));
			
			//Check Amount
			Withdrawal = kybd.nextDouble();
			type = "Clear Check";

			//Date of check
			String date = kybd.next();
			Calendar dateOfCheck =  CDAccount.stringToCalendar(date);

			//putting it all together into the clearCheck method of CheackingsAccount
			Check object1 = new Check(accNum, new MonetaryValue(Withdrawal), dateOfCheck);
			ticket = new TransactionTicket(Calendar.getInstance(), "Clear Check", Withdrawal, 0);
			receipt = new TransactionReceipt(object.clearCheck(ticket, object1));
			object.addTransaction(receipt);

			//Adjusting the bank information
			bank.totalsManipulation("Checking", 'w', new MonetaryValue(Withdrawal));

		}catch(InvalidAccountException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
		}catch(PostDatedCheckException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
			object.addTransaction(receipt);
		}catch(InvalidAmountException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
			object.addTransaction(receipt);
		}catch(CheckTooOldException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
			object.addTransaction(receipt);
		}catch(InsufficientFundsException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
			object.addTransaction(receipt);
		}
		receipt.setAccNum(accNum);
		receipt.alterationString(type);
		outFile.println(receipt.toString());
	}

	/* Method newAcct:
	 * Input:
	 *  bank - reference to the Bank object
	 *  outFile - reference to the output file
	 *  kybd - reference to the "test cases" input file
	* Process:
	 *  Prompts for the new account info
	 *  Calls bank.openNewAccount() to execute the transaction
	 *  If the transaction is valid, a confirmation message is printed  
	 *  Otherwise, an error message is printed
	 * Output:
	 *  For a valid transaction, a confirmation message is printed  
	 *  Otherwise, an error message is printed
	 */
	public static void newAcct(Bank bank, PrintWriter outFile, Scanner kybd) throws IOException
	{
		TransactionTicket ticket = new TransactionTicket();
		TransactionReceipt receipt = new TransactionReceipt();
		
		Account object = new Account();
		int accNum = 0;

		try{
			//starting with accNum to check if it already exists
			accNum = kybd.nextInt();
			bank.accountExists(accNum);

			//Initializing objects
			Name name = new Name();
			Depositor person = new Depositor();

			//Creating the Name Object for the Depositor Object
			name.setFirst(kybd.next());
			name.setLast(kybd.next());

			person.setName(name);
			
			//Finishing the Depositor Object
			int SSN = kybd.nextInt();
			person.setSSN(SSN);

			//the rest of the information for the new account
			String accType = kybd.next();

			double accBalance = kybd.nextDouble();
			
			//Initializing the variables that will be used for the openAccount method
			Account open = new Account(person,accNum,accType, new MonetaryValue(accBalance));
			ticket = new TransactionTicket(Calendar.getInstance(), "Opening New Account", 0, 0);
			//Create the account object specified to it's type
			if(accType.equals("Savings")){
				SavingsAccount Sobject = new SavingsAccount(open);
				//finalizing by adding receipt to transaction history of account
				receipt = new TransactionReceipt(bank.openNewAccount(ticket, Sobject));
				Sobject.addTransaction(receipt);
				receipt.setInformation(Sobject);

			}else if(accType.equals("Checking")){
				CheckingsAccount Cobject = new CheckingsAccount(open);
				//finalizing by adding receipt to transaction history of account
				receipt = new TransactionReceipt(bank.openNewAccount(ticket, Cobject));
				Cobject.addTransaction(receipt);
				receipt.setInformation(Cobject);

			}else{
				String date = kybd.next();
				CDAccount CDobject = new CDAccount(open, CDAccount.stringToCalendar(date));
				//finalizing by adding receipt to transaction history of account
				receipt = new TransactionReceipt(bank.openNewAccount(ticket, CDobject));
				CDobject.addTransaction(receipt);
				receipt.setMaturityDate(CDobject.getMaturityDate());

				receipt.setInformation(CDobject);
			}
			//Adjusting the bank information
			bank.totalsManipulation(accType, 'd', new MonetaryValue(accBalance));

		}catch(AccountExistsException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
		}
		receipt.setAccNum(accNum);
		outFile.println(receipt.toString());
	}

	/* Method deleteAcct:
	 * Input:
	 *  bank - reference to the Bank object
	 *  outFile - reference to the output file
	 *  kybd - reference to the "test cases" input file
	* Process:
	 *  Prompts for the requested account
	 *  Creates TransactionTicket object
	 *  Calls bank.deleteAccount() to execute the transaction
	 *  If the transaction is valid, a confirmation message is printed  
	 *  Otherwise, an error message is printed
	 * Output:
	 *  For a valid transaction, the transaction result is printed
	 *  Otherwise, an error message is printed
	 */
	public static void deleteAcct(Bank bank, PrintWriter outFile, Scanner kybd) throws IOException
	{

		TransactionTicket ticket = new TransactionTicket();
		TransactionReceipt receipt = new TransactionReceipt();
		
		Account object = new Account();
		int accNum = 0;

		try{
			ticket = new TransactionTicket(Calendar.getInstance(), "Account Deletion", 0, 0);
			//Find account
			accNum = kybd.nextInt();
			object = bank.getAcct(bank.findAcct(accNum));

			//processing the transaction
			receipt = new TransactionReceipt(bank.deleteAccount(ticket,object));
			object.addTransaction(receipt);

			//Adjusting the bank information
			bank.totalsManipulation(object.getAcctType(), 'w', new MonetaryValue(object.getAccBalance().doubleValue()));
			
		}catch(InvalidAccountException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
		}catch(InvalidAccountDeletionException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), object.getAccBalance(), object.getAccBalance());
			object.addTransaction(receipt);
		}	
		receipt.setNumAccts(bank.getNumAccts());
		receipt.setAccNum(accNum);
		outFile.println(receipt.toString());
	}

	/* Method acctIn
	* Input:
	 *  bank - reference to the Bank object
	 *  outFile - reference to the output file
	 *  kybd - reference to the "test cases" input file
	* Process:
	 *  Prompts for the requested SSN
	 *  searches the account database for matching accounts
	 *  and prints the account info for each matching account
	 *  If there are no matching accounts, an error message is printed
	 * Output:
	 *  For a valid transaction, the transaction result is printed
	 *  Otherwise, an error message is printed
	 */
	public static void acctInfo(Bank bank, PrintWriter outFile, Scanner kybd) throws IOException
	{
		TransactionTicket ticket = new TransactionTicket();
		TransactionReceipt receipt = new TransactionReceipt();

		int SSN = 0;
		Account account = new Account();

		try{
			ticket = new TransactionTicket(Calendar.getInstance(),"Account Lookup", 0, 0);
			SSN = kybd.nextInt();
			account = bank.getAcct(bank.findAcct(SSN));
			
			receipt = new TransactionReceipt(ticket, true, "none", account.getAccBalance(), account.getAccBalance());
			//using the numaccts method to loop through accounts searching for matches
				
		}catch(InvalidAccountException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), account.getAccBalance(), account.getAccBalance());
		}
		outFile.println();
		receipt.setAccNum(SSN);
		outFile.println(receipt);

		if(receipt.getSuccessIndicatorFlag() == true){
			outFile.println("Last Name | First Name |       SSN       |    Account Number    | Account Type | Account Status |    Balance    | Maturity Date");
			for(int i = 0; i < bank.getNumAccts(); i++){
				if(bank.getAcct(i).getDepositor().getSSN() == SSN){

					Account object = bank.getAcct(i);
					Name name = object.getDepositor().getName();
					int ssn = object.getDepositor().getSSN();
					int accountNumber = object.getAccNum();
					String accType = object.getAcctType();
					Boolean status = object.getStatus();
					MonetaryValue balance = object.getAccBalance();

					if(!accType.equals("CD")){
						object.addTransaction(new TransactionReceipt(ticket, true, "none", balance, balance));
						outFile.print(String.format("%-15s %-11s %-20s %-18s %-15s %-15s %s", "   " +  name.getFirst(), name.getLast(), ssn, accountNumber,
									accType, status? "Open":"Closed", balance.toString()));
					}else{
						CDAccount CDobject = new CDAccount((CDAccount)bank.getAcct(i));
						account.addTransaction(new TransactionReceipt(ticket, true, "none", balance, balance));

						outFile.print(String.format("%-15s %-11s %-20s %-18s %-15s %-15s %-15s %s", "   " +  name.getFirst(), name.getLast(), ssn, accountNumber,
									accType, status? "Open":"Closed", balance.toString(), Bank.dateToString(CDobject.getMaturityDate())));
					}
					
					outFile.println();
				}
			}
		}
	}
	
	/* Method acctInfoWithTransactionHistory:
	 * Input:
	 *  bank - reference to the Bank object
	 *  outFile - reference to the output file
	 *  kybd - reference to the "test cases" input file
	* Process:
	 *  Prompts for the requested SSN
	 *  searches the account database for matching accounts
	 *  and prints the account info for each matching account
	 *  If there are no matching accounts, an error message is printed
	 * Output:
	 *  For a valid transaction, the transaction result is printed
	 *  Otherwise, an error message is printed
	 */
	public static void acctInfoWithTransactionHistory(Bank bank, PrintWriter outFile, Scanner kybd)throws IOException
	{
		TransactionTicket ticket = new TransactionTicket();
		TransactionReceipt receipt = new TransactionReceipt();

		int SSN = 0;
		Account account = new Account();

		try{
			ticket = new TransactionTicket(Calendar.getInstance(), "Account History Lookup", 0,0);
			SSN = kybd.nextInt();
			account = new Account(bank.getAcct(bank.findAcct(SSN)));

			receipt = new TransactionReceipt(ticket, true, "none", account.getAccBalance(), account.getAccBalance());
		}catch(InvalidAccountException ex){
			receipt = new TransactionReceipt(ticket, false, ex.getMessage(), account.getAccBalance(), account.getAccBalance());
		}
		outFile.println();
		receipt.setAccNum(SSN);
		outFile.println(receipt);


			for(int i = 0; i < bank.getNumAccts(); i++){
				if(bank.getAcct(i).getDepositor().getSSN() == SSN){
					outFile.println("Last Name | First Name |       SSN       |    Account Number    | Account Type | Account Status |    Balance    | Maturity Date");
					Account object = bank.getAcct(i);
					Name name = object.getDepositor().getName();
					int ssn = object.getDepositor().getSSN();
					int accountNumber = object.getAccNum();
					String accType = object.getAcctType();
					Boolean status = object.getStatus();
					MonetaryValue balance = object.getAccBalance();

					if(!accType.equals("CD")){
						outFile.print(String.format("%-15s %-11s %-20s %-18s %-15s %-15s %s", "   " +  name.getFirst(), name.getLast(), ssn, accountNumber,
									accType, status? "Open":"Closed", balance.toString()));
					}else{
						CDAccount CDaccount = new CDAccount((CDAccount)bank.getAcct(i));
						outFile.print(String.format("%-15s %-11s %-20s %-18s %-15s %-15s %-15s %s", "   " +  name.getFirst(), name.getLast(), ssn, accountNumber,
									accType, status? "Open":"Closed", balance.toString(), Bank.dateToString(CDaccount.getMaturityDate())));
					}
					outFile.println("\n     ******* Account Transactions *******");
					outFile.println("Date            Transaction               Amount     Status   Balance");
					ArrayList<TransactionReceipt> transactionHistory = object.getTransactionHistory(ticket);
					//loops through the whole arraylist printing every Transaction Receipt
					for(int k = 0; k < transactionHistory.size(); k++){
						receipt = transactionHistory.get(k);
						outFile.println(receipt.historyFormat());
					}
					outFile.println();
				}
	
			}
		}	
	
	/* Method pause() */
	public static void pause(Scanner keyboard)
	{
	}

}