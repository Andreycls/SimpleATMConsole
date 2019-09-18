package projek.Model;

import java.util.Date;

public class Account {
    String accountName;
    String accountAddress;
    String accountJob;
    Date accountDateBirth;
    String accountPin;
    int amount;


    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountAddress() {
        return accountAddress;
    }
    public void setAccountAddress(String accountAddress) {
        this.accountAddress = accountAddress;
    }

    public String getAccountJob() {
        return accountJob;
    }
    public void setAccountJob(String accountJob) {
        this.accountJob = accountJob;
    }

    public Date getAccountDateBirth() {
        return accountDateBirth;
    }
    public void setAccountDateBirth(Date accountDateBirth) {
        this.accountDateBirth = accountDateBirth;
    }

    public String getAccountPin() {
        return accountPin;
    }
    public void setAccountPin(String accountJob) {
        this.accountPin = accountPin;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }


}
