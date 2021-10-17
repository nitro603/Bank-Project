import java.util.Calendar;

public class TransactionReceipt {

    private TransactionTicket ticket;
    private boolean successIndicatorFlag;
    private String reasonForFailure;
    private MonetaryValue preTransactionBalance;
    private MonetaryValue postTransactionBalance;
    private Calendar postTransactionMaturityDate;

    private int identifier;
    private String balanceAlteration;
    private Account information = new Account();
    private Calendar maturity = Calendar.getInstance();
    private int numaccts;

    TransactionReceipt(){
        this.ticket = new TransactionTicket();
        this.successIndicatorFlag = false;
        this.reasonForFailure = "";
        this.preTransactionBalance = new MonetaryValue();
        this.postTransactionBalance = new MonetaryValue();
    }

    TransactionReceipt(TransactionTicket ticket, boolean success, String reason, MonetaryValue balance, MonetaryValue balance2){
        this.ticket = ticket;
        this.successIndicatorFlag = success;
        this.reasonForFailure = reason;
        this.preTransactionBalance = balance;
        this.postTransactionBalance = balance2;
    }
    TransactionReceipt(TransactionReceipt other){
        this.ticket = other.getTransactionTicket();
        this.successIndicatorFlag = other.getSuccessIndicatorFlag();
        this.reasonForFailure = other.getReasonForFailure();
        this.preTransactionBalance = other.getPreTransactionBalance();
        this.postTransactionBalance = other.GetPostTransactionBalance();
    }

    public TransactionTicket getTransactionTicket(){
        return ticket;
    }
    public boolean getSuccessIndicatorFlag(){
        return successIndicatorFlag;
    }
    public String getReasonForFailure(){
        return reasonForFailure;
    }
    public MonetaryValue getPreTransactionBalance(){
        return preTransactionBalance;
    }
    public MonetaryValue GetPostTransactionBalance(){
        return postTransactionBalance;
    }
    public Calendar getPostTransactionMaturityDate(){
        return postTransactionMaturityDate;        
    }
    public void setPostTransactionMaturityDate(Calendar date){
        postTransactionMaturityDate = date;
    }

    public String historyFormat(){
        return String.format("%-15s %-25s %-10s %-8s %-10s %s", Bank.dateToString(ticket.getDateOfTransaction()), ticket.getTypeOfTransaction(), ticket.GetAmountOfTransaction().toString(),
        successIndicatorFlag? "Done":"Failed", postTransactionBalance.toString(), !successIndicatorFlag? reasonForFailure: "");
    }
    public void setInformation(Account other){
        information = new Account(other);
    }

    public void setAccNum(int identity){
        identifier = identity;
    }

    public void alterationString(String type){
        boolean cd = false;

        if(type.equals("CDdeposit") || type.equals("CDwithdrawal")){
            cd = true;
        }
        switch(type){
            case "Deposit":case "CDdeposit":
                if(successIndicatorFlag == true){
                    balanceAlteration = "Current Balance: " + preTransactionBalance.toString() + "\nAmount to Deposit: " + ticket.GetAmountOfTransaction().toString()
                    + "\nNew Balance: " + postTransactionBalance.toString() + (cd? "\nNew Maturity Date: " + Bank.dateToString(postTransactionMaturityDate):"") + "\n";
                }else{
                    balanceAlteration = "Current Balance: " + preTransactionBalance.toString() + "\nAmount to Deposit: " + ticket.GetAmountOfTransaction().toString()
                    + "\n" + reasonForFailure + "\n";
                }
            break;
            case "Withdrawal":case "CDwithdrawal":
                if(successIndicatorFlag == true){
                    balanceAlteration = "Current Balance: " + preTransactionBalance.toString() + "\nAmount to Withdraw: " + ticket.GetAmountOfTransaction().toString()
                    + "\nNew Balance: " + postTransactionBalance.toString() + (cd? "\nNew Maturity Date: " + Bank.dateToString(postTransactionMaturityDate):"") + "\n";
                }else{
                    balanceAlteration = "Current Balance: " + preTransactionBalance.toString() + "\nAmount to Withdraw: " + ticket.GetAmountOfTransaction().toString()
                    + "\n" + reasonForFailure + "\n";
                }
            break;
            case "Clear Check":
            if(successIndicatorFlag == true){
                balanceAlteration = "Current Balance: " + preTransactionBalance.toString() + "\nAmount to Withdraw: " + ticket.GetAmountOfTransaction().toString()
                + "\nNew Balance: " + postTransactionBalance.toString() + "\n";
            }else{
                balanceAlteration = "Current Balance: " + preTransactionBalance.toString() + "\nAmount to Withdraw: " + ticket.GetAmountOfTransaction().toString()
                + "\n" + reasonForFailure + "\n";
            }
            break;
            default:
            break;
        }
        
    }

    public void setMaturityDate(Calendar other){
        maturity = other;
    }
  
    public void setNumAccts(int num){
        numaccts = num;
    }

    @Override
    public String toString(){

        String type = ticket.getTypeOfTransaction();
        if(type.equals("Deposit") || type.equals("Withdrawal") || type.equals("Clear Check")){
            return "Transaction Type: " + type + "\nAccount Number: " + identifier +"\n" + balanceAlteration;
        }else if(type.equals("Balance Inquiry")){
            return "Transaction Type: " + type + "\nAccount Number: " + identifier 
            + "\n" + (!successIndicatorFlag? reasonForFailure:"Current Balance: " + preTransactionBalance.toString()) + "\n";
        }else if(type.equals("Reopen Account")){
            return "Transaction Type: " + type + "\nAccount Number: " + identifier + "\n" 
            + (successIndicatorFlag?"Account Successfully Reopened": reasonForFailure) + "\n";
        }else if(type.equals("Account Deletion")){
            return "Transaction Type: " + type + "\nAccount Number: " + identifier + "\n" 
            + (successIndicatorFlag? "Account Successfully Deleted\n" + "New Number of Accounts: " + numaccts: reasonForFailure) + "\n";
        }else if(type.equals("Close Account")){
            return "Transaction Type: " + type + "\nAccount Number: " + identifier + "\n" 
            + (successIndicatorFlag?"Account Successfully Closed": reasonForFailure) + "\n";
        }else if(type.equals("Account Lookup") || type.equals("Account History Lookup")){
            return "Transaction Type: " + type + "\nSSN: " + identifier
            + (successIndicatorFlag? "": "\n" + reasonForFailure + "\n");
        }else if(type.equals("Opening New Account")){
            return "Transaction Type: " + type + "\nAccount Number: " + identifier + "\n" 
            + (!successIndicatorFlag? reasonForFailure: information.getFormat()) + "\n" 
            + (information.getAcctType().equals("CD")? "Maturity Date: " + Bank.dateToString(maturity) + "\n":"");
        }else{
            return "";
        }
                
    }
        

}

