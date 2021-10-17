import java.util.Calendar;
import java.util.Date;

public class TransactionTicket {
    private Calendar dateOfTransaction;
    private String typeOfTransaction;
    private MonetaryValue amountOfTransaction;
    private int termOfDate;


    TransactionTicket(){
        dateOfTransaction = Calendar.getInstance();
        typeOfTransaction = "";
        amountOfTransaction = new MonetaryValue();
        termOfDate = 6;
    }
    TransactionTicket(Calendar dateOfTransaction, String typeOfTransaction, double amountOfTransaction, int termOfDate){
        this.dateOfTransaction = dateOfTransaction;
        this.typeOfTransaction = typeOfTransaction;
        this.amountOfTransaction = new MonetaryValue(amountOfTransaction);
        this.termOfDate = termOfDate;
    }
    TransactionTicket(TransactionTicket other){
        this.dateOfTransaction = other.getDateOfTransaction();
        this.typeOfTransaction = other.getTypeOfTransaction();
        this.amountOfTransaction = other.GetAmountOfTransaction();
        this.termOfDate = other.GetTermOfDate();
    }

    public Calendar getDateOfTransaction(){
        return dateOfTransaction;
    }
    public String getTypeOfTransaction(){
        return typeOfTransaction;
    }
    public MonetaryValue GetAmountOfTransaction(){
        return amountOfTransaction;
    }
    public int GetTermOfDate(){
        return termOfDate;
    }
      
    public String toString(){
        //getting date into a format similar to what is received
        Date format = dateOfTransaction.getTime();
        //Creating a string to hold all the information
        String ticket = "Day Of Transaction: " + format.toString() + "\nType of Transaction: " + typeOfTransaction + "\nTransactionAmount: " + amountOfTransaction
        +  "\nTerm of Date: " + termOfDate;

        return ticket;

    }
}
