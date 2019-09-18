package projek.client.controller;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class ActionController {
    private static final Logger logger = LoggerFactory.getLogger(ActionController.class);

        ResourcesController res = new ResourcesController();
        String URL = "http://localhost:8086/account/";

    public String balanceInquiry(String primaryAccountNumber, String PINData) {
        String resultISO="";
        try{
            String MTI = "0200";
            InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
            GenericPackager packager = new GenericPackager(in);

            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI(MTI);
            isoMsg.set(1,"000");
            isoMsg.set(2,primaryAccountNumber);
            isoMsg.set(3,"300000");
            isoMsg.set(4,"000");
            isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
            isoMsg.set(11,"0000");
            isoMsg.set(12, new SimpleDateFormat("hhmmss").format(new Date()));
            isoMsg.set(13, new SimpleDateFormat("mmdd").format(new Date()));
            isoMsg.set(15,new SimpleDateFormat("mmdd").format(new Date()));
            isoMsg.set(18,"0000");
            isoMsg.set(32,"00000000000");
            isoMsg.set(33,"00000000000");
            isoMsg.set(37,"000000000000");
            isoMsg.set(41,"00008086");
            isoMsg.set(42,"000000000000000");
            isoMsg.set(43,"0000000000000000000000000000000000000000");
            isoMsg.set(49,"000");
            isoMsg.set(52,PINData);
            isoMsg.set(54,"000");
            isoMsg.set(62,"000");
            isoMsg.set(102,"0000000000000000000000000000");
            isoMsg.set(128,"0000000000000000");
            //printISOMessage(isoMsg);
            byte[] result = isoMsg.pack();
            resultISO =  new String(result);
            logger.info("Build ISO Message for Balance Inquiry");
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return resultISO;
    }

    public String getAccountBalance(String reqBody) throws ISOException {
        InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(reqBody.getBytes());
        return isoMsg.getString(54);
    }

    public String getAccountNameBeneficiary(String reqBody) throws ISOException {
        InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(reqBody.getBytes());
        return isoMsg.getString(103);
    }
    public String getAccountNumberBeneficiary(String reqBody) throws ISOException {
        InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(reqBody.getBytes());
        return isoMsg.getString(62);
    }

    public void checkResponseCode(String reqBody) throws ISOException {
        InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(reqBody.getBytes());
        if(isoMsg.getString(39).equals("00")){
            System.out.println("Sukses");
        } else if(isoMsg.getString(39).equals("51")){
            System.out.println("Saldo tidak cukup");
        }
        else if(isoMsg.getString(39).equals("77")){
            System.out.println("Pin Salah");
        }else{
            System.out.println("SALAH");
        }
    }

    public String checkAuthorize(String reqBody) throws ISOException {

        InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(reqBody.getBytes());

        return isoMsg.getString(39);


    }

    public boolean checkPIN(String reqBody) throws ISOException {
        boolean checker = false;
        InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(reqBody.getBytes());
        if(!isoMsg.getString(39).equals("77")){
            checker=true;
        }
        return checker;
    }



    public String cashWithdrawal(String primaryAccountNumber, String PINData, int cash) throws ISOException, IOException {
        String resultISO="";
        try{

        String MTI = "0200";
        InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.setMTI(MTI);
        isoMsg.set(1,"000");
        isoMsg.set(2,primaryAccountNumber);
        isoMsg.set(3,"010000");
        isoMsg.set(4, String.valueOf(cash));
        isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
        isoMsg.set(11,"0000");
        isoMsg.set(12, new SimpleDateFormat("hhmmss").format(new Date()));
        isoMsg.set(13, new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(15,new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(18,"0000");
        isoMsg.set(32,"00000000000");
        isoMsg.set(33,"00000000000");
        isoMsg.set(37,"000000000000");
        isoMsg.set(41,"00008086");
        isoMsg.set(42,"000000000000000");
        isoMsg.set(43,"0000000000000000000000000000000000000000");
        isoMsg.set(49,"000");
        isoMsg.set(52,PINData);
        isoMsg.set(54,"000");
        isoMsg.set(62,"000");
        isoMsg.set(102,"0000000000000000000000000000");
        isoMsg.set(128,"0000000000000000");

        byte[] result = isoMsg.pack();
         resultISO =  new String(result);
        logger.info("Build ISO Message for Cash Withdrawal");
    }catch (Exception e){
        logger.error(e.getMessage());
    }

        return resultISO;
    }



    public String transfer(String primaryAccountNumber, String PINData, int cash, String accountNumberTransfer) throws ISOException, IOException {
        String responseBody="";
        try {
            String MTI = "0200";
            InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
            GenericPackager packager = new GenericPackager(in);
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI(MTI);
            isoMsg.set(1, "000");
            isoMsg.set(2, primaryAccountNumber);
            isoMsg.set(3, "400000");
            isoMsg.set(4, String.valueOf(cash));
            isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
            isoMsg.set(11, "0000");
            isoMsg.set(12, new SimpleDateFormat("hhmmss").format(new Date()));
            isoMsg.set(13, new SimpleDateFormat("mmdd").format(new Date()));
            isoMsg.set(15, new SimpleDateFormat("mmdd").format(new Date()));
            isoMsg.set(18, "0000");
            isoMsg.set(32, "00000000000");
            isoMsg.set(33, "00000000000");
            isoMsg.set(37, "000000000000");
            isoMsg.set(41, "00008086");
            isoMsg.set(42, "000000000000000");
            isoMsg.set(43, "0000000000000000000000000000000000000000");
            isoMsg.set(49, "000");
            isoMsg.set(52, PINData);
            isoMsg.set(62, accountNumberTransfer);
            isoMsg.set(100, "002");//kode bank pemberi
            isoMsg.set(102, "0000000000000000000000000000");
            isoMsg.set(103, "0000000000000000000000000000");
            isoMsg.set(125, "0000000000000000");
            isoMsg.set(127, "002");//bankcode penerima
            isoMsg.set(128, "0000000000000000");

            logger.info("Build ISO Message for Transfer Same bank");

            byte[] result = isoMsg.pack();
            String res = new String(result);


            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL + primaryAccountNumber + "/transfer");
            logger.info("Try Post data to Server {}",URL + primaryAccountNumber + "/transfer");

            StringEntity entity = new StringEntity(res);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "text/plain");
            httpPost.setHeader("Content-type", "text/plain");
            CloseableHttpResponse response = client.execute(httpPost);

             responseBody = EntityUtils.toString(response.getEntity());

            client.close();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return responseBody;
    }

    public String transferSend(String primaryAccountNumber, String PINData, int cash, String accountNumberTransfer) throws ISOException, IOException {
        String MTI = "0200";
        InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);
        ISOMsg isoMsgs = new ISOMsg();
        isoMsgs.setPackager(packager);
        isoMsgs.setMTI(MTI);
        isoMsgs.set(1,"000");
        isoMsgs.set(2,primaryAccountNumber);
        isoMsgs.set(3,"400000");
        isoMsgs.set(4, String.valueOf(cash));
        isoMsgs.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
        isoMsgs.set(11,"0000");
        isoMsgs.set(12, new SimpleDateFormat("hhmmss").format(new Date()));
        isoMsgs.set(13, new SimpleDateFormat("mmdd").format(new Date()));
        isoMsgs.set(15,new SimpleDateFormat("mmdd").format(new Date()));
        isoMsgs.set(18,"0000");
        isoMsgs.set(32,"00000000000");
        isoMsgs.set(33,"00000000000");
        isoMsgs.set(37,"000000000000");
        isoMsgs.set(41,"00008086");
        isoMsgs.set(42,"000000000000000");
        isoMsgs.set(43,"0000000000000000000000000000000000000000");
        isoMsgs.set(49,"000");
        isoMsgs.set(52,PINData);
        isoMsgs.set(62,accountNumberTransfer);
        isoMsgs.set(100,"002");
        isoMsgs.set(102,"0000000000000000000000000000");
        isoMsgs.set(103,"0000000000000000000000000000");
        isoMsgs.set(125,"0000000000000000");
        isoMsgs.set(127,"002");
        isoMsgs.set(128,"0000000000000000");

        byte[] result = isoMsgs.pack();
        String res =  new String(result);

        return res;
    }

    public String transferInquiry(String primaryAccountNumber, String PINData, int cash, String accountNumberTransfer) throws ISOException, IOException {
        String responseBody="";
        try{

        String MTI = "0200";
        InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.setMTI(MTI);
        isoMsg.set(1,"000");
        isoMsg.set(2,primaryAccountNumber);
        isoMsg.set(3,"390000");
        isoMsg.set(4, String.valueOf(cash));
        isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
        isoMsg.set(11,"0000");
        isoMsg.set(12, new SimpleDateFormat("hhmmss").format(new Date()));
        isoMsg.set(13, new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(15,new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(18,"0000");
        isoMsg.set(32,"00000000000");
        isoMsg.set(33,"00000000000");
        isoMsg.set(37,"000000000000");
        isoMsg.set(41,"00008086");
        isoMsg.set(42,"000000000000000");
        isoMsg.set(43,"0000000000000000000000000000000000000000");
        isoMsg.set(49,"000");
        isoMsg.set(52,PINData);
        isoMsg.set(62,accountNumberTransfer);
        isoMsg.set(100,"002");//kode bank pemberi
        isoMsg.set(102,"0000000000000000000000000000");
        isoMsg.set(103,"0000000000000000000000000000");
        isoMsg.set(125,"0000000000000000");
        isoMsg.set(127,"002");//bankcode penerima
        isoMsg.set(128,"0000000000000000");

        byte[] result = isoMsg.pack();
        String res =  new String(result);
        logger.info("Build ISO Message for Transfer Inquiry Same bank");


        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(URL+primaryAccountNumber+"/transfer_inquiry");
        logger.info("Try Post data to Server {}",URL + primaryAccountNumber + "/transfer_inquiry");
        StringEntity entity = new StringEntity(res);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "text/plain");
        httpPost.setHeader("Content-type", "text/plain");
        CloseableHttpResponse response = client.execute(httpPost);
        responseBody = EntityUtils.toString(response.getEntity());
        client.close();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return responseBody;
    }


    public String paymentInquiryPAM(String primaryAccountNumber, String PINData) throws ISOException, IOException {
        String responseBody = "";
        try {
            String MTI = "0200";
            JSONObject PAMData = (JSONObject) res.getCompanyAccountNumber().get("PAM");
            InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
            GenericPackager packager = new GenericPackager(in);
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI(MTI);
            isoMsg.set(1, "000");
            isoMsg.set(2, primaryAccountNumber);
            isoMsg.set(3, "382101");
            isoMsg.set(4, String.valueOf(PAMData.get("amount")));
            isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
            isoMsg.set(11, "0000");
            isoMsg.set(12, new SimpleDateFormat("hhmmss").format(new Date()));
            isoMsg.set(13, new SimpleDateFormat("mmdd").format(new Date()));
            isoMsg.set(15, new SimpleDateFormat("mmdd").format(new Date()));
            isoMsg.set(18, "0000");
            isoMsg.set(32, "00000000000");
            isoMsg.set(33, "00000000000");
            isoMsg.set(37, "000000000000");
            isoMsg.set(41, "00008086");
            isoMsg.set(42, "000000000000000");
            isoMsg.set(43, "0000000000000000000000000000000000000000");
            isoMsg.set(49, "000");
            isoMsg.set(52, PINData);
            isoMsg.set(62, String.valueOf(PAMData.get("accountNumber")));
            isoMsg.set(100, "002");
            isoMsg.set(102, "0000000000000000000000000000");
            isoMsg.set(103, "0000000000000000000000000000");
            isoMsg.set(125, "0000000000000000");
            isoMsg.set(127, "002");//bankcode penerima
            isoMsg.set(128, "0000000000000000");

            byte[] result = isoMsg.pack();
            String res = new String(result);
            logger.info("Build ISO Message for Payment Inquiry PDAM");


            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL + primaryAccountNumber + "/payment_inquiry/PAM");
            logger.info("Try Post data to Server {}",URL + primaryAccountNumber + "/payment_inquiry/PAM");
            StringEntity entity = new StringEntity(res);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "text/plain");
            httpPost.setHeader("Content-type", "text/plain");
            CloseableHttpResponse response = client.execute(httpPost);
            //int statusCode = response.getEntity().getContent();
             responseBody = EntityUtils.toString(response.getEntity());

            client.close();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return responseBody;
    }

    public String paymentPAM(String primaryAccountNumber, String PINData) throws ISOException, IOException {
        String responseBody="";
        try{

        String MTI = "0200";
        JSONObject PAMData = (JSONObject) res.getCompanyAccountNumber().get("PAM");
        InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.setMTI(MTI);
        isoMsg.set(1,"000");
        isoMsg.set(2,primaryAccountNumber);
        isoMsg.set(3,"182101");
        isoMsg.set(4, String.valueOf(PAMData.get("amount")));
        isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
        isoMsg.set(11,"0000");
        isoMsg.set(12, new SimpleDateFormat("hhmmss").format(new Date()));
        isoMsg.set(13, new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(15,new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(18,"0000");
        isoMsg.set(32,"00000000000");
        isoMsg.set(33,"00000000000");
        isoMsg.set(37,"000000000000");
        isoMsg.set(41,"00008086");
        isoMsg.set(42,"000000000000000");
        isoMsg.set(43,"0000000000000000000000000000000000000000");
        isoMsg.set(49,"000");
        isoMsg.set(52,PINData);
        isoMsg.set(62,String.valueOf(PAMData.get("accountNumber")));
        isoMsg.set(100,"002");
        isoMsg.set(102,"0000000000000000000000000000");
        isoMsg.set(103,"0000000000000000000000000000");
        isoMsg.set(125,"0000000000000000");
        isoMsg.set(127,"002");//bankcode penerima
        isoMsg.set(128,"0000000000000000");

        byte[] result = isoMsg.pack();
        String res =  new String(result);
        logger.info("Build ISO Message for Payment PDAM");

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(URL+primaryAccountNumber+"/payment/PAM");
        logger.info("Try Post data to Server {}",URL + primaryAccountNumber + "/payment/PAM");
        StringEntity entity = new StringEntity(res);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "text/plain");
        httpPost.setHeader("Content-type", "text/plain");
        CloseableHttpResponse response = client.execute(httpPost);
        //int statusCode = response.getEntity().getContent();
         responseBody = EntityUtils.toString(response.getEntity());

        client.close();

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return responseBody;
    }
    public String paymentPAMSend(String primaryAccountNumber, String PINData) throws ISOException, IOException {
        String MTI = "0200";
        JSONObject PAMData = (JSONObject) res.getCompanyAccountNumber().get("PAM");
        InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.setMTI(MTI);
        isoMsg.set(1,"000");
        isoMsg.set(2,primaryAccountNumber);
        isoMsg.set(3,"182101");
        isoMsg.set(4, String.valueOf(PAMData.get("amount")));
        isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
        isoMsg.set(11,"0000");
        isoMsg.set(12, new SimpleDateFormat("hhmmss").format(new Date()));
        isoMsg.set(13, new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(15,new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(18,"0000");
        isoMsg.set(32,"00000000000");
        isoMsg.set(33,"00000000000");
        isoMsg.set(37,"000000000000");
        isoMsg.set(41,"00008086");
        isoMsg.set(42,"000000000000000");
        isoMsg.set(43,"0000000000000000000000000000000000000000");
        isoMsg.set(49,"000");
        isoMsg.set(52,PINData);
        isoMsg.set(62,String.valueOf(PAMData.get("accountNumber")));
        isoMsg.set(100,"002");
        isoMsg.set(102,"0000000000000000000000000000");
        isoMsg.set(103,"0000000000000000000000000000");
        isoMsg.set(125,"0000000000000000");
        isoMsg.set(127,"002");//bankcode penerima
        isoMsg.set(128,"0000000000000000");

        byte[] result = isoMsg.pack();
        String resISO =  new String(result);

        return resISO;
    }

    public String purchaseInquiryPLN(String primaryAccountNumber, String PINData, int cash) throws ISOException, IOException {
        String responseBody="";
        try{

        String MTI = "0200";
        JSONObject PLNData = (JSONObject) res.getCompanyAccountNumber().get("PLN");

        InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.setMTI(MTI);
        isoMsg.set(1,"000");
        isoMsg.set(2,primaryAccountNumber);
        isoMsg.set(3,"382102");
        isoMsg.set(4, String.valueOf(cash));
        isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
        isoMsg.set(11,"0000");
        isoMsg.set(12, new SimpleDateFormat("hhmmss").format(new Date()));
        isoMsg.set(13, new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(15,new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(18,"0000");
        isoMsg.set(32,"00000000000");
        isoMsg.set(33,"00000000000");
        isoMsg.set(37,"000000000000");
        isoMsg.set(41,"00008086");
        isoMsg.set(42,"000000000000000");
        isoMsg.set(43,"0000000000000000000000000000000000000000");
        isoMsg.set(49,"000");
        isoMsg.set(52,PINData);
        isoMsg.set(62,String.valueOf(PLNData.get("accountNumber")));
        isoMsg.set(100,"002");  //kode bank pemberi
        isoMsg.set(102,"0000000000000000000000000000");
        isoMsg.set(103,"0000000000000000000000000000");
        isoMsg.set(125,"0000000000000000");
        isoMsg.set(127,"002");  //bankcode penerima
        isoMsg.set(128,"0000000000000000");

        byte[] result = isoMsg.pack();
        String res =  new String(result);
        logger.info("Build ISO Message for Purchase Inquiry PLN");

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(URL+primaryAccountNumber+"/purchase_inquiry/PLN");
        logger.info("Try Post data to Server {}",URL+primaryAccountNumber+"/purchase_inquiry/PLN");
        StringEntity entity = new StringEntity(res);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "text/plain");
        httpPost.setHeader("Content-type", "text/plain");
        CloseableHttpResponse response = client.execute(httpPost);
        //int statusCode = response.getEntity().getContent();
         responseBody = EntityUtils.toString(response.getEntity());

        client.close();

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return responseBody;
    }

    public String purchasePLN(String primaryAccountNumber, String PINData, int cash) throws ISOException, IOException {
        String responseBody="";
        try{

        String MTI = "0200";
        JSONObject PLNData = (JSONObject) res.getCompanyAccountNumber().get("PLN");

        InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.setMTI(MTI);
        isoMsg.set(1,"000");
        isoMsg.set(2,primaryAccountNumber);
        isoMsg.set(3,"182102");
        isoMsg.set(4, String.valueOf(cash));
        isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
        isoMsg.set(11,"0000");
        isoMsg.set(12, new SimpleDateFormat("hhmmss").format(new Date()));
        isoMsg.set(13, new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(15,new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(18,"0000");
        isoMsg.set(32,"00000000000");
        isoMsg.set(33,"00000000000");
        isoMsg.set(37,"000000000000");
        isoMsg.set(41,"00008086");
        isoMsg.set(42,"000000000000000");
        isoMsg.set(43,"0000000000000000000000000000000000000000");
        isoMsg.set(49,"000");
        isoMsg.set(52,PINData);
        isoMsg.set(62,String.valueOf(PLNData.get("accountNumber")));
        isoMsg.set(100,"002");  //kode bank pemberi
        isoMsg.set(102,"0000000000000000000000000000");
        isoMsg.set(103,"0000000000000000000000000000");
        isoMsg.set(125,"0000000000000000");
        isoMsg.set(127,"002");  //bankcode penerima
        isoMsg.set(128,"0000000000000000");

        byte[] result = isoMsg.pack();
        String res =  new String(result);
        logger.info("Build ISO Message for Purchase PLN");

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(URL+primaryAccountNumber+"/purchase/PLN");
        logger.info("Try Post data to Server {}",URL+primaryAccountNumber+"/purchase/PLN");
        StringEntity entity = new StringEntity(res);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "text/plain");
        httpPost.setHeader("Content-type", "text/plain");
        CloseableHttpResponse response = client.execute(httpPost);

         responseBody = EntityUtils.toString(response.getEntity());

        client.close();
        }catch (Exception e){
        logger.error(e.getMessage());
         }
        return responseBody;
    }

    public String purchasePLNSend(String primaryAccountNumber, String PINData, int cash) throws ISOException, IOException {
        String MTI = "0200";
        JSONObject PLNData = (JSONObject) res.getCompanyAccountNumber().get("PLN");

        InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.setMTI(MTI);
        isoMsg.set(1,"000");
        isoMsg.set(2,primaryAccountNumber);
        isoMsg.set(3,"182102");
        isoMsg.set(4, String.valueOf(cash));
        isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
        isoMsg.set(11,"0000");
        isoMsg.set(12, new SimpleDateFormat("hhmmss").format(new Date()));
        isoMsg.set(13, new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(15,new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(18,"0000");
        isoMsg.set(32,"00000000000");
        isoMsg.set(33,"00000000000");
        isoMsg.set(37,"000000000000");
        isoMsg.set(41,"00008086");
        isoMsg.set(42,"000000000000000");
        isoMsg.set(43,"0000000000000000000000000000000000000000");
        isoMsg.set(49,"000");
        isoMsg.set(52,PINData);
        isoMsg.set(62,String.valueOf(PLNData.get("accountNumber")));
        isoMsg.set(100,"002");
        isoMsg.set(102,"0000000000000000000000000000");
        isoMsg.set(103,"0000000000000000000000000000");
        isoMsg.set(125,"0000000000000000");
        isoMsg.set(127,"002");
        isoMsg.set(128,"0000000000000000");

        byte[] result = isoMsg.pack();
        String resISO =  new String(result);

        return resISO;
    }




    public String transferSwitchingInquiry(String primaryAccountNumber, String PINData, int cash, String accountNumberTransfer) throws ISOException, IOException {
        String responseBody="";
        try{

        String MTI = "0200";
        InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);
        String bankCode =accountNumberTransfer.substring(0, 3);
         String newAccountNumberTransfer = accountNumberTransfer.substring(3);
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.setMTI(MTI);
        isoMsg.set(1,"000");
        isoMsg.set(2,primaryAccountNumber);
        isoMsg.set(3,"000");
        isoMsg.set(4, String.valueOf(cash));
        isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
        isoMsg.set(11,"0000");
        isoMsg.set(12, new SimpleDateFormat("hhmmss").format(new Date()));
        isoMsg.set(13, new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(15,new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(18,"0000");
        isoMsg.set(32,"00000000000");
        isoMsg.set(33,"00000000000");
        isoMsg.set(37,"000000000000");
        isoMsg.set(41,"00008086");
        isoMsg.set(42,"000000000000000");
        isoMsg.set(43,"0000000000000000000000000000000000000000");
        isoMsg.set(49,"000");
        isoMsg.set(52,PINData);
        isoMsg.set(62,newAccountNumberTransfer);
        isoMsg.set(100,"002");//kode bank pemberi
        isoMsg.set(102,"0000000000000000000000000000");
        isoMsg.set(103,"0000000000000000000000000000");
        isoMsg.set(125,"0000000000000000");
        isoMsg.set(127,bankCode);//bankcode penerima
        isoMsg.set(128,"0000000000000000");

        byte[] result = isoMsg.pack();
        String res =  new String(result);
        logger.info("Build ISO Message for Transfer Switching");

        CloseableHttpClient client = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("http://localhost:8089/switching/account/"+primaryAccountNumber+"/transfer_inquiry");
        logger.info("Try Post data to Server {}","http://localhost:8089/switching/account/"+primaryAccountNumber+"/transfer_inquiry");
        StringEntity entity = new StringEntity(res);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "text/plain");
        httpPost.setHeader("Content-type", "text/plain");
        CloseableHttpResponse response = client.execute(httpPost);
        //int statusCode = response.getEntity().getContent();
         responseBody = EntityUtils.toString(response.getEntity());

        client.close();

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return responseBody;
    }

    public String transferSwitching(String primaryAccountNumber, String PINData, int cash, String accountNumberTransfer) throws ISOException, IOException {
        String responseBody="";
        try{

        String MTI = "0200";
        String bankCode = accountNumberTransfer.substring(0, 3);
        InputStream in = ActionController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.setMTI(MTI);
        isoMsg.set(1, "000");
        isoMsg.set(2, primaryAccountNumber);
        isoMsg.set(3, "000");
        isoMsg.set(4, String.valueOf(cash));
        isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
        isoMsg.set(11, "0000");
        isoMsg.set(12, new SimpleDateFormat("hhmmss").format(new Date()));
        isoMsg.set(13, new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(15, new SimpleDateFormat("mmdd").format(new Date()));
        isoMsg.set(18, "0000");
        isoMsg.set(32, "00000000000");
        isoMsg.set(33, "00000000000");
        isoMsg.set(37, "000000000000");
        isoMsg.set(41, "00008086");
        isoMsg.set(42, "000000000000000");
        isoMsg.set(43, "0000000000000000000000000000000000000000");
        isoMsg.set(49, "000");
        isoMsg.set(52, PINData);
        isoMsg.set(62, accountNumberTransfer);
        isoMsg.set(100, "002");//kode bank pemberi
        isoMsg.set(102, "0000000000000000000000000000");
        isoMsg.set(103, "0000000000000000000000000000");
        isoMsg.set(125, "0000000000000000");
        isoMsg.set(127, bankCode);//bankcode penerima
        isoMsg.set(128, "0000000000000000");


        byte[] result = isoMsg.pack();
        String res = new String(result);


        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8089/switching/account/" + primaryAccountNumber + "/transfer");
        logger.info("Try Post data to Server {}","http://localhost:8089/switching/account/"+primaryAccountNumber+"/transfer");
        //dbCon.setAmount((Statement)dbCon.connectToDB().get(1),amount,accountNumber);
        StringEntity entity = new StringEntity(res);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "text/plain");
        httpPost.setHeader("Content-type", "text/plain");
        CloseableHttpResponse response = client.execute(httpPost);

         responseBody = EntityUtils.toString(response.getEntity());


        HttpPost httpPostAmount = new HttpPost("http://localhost:8086/account/" + primaryAccountNumber + "/transfer_amount");
        logger.info("Try to set Issuer Amount ");
        StringEntity entityAmount = new StringEntity(String.valueOf(cash));
        httpPostAmount.setEntity(entityAmount);
        httpPostAmount.setHeader("Accept", "text/plain");
        httpPostAmount.setHeader("Content-type", "text/plain");
        CloseableHttpResponse responseAmount = client.execute(httpPostAmount);


        client.close();
    }catch(Exception e){
            logger.error(e.getMessage());
        }
        return responseBody;

    }






    private void printISOMessage(ISOMsg isoMsg) {
        try {
            System.out.printf("MTI = %s%n", isoMsg.getMTI());
            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
                if (isoMsg.hasField(i)) {
                    System.out.printf("Field (%s) = %s%n", i, isoMsg.getString(i));
                }
                isoMsg.setFieldNumber(49);
            }
        } catch (ISOException e) {
            e.printStackTrace();
        }
    }
}
