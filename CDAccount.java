import java.io.IOException;
import java.util.Calendar;
import java.util.regex.MatchResult;

public class CDAccount extends Account{

    private Calendar maturityDate;

    CDAccount(){
        super();
        maturityDate = Calendar.getInstance();
    }
   CDAccount(Account object, Calendar date){
        super(object);
        this.maturityDate = date;
    }CDAccount(CDAccount object){
        super(object);
        this.maturityDate = object.getMaturityDate();
    }

    public static Calendar stringToCalendar(String date){
        Calendar calendar = Calendar.getInstance();
        int month;
		int day;
		int year;
		
		//This block of code is used to organize the string input into ints to make a calendar object
		if(date.substring(2,3).equals("/")){

			month = Integer.parseInt(date.substring(0, 2));
			if(date.substring(5,6).equals("/")){
				day = Integer.parseInt(date.substring(3,5));
				year = Integer.parseInt(date.substring(6));
			}else{
				day = Integer.parseInt(date.substring(3,4));
				year = Integer.parseInt(date.substring(5));
			}
		}else{

			month = Integer.parseInt(date.substring(0,1));
			if(date.substring(4,5).equals("/")){
				day = Integer.parseInt(date.substring(2,4));
				year = Integer.parseInt(date.substring(5));
			}else{
				day = Integer.parseInt(date.substring(2,3));
				year = Integer.parseInt(date.substring(4));
			}
		}
		calendar.set(year, month-1, day);

        return calendar;
    }

    public Calendar getMaturityDate(){
        return maturityDate;
    }
 
    @Override
    public TransactionReceipt makeDeposit(TransactionTicket ticket, MonetaryValue deposit)throws IOException{
        //creating instance of pretransaction balance
        double preTransaction = super.getAccBalance().doubleValue();
        MonetaryValue preAmount = new MonetaryValue(preTransaction);

        if(!deposit.isNegative()){
            if(Calendar.getInstance().after(maturityDate)){
                return new TransactionReceipt(ticket, super.getAccBalance().add(deposit), "none", preAmount, super.getAccBalance());
            }else{
                throw new CDMaturityDateException();
            }
        }else{
            throw new InvalidAmountException("Deposit");
        }

    }

    @Override
    public TransactionReceipt makeWithdrawal(TransactionTicket ticket, MonetaryValue withdrawal)throws IOException{     
        //creating instance of pretransaction balance
        double preTransaction = super.getAccBalance().doubleValue();
        MonetaryValue preAmount = new MonetaryValue(preTransaction);

        if(!withdrawal.isNegative()){
            if(Calendar.getInstance().after(maturityDate)){
                    if(super.getAccBalance().isLessThan(withdrawal)){
                        throw new InsufficientFundsException();
                    }
                    return new TransactionReceipt(ticket, super.getAccBalance().subtract(withdrawal), "none", preAmount, super.getAccBalance());
            }else{
                throw new CDMaturityDateException();
            }
        }else{
            throw new InvalidAmountException("Withdrawal");
        }
       
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof CDAccount){
            CDAccount other = (CDAccount)obj;
            return other.getDepositor().equals(this.getDepositor()) && other.getAccNum() == this.getAccNum() 
            && other.getAccBalance() == this.getAccBalance() && other.getAcctType() == this.getAcctType();
        }else{
            return false;
        }
    }

    @Override
    public String toString(){
        return super.getDepositor().toString() + "             " + super.getAccNum() + "              " + super.getAcctType() + "                 " 
        + (super.getStatus()? "Open":"Closed") + "        " + super.getAccBalance().toString() + "        " +  Bank.dateToString(maturityDate);  
    }

    public void setMaturityDate(int Choice){
        maturityDate.add(Calendar.MONTH, Choice);
    }
}
