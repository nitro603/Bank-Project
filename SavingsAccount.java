public class SavingsAccount extends Account{
   
    
    SavingsAccount(){
        super();
    }
    SavingsAccount(Account object){
        super(object);
    }
    @Override
    public boolean equals(Object obj){
        if(obj instanceof SavingsAccount){
            SavingsAccount other = (SavingsAccount)obj;
            return other.getDepositor().equals(this.getDepositor()) && other.getAccNum() == this.getAccNum() 
            && other.getAccBalance() == this.getAccBalance() && other.getAcctType() == this.getAcctType();
        }else{
            return false;
        }
    }
}
