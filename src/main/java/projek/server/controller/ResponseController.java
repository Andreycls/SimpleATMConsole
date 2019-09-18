package projek.server.controller;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import projek.client.controller.ActionController;

import java.io.InputStream;
import java.sql.Statement;

public class ResponseController {
    AccountController accountController = new AccountController();
    DatabaseController dbCon = new DatabaseController();
    private static final Logger logger = LoggerFactory.getLogger(ActionController.class);

    public String getAccountBalanceResponse(Statement stmt, String accountNumber, String reqBody) throws ISOException {
        InputStream in = AccountController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(reqBody.getBytes());
        int balanceInquiry = accountController.getAccountBalance( accountNumber);
        for (int i = 1; i <= isoMsg.getMaxField(); i++) {
            if (isoMsg.hasField(i)) {
                isoMsg.set(i, isoMsg.getString(i));
            }
        }
        isoMsg.setMTI("0210");
        isoMsg.set(54, String.valueOf(balanceInquiry));
        String PINData = isoMsg.getString(52);

        if (accountController.checkAccountPIN(stmt, accountNumber, PINData)) {
            isoMsg.set(39,"00");

        } else {
            isoMsg.set(39,"77");

        }
        byte[] result = isoMsg.pack();
        logger.info("Balance inquiry : {} with balance {}",accountNumber,balanceInquiry);
        return new String(result);




    }

    public String getCashWithdrawalResponse(Statement stmt, String accountNumber, String reqBody) throws ISOException {
        InputStream in = AccountController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(reqBody.getBytes());
        int balanceInquiry = accountController.getAccountBalance( accountNumber);
        for (int i = 1; i <= isoMsg.getMaxField(); i++) {
            if (isoMsg.hasField(i)) {
                isoMsg.set(i, isoMsg.getString(i));
            }
        }
        isoMsg.setMTI("0210");
        String PINData = isoMsg.getString(52);
        int amount = balanceInquiry - Integer.parseInt(isoMsg.getString(4));
        if ((accountController.checkAccountPIN(stmt, accountNumber, PINData))&&amount>0) {
            isoMsg.set(39,"00");

            dbCon.setAmount((Statement)dbCon.connectToDB().get(1),amount,accountNumber);
        }
        else if(Integer.parseInt(isoMsg.getString(4))>balanceInquiry){
            isoMsg.set(39,"51");
        }else if(!accountController.checkAccountPIN(stmt, accountNumber, PINData)) {
            isoMsg.set(39,"77");
        }
        isoMsg.set(54, String.valueOf(balanceInquiry));
        byte[] result = isoMsg.pack();
        String res = new String(result);
        logger.info("Cash Withdrawal : {} trying cash withdraw {}, balance {}",accountNumber,isoMsg.getString(4),balanceInquiry);
        return res;
    }


    public String getTransferResponse(Statement stmt, String accountNumber, String reqBody) throws ISOException {

        InputStream in = AccountController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(reqBody.getBytes());
        String accountNumberBeneficiary = isoMsg.getString(62);
        int balanceInquiry = accountController.getAccountBalance( accountNumber);
        int balanceInquiryBeneficiary = accountController.getAccountBalance( accountNumberBeneficiary);

        for (int i = 1; i <= isoMsg.getMaxField(); i++) {
            if (isoMsg.hasField(i)) {
                isoMsg.set(i, isoMsg.getString(i));
            }
        }
        isoMsg.setMTI("0210");
        System.out.println(isoMsg.getString(4));
        int amount = balanceInquiry - Integer.parseInt(isoMsg.getString(4));
        int newAmount = balanceInquiryBeneficiary + Integer.parseInt(isoMsg.getString(4));
        System.out.println(amount);
        System.out.println(newAmount);
        dbCon.setAmount((Statement)dbCon.connectToDB().get(1),amount,accountNumber);
        dbCon.setAmount((Statement)dbCon.connectToDB().get(1),newAmount,accountNumberBeneficiary);
        String PINData = isoMsg.getString(52);
        if(Integer.parseInt(isoMsg.getString(4))>balanceInquiry){
            isoMsg.set(39,"51");

        }
        if (accountController.checkAccountPIN(stmt, accountNumber, PINData)) {
            isoMsg.set(39,"00");
        }else{
            isoMsg.set(39,"77");
        }
        isoMsg.set(54, String.valueOf(balanceInquiry));
        isoMsg.set(4,isoMsg.getString(4));
        byte[] result = isoMsg.pack();
        String res = new String(result);
        logger.info("Transfer Response : {} trying transfer to {} amount {}",accountNumber,accountNumberBeneficiary,isoMsg.getString(4));
        return res;


    }



    public String getTransferInquiryResponse(Statement stmt, String accountNumber, String reqBody) throws ISOException {

        InputStream in = AccountController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(reqBody.getBytes());
        String accountNumberBeneficiary = isoMsg.getString(62);
        String accountNameBeneficiary = accountController.getAccountName(stmt,accountNumberBeneficiary);
        int balanceInquiry = accountController.getAccountBalance( accountNumber);
        int balanceInquiryBeneficiary = accountController.getAccountBalance( accountNumberBeneficiary);

        for (int i = 1; i <= isoMsg.getMaxField(); i++) {
            if (isoMsg.hasField(i)) {
                isoMsg.set(i, isoMsg.getString(i));
            }
        }
        isoMsg.setMTI("0210");
        int amount = balanceInquiry - Integer.parseInt(isoMsg.getString(4));
        int newAmount = balanceInquiryBeneficiary + Integer.parseInt(isoMsg.getString(4));
        isoMsg.set(103,accountNameBeneficiary);
        String PINData = isoMsg.getString(52);

        if ((accountController.checkAccountPIN(stmt, accountNumber, PINData))&&amount>0) {
            isoMsg.set(39,"00");

        }
        else if(Integer.parseInt(isoMsg.getString(4))>balanceInquiry){
            isoMsg.set(39,"51");
        }else if(!accountController.checkAccountPIN(stmt, accountNumber, PINData)) {
            isoMsg.set(39,"77");
        }
        isoMsg.set(54, String.valueOf(balanceInquiry));
        isoMsg.set(4,isoMsg.getString(4));

        byte[] result = isoMsg.pack();
        String res = new String(result);
        logger.info("Transfer Inquiry : {} trying transfer to {} amount {}",accountNumber,accountNumberBeneficiary,isoMsg.getString(4));


        return res;
    }


    public String getPaymentInquiryResponsePAM(Statement stmt, String accountNumber, String reqBody) throws ISOException {

        InputStream in = AccountController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(reqBody.getBytes());
        String accountNumberBeneficiary = isoMsg.getString(62);
        String accountNameBeneficiary = accountController.getAccountName(stmt,accountNumberBeneficiary);
        int balanceInquiry = accountController.getAccountBalance( accountNumber);
        int balanceInquiryBeneficiary = accountController.getAccountBalance( accountNumberBeneficiary);

        for (int i = 1; i <= isoMsg.getMaxField(); i++) {
            if (isoMsg.hasField(i)) {
                isoMsg.set(i, isoMsg.getString(i));
            }
        }
        isoMsg.setMTI("0210");
        int amount = balanceInquiry - Integer.parseInt(isoMsg.getString(4));
        int newAmount = balanceInquiryBeneficiary + Integer.parseInt(isoMsg.getString(4));
        isoMsg.set(103,accountNameBeneficiary);
        String PINData = isoMsg.getString(52);

        if ((accountController.checkAccountPIN(stmt, accountNumber, PINData))&&amount>0) {
            isoMsg.set(39,"00");

        }
        else if(Integer.parseInt(isoMsg.getString(4))>balanceInquiry){
            isoMsg.set(39,"51");
        }else if(!accountController.checkAccountPIN(stmt, accountNumber, PINData)) {
            isoMsg.set(39,"77");
        }


        isoMsg.set(54, String.valueOf(balanceInquiry));
        isoMsg.set(4,isoMsg.getString(4));

        byte[] result = isoMsg.pack();
        String res = new String(result);
        logger.info("Payment Inquiry PDAM : {} trying transfer to {} amount {}",accountNumber,accountNumberBeneficiary,isoMsg.getString(4));

        return res;
    }

    public String getPaymentResponsePAM(Statement stmt, String accountNumber, String reqBody) throws ISOException {

        InputStream in = AccountController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(reqBody.getBytes());
        String accountNumberBeneficiary = isoMsg.getString(62);
        int balanceInquiry = accountController.getAccountBalance( accountNumber);
        int balanceInquiryBeneficiary = accountController.getAccountBalance( accountNumberBeneficiary);
        System.out.println("Balance Pengirim : "+balanceInquiry);
        System.out.println("Balance Penerima : "+balanceInquiryBeneficiary);
        for (int i = 1; i <= isoMsg.getMaxField(); i++) {
            if (isoMsg.hasField(i)) {
                isoMsg.set(i, isoMsg.getString(i));
            }
        }
        isoMsg.setMTI("0210");
        int amount = balanceInquiry - Integer.parseInt(isoMsg.getString(4));
        int newAmount = balanceInquiryBeneficiary + Integer.parseInt(isoMsg.getString(4));
        dbCon.setAmount((Statement)dbCon.connectToDB().get(1),amount,accountNumber);

        dbCon.setAmount((Statement)dbCon.connectToDB().get(1),newAmount,accountNumberBeneficiary);
        String PINData = isoMsg.getString(52);
        if(Integer.parseInt(isoMsg.getString(4))>balanceInquiry){
            isoMsg.set(39,"51");

        }
        if (accountController.checkAccountPIN(stmt, accountNumber, PINData)) {
            isoMsg.set(39,"00");
        }
        else{
            isoMsg.set(39,"77");
        }
        isoMsg.set(54, String.valueOf(balanceInquiry));
        isoMsg.set(4,isoMsg.getString(4));
        byte[] result = isoMsg.pack();
        String res = new String(result);
        logger.info("Payment PDAM : {} trying transfer to {} amount {}",accountNumber,accountNumberBeneficiary,isoMsg.getString(4));
        return res;

    }




    public String getPurchaseInquiryResponsePLN(Statement stmt, String accountNumber, String reqBody) throws ISOException {

        InputStream in = AccountController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(reqBody.getBytes());
        String accountNumberBeneficiary = isoMsg.getString(62);
        String accountNameBeneficiary = accountController.getAccountName(stmt,accountNumberBeneficiary);
        int balanceInquiry = accountController.getAccountBalance( accountNumber);
        int balanceInquiryBeneficiary = accountController.getAccountBalance( accountNumberBeneficiary);

        for (int i = 1; i <= isoMsg.getMaxField(); i++) {
            if (isoMsg.hasField(i)) {
                isoMsg.set(i, isoMsg.getString(i));
            }
        }
        isoMsg.setMTI("0210");


        isoMsg.set(103,accountNameBeneficiary);
        String PINData = isoMsg.getString(52);
        System.out.println("BALANCE "+balanceInquiry);
        System.out.println("cash "+Integer.parseInt(isoMsg.getString(4)));
        int amount = balanceInquiry - Integer.parseInt(isoMsg.getString(4));
        if ((accountController.checkAccountPIN(stmt, accountNumber, PINData))&&amount>0) {
            isoMsg.set(39,"00");

            ///dbCon.setAmount((Statement)dbCon.connectToDB().get(1),amount,accountNumber);
        }
        else if(Integer.parseInt(isoMsg.getString(4))>balanceInquiry){
            isoMsg.set(39,"51");
        }else if(!accountController.checkAccountPIN(stmt, accountNumber, PINData)) {
            isoMsg.set(39,"77");
        }

        System.out.println("cash "+isoMsg.getString(39));


        isoMsg.set(54, String.valueOf(balanceInquiry));


        byte[] result = isoMsg.pack();
        String resISO = new String(result);
        logger.info("Payment Inquiry PLN : {} trying transfer to {} amount {}",accountNumber,accountNumberBeneficiary,isoMsg.getString(4));


        return resISO;
    }

    public String getPurchaseResponsePLN(Statement stmt, String accountNumber, String reqBody) throws ISOException {

        InputStream in = AccountController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(reqBody.getBytes());
        String accountNumberBeneficiary = isoMsg.getString(62);
        int balanceInquiry = accountController.getAccountBalance( accountNumber);
        int balanceInquiryBeneficiary = accountController.getAccountBalance( accountNumberBeneficiary);
        System.out.println("Balance Pengirim : "+balanceInquiry);
        System.out.println("Balance Penerima : "+balanceInquiryBeneficiary);
        for (int i = 1; i <= isoMsg.getMaxField(); i++) {
            if (isoMsg.hasField(i)) {
                isoMsg.set(i, isoMsg.getString(i));
            }
        }
        isoMsg.setMTI("0210");
        int amount = balanceInquiry - Integer.parseInt(isoMsg.getString(4));
        int newAmount = balanceInquiryBeneficiary + Integer.parseInt(isoMsg.getString(4));
        dbCon.setAmount((Statement)dbCon.connectToDB().get(1),amount,accountNumber);

        dbCon.setAmount((Statement)dbCon.connectToDB().get(1),newAmount,accountNumberBeneficiary);
        String PINData = isoMsg.getString(52);
        if(Integer.parseInt(isoMsg.getString(4))>balanceInquiry){
            isoMsg.set(39,"51");

        }
        if (accountController.checkAccountPIN(stmt, accountNumber, PINData)) {
            isoMsg.set(39,"00");
        }
        else{
            isoMsg.set(39,"77");
        }
        isoMsg.set(54, String.valueOf(balanceInquiry));
        isoMsg.set(4,isoMsg.getString(4));
        byte[] result = isoMsg.pack();
        String res = new String(result);
        logger.info("Payment PLN : {} trying transfer to {} amount {}",accountNumber,accountNumberBeneficiary,isoMsg.getString(4));

        return res;

    }

    public void setAmount(int amount, String accountNumber){
        int balanceInquiry = accountController.getAccountBalance( accountNumber);
        int newBalance = balanceInquiry-(amount+5500);
        dbCon.setAmount((Statement) dbCon.connectToDB().get(1),newBalance,accountNumber);
    }





















}
