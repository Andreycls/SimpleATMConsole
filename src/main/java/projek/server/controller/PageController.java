package projek.server.controller;

import org.jpos.iso.ISOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projek.Model.Account;



import java.sql.Statement;
import java.util.List;

@Controller
public class PageController {
    DatabaseController dbCon = new DatabaseController();
    AccountController authCon = new AccountController();
    ResponseController response = new ResponseController();



    @RequestMapping(method = RequestMethod.GET, value="/account/allaccount")
    @ResponseBody
    public List<Account> getAllAccount() {
        return dbCon.DBToAccountList();
    }

    @RequestMapping(method = RequestMethod.GET, value="/account/check/{accountNumber}")
    @ResponseBody
    public boolean checkAccountNumber(@PathVariable String accountNumber) {

        return authCon.checkAccountNumber((Statement)dbCon.connectToDB().get(1),accountNumber);
    }
    @RequestMapping(method = RequestMethod.POST, value="/account/{accountNumber}/balance")
    @ResponseBody
    public String checkBalanceInquiry(@PathVariable String accountNumber, @RequestBody String reqBody) throws ISOException {
        System.out.println(reqBody);
//        producer.produceMessage(reqBody);
        return response.getAccountBalanceResponse((Statement)dbCon.connectToDB().get(1),accountNumber,reqBody);

    }

    @RequestMapping(method = RequestMethod.POST, value="/account/{accountNumber}/withdraw")
    @ResponseBody
    public String cashWithdrawal(@PathVariable String accountNumber, @RequestBody String reqBody) throws ISOException {
        System.out.println(reqBody);
        return response.getCashWithdrawalResponse((Statement)dbCon.connectToDB().get(1),accountNumber,reqBody);
    }

    @RequestMapping(method = RequestMethod.POST, value="/account/{accountNumber}/transfer_inquiry")
    @ResponseBody
    public String transferInquiry(@PathVariable String accountNumber, @RequestBody String reqBody) throws ISOException {
        System.out.println(reqBody);
        return response.getTransferInquiryResponse((Statement)dbCon.connectToDB().get(1),accountNumber,reqBody);
    }

    @RequestMapping(method = RequestMethod.POST, value="/account/{accountNumber}/transfer")
    @ResponseBody
    public String transfer(@PathVariable String accountNumber, @RequestBody String reqBody) throws ISOException {
        System.out.println(reqBody);
        return response.getTransferResponse((Statement)dbCon.connectToDB().get(1),accountNumber,reqBody);
    }


    @RequestMapping(method = RequestMethod.POST, value="/account/{accountNumber}/payment_inquiry/PAM")
    @ResponseBody
    public String paymentInquiryPAM(@PathVariable String accountNumber, @RequestBody String reqBody) throws ISOException {
        System.out.println(reqBody);
        return response.getPaymentInquiryResponsePAM((Statement)dbCon.connectToDB().get(1),accountNumber,reqBody);
    }

    @RequestMapping(method = RequestMethod.POST, value="/account/{accountNumber}/payment/PAM")
    @ResponseBody
    public String paymentPAM(@PathVariable String accountNumber, @RequestBody String reqBody) throws ISOException {
        System.out.println(reqBody);
        return response.getPaymentResponsePAM((Statement)dbCon.connectToDB().get(1),accountNumber,reqBody);
    }

    @RequestMapping(method = RequestMethod.POST, value="/account/{accountNumber}/purchase_inquiry/PLN")
    @ResponseBody
    public String purchaseInquiryPLN(@PathVariable String accountNumber, @RequestBody String reqBody) throws ISOException {
        System.out.println(reqBody);
        return response.getPurchaseInquiryResponsePLN((Statement)dbCon.connectToDB().get(1),accountNumber,reqBody);

    }

    @RequestMapping(method = RequestMethod.POST, value="/account/{accountNumber}/purchase/PLN")
    @ResponseBody
    public String purchasePLN(@PathVariable String accountNumber, @RequestBody String reqBody) throws ISOException {
        System.out.println(reqBody);
        return response.getPurchaseResponsePLN((Statement)dbCon.connectToDB().get(1),accountNumber,reqBody);
    }

    @RequestMapping(method = RequestMethod.POST, value="/account/{accountNumber}/transfer_amount")
    @ResponseBody
    public void setAmount(@PathVariable String accountNumber, @RequestBody String amount) throws ISOException {
         response.setAmount(Integer.parseInt(amount),accountNumber);
    }









}
