package projek.messageBroker;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import projek.server.controller.AccountController;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

@Component
public class Consumer {
    String URL = "http://localhost:8086/account/";
    @RabbitListener(queues = "${projek.rabbitmq.queue}")
    public void receivedMessage(String msg) throws ISOException, IOException {
        InputStream in = AccountController.class.getClassLoader().getResourceAsStream("fields.xml");
        GenericPackager packager = new GenericPackager(in);
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(msg.getBytes());

        if(isoMsg.getString(3).equals("300000")){
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL+isoMsg.getString(2)+"/balance");
            StringEntity entity = new StringEntity(msg);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "text/plain");
            httpPost.setHeader("Content-type", "text/plain");
            CloseableHttpResponse response = client.execute(httpPost);

            String responseBody = EntityUtils.toString(response.getEntity());
            Socket socket = new Socket("localhost", 7777);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(responseBody);
            dataOutputStream.flush();
            dataOutputStream.close();
            socket.close();

        }else if(isoMsg.getString(3).equals("390000")){
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL+isoMsg.getString(2)+"/transfer_inquiry");
            StringEntity entity = new StringEntity(msg);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "text/plain");
            httpPost.setHeader("Content-type", "text/plain");
            CloseableHttpResponse response = client.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            Socket socket = new Socket("localhost", 7777);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(responseBody);
            dataOutputStream.flush(); // send the message
            dataOutputStream.close(); // close the output stream when we're done.
            socket.close();

        }else if(isoMsg.getString(3).equals("400000")){
            System.out.println("TRansfer");
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL+isoMsg.getString(2)+"/transfer");
            StringEntity entity = new StringEntity(msg);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "text/plain");
            httpPost.setHeader("Content-type", "text/plain");
            CloseableHttpResponse response = client.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            Socket socket = new Socket("localhost", 7777);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(responseBody);
            dataOutputStream.flush(); // send the message
            dataOutputStream.close(); // close the output stream when we're done.
            socket.close();

        }else if(isoMsg.getString(3).equals("010000")){
            System.out.println("Withdraw");
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL+isoMsg.getString(2)+"/withdraw");
            StringEntity entity = new StringEntity(msg);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "text/plain");
            httpPost.setHeader("Content-type", "text/plain");
            CloseableHttpResponse response = client.execute(httpPost);
            //int statusCode = response.getEntity().getContent();
            String responseBody = EntityUtils.toString(response.getEntity());
            Socket socket = new Socket("localhost", 7777);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(responseBody);
            dataOutputStream.flush(); // send the message
            dataOutputStream.close(); // close the output stream when we're done.
            socket.close();
        }else if(isoMsg.getString(3).equals("382101")){
            System.out.println("Withdraw");
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL+isoMsg.getString(2)+"/payment_inquiry/PAM");
            StringEntity entity = new StringEntity(msg);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "text/plain");
            httpPost.setHeader("Content-type", "text/plain");
            CloseableHttpResponse response = client.execute(httpPost);
            //int statusCode = response.getEntity().getContent();
            String responseBody = EntityUtils.toString(response.getEntity());
            Socket socket = new Socket("localhost", 7777);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(responseBody);
            dataOutputStream.flush(); // send the message
            dataOutputStream.close(); // close the output stream when we're done.
            socket.close();
        }else if(isoMsg.getString(3).equals("182101")){
            System.out.println("payment");
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL+isoMsg.getString(2)+"/payment/PAM");
            StringEntity entity = new StringEntity(msg);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "text/plain");
            httpPost.setHeader("Content-type", "text/plain");
            CloseableHttpResponse response = client.execute(httpPost);
            //int statusCode = response.getEntity().getContent();
            String responseBody = EntityUtils.toString(response.getEntity());
            Socket socket = new Socket("localhost", 7777);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(responseBody);
            dataOutputStream.flush(); // send the message
            dataOutputStream.close(); // close the output stream when we're done.
            socket.close();
        }
        else if(isoMsg.getString(3).equals("382102")){
            System.out.println("Withdraw");
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL+isoMsg.getString(2)+"/purchase_inquiry/PLN");
            StringEntity entity = new StringEntity(msg);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "text/plain");
            httpPost.setHeader("Content-type", "text/plain");
            CloseableHttpResponse response = client.execute(httpPost);
            //int statusCode = response.getEntity().getContent();
            String responseBody = EntityUtils.toString(response.getEntity());
            Socket socket = new Socket("localhost", 7777);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(responseBody);
            dataOutputStream.flush(); // send the message
            dataOutputStream.close(); // close the output stream when we're done.
            socket.close();
        }else if(isoMsg.getString(3).equals("182102")){
            System.out.println("payment");
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL+isoMsg.getString(2)+"/purchase/PLN");
            StringEntity entity = new StringEntity(msg);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "text/plain");
            httpPost.setHeader("Content-type", "text/plain");
            CloseableHttpResponse response = client.execute(httpPost);

            String responseBody = EntityUtils.toString(response.getEntity());
            Socket socket = new Socket("localhost", 7777);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(responseBody);
            dataOutputStream.flush();
            dataOutputStream.close();
            socket.close();
        }
    }
}
