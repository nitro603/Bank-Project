import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class Account {

    private Depositor person;
    private int accNum;
    private String acctType;
    private MonetaryValue accBalance;
    private boolean statusOpen;

    private ArrayList<TransactionReceipt> transactionHistory;

    Account(){
        this.person = new Depositor();
        this.accNum = 0;
        this.acctType = "";
        this.accBalance = new MonetaryValue();
        this.statusOpen = false;
        transactionHistory = new ArrayList<TransactionReceipt>();
        
    }

    Account(Depositor newP, int newaNum, String accType, MonetaryValue balance){
        this.person = newP;
        this.accNum = newaNum;
        this.acctType = accType; 
        this.accBalance = balance;
        statusOpen = true;
        transactionHistory = new ArrayList<TransactionReceipt>();
    }

    Account(Account object){
        this.person = object.getDepositor();
        this.accNum = object.getAccNum();
        this.acctType = object.getAcctType();
        this.accBalance = object.getAccBalance();
        statusOpen = true;

        transactionHistory = object.getTransactionHistory(null);
    }

    public Depositor getDepositor(){
        return person;
    }

    public int getAccNum(){
        return accNum;
    }

    public String getAcctType(){
        return acctType;
    }

    public MonetaryValue getAccBalance(){
        return accBalance;
    }

    public boolean getStatus(){
        return statusOpen;
    }

    public void addTransaction(TransactionReceipt object){
        TransactionReceipt addition = object;    
        transactionHistory.add(addition);
    }

    public TransactionReceipt getBalance(TransactionTicket ticket, MonetaryValue balance){

        return new TransactionReceipt(ticket, true, "none", balance, balance);
    }

    public TransactionReceipt makeDeposit(TransactionTicket ticket, MonetaryValue deposit)throws IOException{
        if(statusOpen){
            if(deposit.isNegative() || !accBalance.add(deposit)){
                throw new InvalidAmountException("Deposit");
            }
            return new TransactionReceipt(ticket, true, "none", new MonetaryValue(accBalance.doubleValue() - deposit.doubleValue()), accBalance);
        }else{
            throw new AccountClosedException(accNum);
        }
            
    }

    public TransactionReceipt makeWithdrawal(TransactionTicket ticket, MonetaryValue withdrawal)throws IOException{
        MonetaryValue before = new MonetaryValue(accBalance.doubleValue());
        if(statusOpen){
            if(withdrawal.isNegative()){
            throw new InvalidAmountException("Withdrawal");
            }
            if(accBalance.isLessThan(withdrawal)){
                throw new InsufficientFundsException();
            }
            accBalance.subtract(withdrawal);
            return new TransactionReceipt(ticket, true, "none", before, accBalance);
        }else{
            throw new AccountClosedException(accNum);
        }
    
    }

    public TransactionReceipt closeAcct(TransactionTicket ticket){
        statusOpen = false;
        return new TransactionReceipt(ticket, true, "none", accBalance, accBalance);
    }

    public TransactionReceipt reopenAcct(TransactionTicket ticket){
        statusOpen = true;
        return new TransactionReceipt(ticket, true, "none", accBalance, accBalance);
    }

    public ArrayList<TransactionReceipt> getTransactionHistory(TransactionTicket ticket){

        if(ticket == null){
            return this.transactionHistory;
        }else{
            TransactionReceipt receipt = new TransactionReceipt(ticket, true, "none", accBalance, accBalance);
            addTransaction(receipt);        
            return this.transactionHistory;
        }
        
    }
    
    public TransactionReceipt clearCheck(TransactionTicket ticket, Check object) throws IOException{
        //creating instance of pretransaction balance
        double preTransaction = accBalance.doubleValue();
        MonetaryValue preAmount = new MonetaryValue(preTransaction);

        //initializing the ints of the month the check can be used and today's month
        Calendar start = object.getDateofCheck();
        String placeholder = Bank.dateToString(start);
        Calendar end = CDAccount.stringToCalendar(placeholder);
        end.add(Calendar.MONTH, 6);
    
        if(acctType.equals("Checking")){

            if(!object.getCheckAmount().isNegative()){
         
                if(object.getDateofCheck().after(Calendar.getInstance())){
                    throw new PostDatedCheckException();
                }
                if(Calendar.getInstance().before(end) && Calendar.getInstance().after(start)){

                    if(accBalance.isLessThan(object.getCheckAmount())){
                        accBalance.subtract(new MonetaryValue(2.50));
                        throw new InsufficientFundsException(object);

                    }else if(accBalance.isLessThan(new MonetaryValue(2500.0))){

                        accBalance.subtract(new MonetaryValue(1.50));
                        accBalance.subtract(object.getCheckAmount());
                        return new TransactionReceipt(ticket, true, "none", preAmount, getAccBalance());
                    }
                    accBalance.subtract(object.getCheckAmount());
                    return new TransactionReceipt(ticket, true, "none", preAmount, getAccBalance());
                }else{
                    throw new CheckTooOldException();
                }
            }else{
                throw new InvalidAmountException("Check Clear");
            }
            
        }else{  
            throw new InvalidAccountException();
        }
        
    }

    public static Account readScan(Scanner fileScan){
        //reading in the variables that make up the account object
        Depositor info = Depositor.readScan(fileScan);
        int accN = fileScan.nextInt();
        String acctType = fileScan.next();
        Double balance = fileScan.nextDouble();
        
        //returning newly created object
        return new Account(info, accN, acctType, new MonetaryValue(balance));

    }

    public String getFormat(){
        return "First Name: " + person.getName().getFirst() + "\nLast Name: " + person.getName().getLast() + 
        "\nSocial Security: " + person.getSSN() + "\nAccount Type: " + acctType + "\nBalance: " + accBalance.toString();
    }

    @Override
    public String toString(){
        return person.toString() + "             " + accNum + "              " + acctType + "            " + (statusOpen? "Open":"Closed") + "       " + getAccBalance();  
    }
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Account){
            Account other = (Account)obj;
            return other.getDepositor().equals(this.getDepositor()) && other.getAccNum() == this.getAccNum() 
            && other.getAccBalance() == this.getAccBalance() && other.getAcctType() == this.getAcctType();
        }else{
            return false;
        }
    }


}
