import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Bank {

    private static MonetaryValue totalAmountInSavingsAccts;
    private static MonetaryValue totalAmountInCheckingsAccts;
    private static MonetaryValue totalAmountInCDAccts;
    private static MonetaryValue totalAmountInAllAccts;
    
    private ArrayList<Account> accounts;

    private static ArrayList<Integer> acctNumSortKey;
    private static ArrayList<Integer> ssnSortKey;
    private static ArrayList<Integer> nameSortKey;

    Bank(){
        totalAmountInSavingsAccts = new MonetaryValue();
        totalAmountInCheckingsAccts = new MonetaryValue();
        totalAmountInCDAccts = new MonetaryValue();
        totalAmountInAllAccts = new MonetaryValue();
        accounts = new ArrayList<Account>();

        acctNumSortKey = new ArrayList<Integer>();
        ssnSortKey = new ArrayList<Integer>();
        nameSortKey = new ArrayList<Integer>();

    }

    private void sortAccountsByAcctNum(){
        //utilizes Quick Sort Algorithm
        //sort in ascending order
        //Runtime Complexity:
        //worst Case: O(n^2)
        //Average case is O(n log n)
        //performance depends largely on pivot selection
        setNumAcctsSort();

        QuickSort object = new QuickSort(accounts);

        Integer[] arr = new Integer[acctNumSortKey.size()];
        arr = acctNumSortKey.toArray(arr);

        object.Sort(arr, 0, arr.length - 1);

        for(int i = 0; i < arr.length; i++){
            acctNumSortKey.set(i, arr[i]);
        }
    }

    public ArrayList<Integer> getAcctNumSortKey(){

        sortAccountsByAcctNum();
        return new ArrayList<Integer>(acctNumSortKey);
    }

    private void sortAccountsBySSN(){
        //utilizes Bubble Sort Algorithm
        //Runtime complexity: O(n^2) (useful with relatively small data)
        setNumAcctsSort();
        int temp;

        for(int lastPos = accounts.size()-1; lastPos >= 0; lastPos--){
            for(int pos = 0; pos < lastPos; pos++){
                int presentSSN = accounts.get(ssnSortKey.get(pos)).getDepositor().getSSN();
                int nextSSN = accounts.get(ssnSortKey.get(pos + 1)).getDepositor().getSSN();

                if(presentSSN > nextSSN){
                    temp = ssnSortKey.get(pos);
                    ssnSortKey.set(pos, ssnSortKey.get(pos + 1));
                    ssnSortKey.set(pos + 1, temp);
                }

            }
        }

    }
    
    public ArrayList<Integer> getSSNSortKey(){
        sortAccountsBySSN();
        return new ArrayList<Integer>(ssnSortKey);
    }

    private void sortAccountsByName(){
        //utilizes Insertion Sort Algorithm
        //Runtime Complexity: O(n^2) (useful for small data sets)
        //The utilization of nexted loops to shift items into place takes time

        setNumAcctsSort();

        int i, j;
        int temp;

        for(i = 1; i < getNumAccts(); i++){
            String name = accounts.get(nameSortKey.get(i)).getDepositor().getName().toString();
            int case1 = (int)name.charAt(4);
            int case2 = (int)name.charAt(5);

            j = i - 1;
            while(j >= 0 && case1 <= (int)accounts.get(nameSortKey.get(j)).getDepositor().getName().toString().charAt(4)){
                String comparison = accounts.get(nameSortKey.get(j)).getDepositor().getName().toString();

                if(case1 == (int)comparison.charAt(4)){
                    if(case2 < (int)comparison.charAt(5)){
                        temp = nameSortKey.get(j);
                        nameSortKey.set(j, nameSortKey.get(j + 1));
                        nameSortKey.set(j + 1, temp);
                    }
                    j--;
                }else{
                    temp = nameSortKey.get(j);
                    nameSortKey.set(j, nameSortKey.get(j + 1));
                    nameSortKey.set(j + 1, temp);
                    j--;
                }
            }
        }
    }

    public ArrayList<Integer> getNameSortKey(){
        sortAccountsByName();
        return new ArrayList<Integer>(nameSortKey);
    }

    public void setNumAcctsSort(){
        acctNumSortKey.clear();
        ssnSortKey.clear();
        nameSortKey.clear();

        for(int i = 0; i < getNumAccts(); i++){
            acctNumSortKey.add(i);
            ssnSortKey.add(i);
            nameSortKey.add(i);
        }
    }

    public void totalsManipulation(String type, char transaction, MonetaryValue amount){
        if(type.equals("Savings")){
            switch(transaction){
                case 'd':case 'D':
                    totalAmountInSavingsAccts.add(amount);
                    totalAmountInAllAccts.add(amount);
                    break;
                case 'w':case 'W':
                    totalAmountInSavingsAccts.subtract(amount);
                    totalAmountInAllAccts.subtract(amount);
                    break;
                default:
                    break;
            }
        }
        if(type.equals("Checking")){
            switch(transaction){
                case 'd':case 'D':
                    totalAmountInCheckingsAccts.add(amount);
                    totalAmountInAllAccts.add(amount);
                    break;
                case 'w':case 'W':
                    totalAmountInCheckingsAccts.subtract(amount);
                    totalAmountInAllAccts.subtract(amount);
                    break;
                default:
                    break;
            }     
        }
        if(type.equals("CD")){
            switch(transaction){
                case 'd':case 'D':
                    totalAmountInCDAccts.add(amount);
                    totalAmountInAllAccts.add(amount);
                    break;
                case 'w':case 'W':
                    totalAmountInCDAccts.subtract(amount);
                    totalAmountInAllAccts.subtract(amount);
                    break;
                default:
                    break;
            }
        }
    }

    public MonetaryValue getTotal(String type){
        if(type.equals("Savings")){
            return totalAmountInSavingsAccts;
        }
        if(type.equals("Checking")){
           return totalAmountInCheckingsAccts;
        }
        if(type.equals("CD")){
            return totalAmountInCDAccts;
        }
        else if(type.equals("Total")){
            return totalAmountInAllAccts;
        }
        return null;
    }

    public Boolean addAccount(Account person){
        accounts.add(person);
        return true;
    }

    public TransactionReceipt openNewAccount(TransactionTicket a, Account object){  
        return new TransactionReceipt(a, addAccount(object), "None", object.getAccBalance(), object.getAccBalance());
    }

    public TransactionReceipt deleteAccount(TransactionTicket a, Account object) throws IOException{
        if(object.getAccBalance().isGreaterThan(new MonetaryValue(0.0))){
            throw new InvalidAccountDeletionException();
        }else{
            accounts.remove(object);
            return new TransactionReceipt(a, true, "none", object.getAccBalance(), new MonetaryValue());
        }
        

    }
    
    public boolean accountExists(int accountnNumber)throws IOException{
        for(int i = 0; i < accounts.size(); i++){
            if(accounts.get(i).getAccNum() == accountnNumber){
                throw new AccountExistsException();
            }
        }
        return false;
    }

    public int findAcct(int identifier)throws IOException{
        boolean found = false;

        for(int i = 0; i < accounts.size(); i++){
            if(identifier > 10000000){
                if(accounts.get(i).getDepositor().getSSN() == identifier){
                    found = true;
                    return i;
                }
            }else{
                if(accounts.get(i).getAccNum() == identifier){
                    found = true;
                    return i;
                } 
            }
        }

        if(found == false && identifier > 10000000){
            throw new InvalidAccountException(identifier, "SSN ");
        }
        if(found == false){
            throw new InvalidAccountException(identifier, "Account Number ");
        }
        return 0;
        
    }

    public Account getAcct(int index){

        Account object = accounts.get(index);

        if(object.getAcctType().equals("CD")){
            
            object = new CDAccount((CDAccount)object);

        }if(object.getAcctType().equals("Savings")){

            object = new SavingsAccount((SavingsAccount)object);

        }if(object.getAcctType().equals("Checkings")){

            object = new CheckingsAccount((CheckingsAccount)object);
        }

        return object;
    }

    public int getNumAccts(){
        int totalAccounts = 0;

        Account obj = new Account();
        for(int i = 0; i < accounts.size(); i++){
            if(accounts.get(i).equals(obj));
            totalAccounts++;
        }
        return totalAccounts;
    }

    public static String dateToString(Calendar date){
        Date time = date.getTime();
        String format = (time.getMonth() + 1) + "/" + time.getDate() + "/" + (time.getYear() + 1900);

        return format;
    }

}
