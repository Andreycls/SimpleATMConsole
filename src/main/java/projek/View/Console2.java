package projek.View;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jpos.iso.ISOException;
import org.json.JSONException;
import projek.client.controller.ActionController;
import projek.server.controller.AccountController;
import projek.server.controller.DatabaseController;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Statement;
import java.util.Scanner;

public class Console2 {
    public static void sendHttpRequest(String msg) throws IOException {


        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8086/send");
        StringEntity entity = new StringEntity(msg);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "text/plain");
        httpPost.setHeader("Content-type", "text/plain");
        CloseableHttpResponse response = client.execute(httpPost);
        //int statusCode = response.getEntity().getContent();
        String responseBody = EntityUtils.toString(response.getEntity());

        client.close();

    }

    public static void main(String[] args) throws IOException, JSONException, ISOException {

        Scanner sc = new Scanner(System.in);
        AccountController auth = new AccountController();
        DatabaseController db = new DatabaseController();
        ActionController action = new ActionController();
        Statement stmt = (Statement) db.connectToDB().get(1);

        System.out.println("\t\t\tPT. ATM MINI");
        System.out.print("MASUKKAN NOMOR REKENING : ");
        String accountNumber = sc.next();

        if (!auth.checkAccountNumber(stmt, accountNumber)) {
            System.out.println("KARTU TIDAK DIKENALI");
            System.exit(0);
        }


        System.out.print("MASUKKAN PIN  : ");

        ulang:
        do {
            String accountPIN = sc.next();
            System.out.println("\t\t\tPT. ATM MINI\n\t\tPILIH JENIS TRANSAKSI" + "\n" + "1 << TARIK TUNAI" + "\t\t5 << PEMBAYARAN"+ "\n" + "2 << TRANSFER   " + "\t\t6 << KELUAR"+ "\n" + "3 << PEMBELIAN  " + "\t\t"+ "\n" + "4 << INFO SALDO     " + "\t\t"+ "\n"  );
            int menu = sc.nextInt();

            menu:
            //Tarik tunai
            switch (menu) {

                case 1:
                    int cash = 0;
                    System.out.println("Pilih nominal :");
                    System.out.println("1<< 50.000\t\t4<< 500.000");
                    System.out.println("2<< 100.000\t\t5<< 1.000.000");
                    System.out.println("3<< 200.000\t\t6<< Nominal lainnya");
                    int nominal = sc.nextInt();
                    if (nominal == 1) {
                        cash = 50000;
                    } else if (nominal == 2) {
                        cash = 100000;
                    } else if (nominal == 3) {
                        cash = 200000;
                    } else if (nominal == 4) {
                        cash = 500000;
                    } else if (nominal == 5) {
                        cash = 1000000;
                    } else if (nominal == 6) {
                        System.out.println("Masukkan nominal :");
                        cash = sc.nextInt();
                    } else {
                        System.out.println("Masukkan nominal :");
                        cash = sc.nextInt();
                    }
                    sendHttpRequest(action.cashWithdrawal(accountNumber, accountPIN, cash));
                    ServerSocket ss = new ServerSocket(7777);
                    Socket socket = ss.accept();
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    String message = dataInputStream.readUTF();
                    System.out.println(message);

                    action.checkResponseCode(message);
                    if(action.checkPIN(message)){
                        System.out.println("Saldo anda : " + action.getAccountBalance(message));
                    }
                    ss.close();
                    socket.close();
                    dataInputStream.close();


            System.out.println("Transaksi selanjutnya: \n[1] Ya\n[2] Tidak");
            int nextStep = sc.nextInt();
            if(nextStep==1){
                System.out.print("MASUKKAN PIN  : ");
                continue ulang;
            }

                case 2:
                    System.out.println("Transfer");
                    System.out.println("1<< Transfer ke BRI\n2<< Transfer ke Bank lain");
                    int goTo = sc.nextInt();
                    if (goTo == 1) {
                        System.out.println("Masukkan nominal : ");
                        int cashTransfer = sc.nextInt();
                        System.out.println("Masukkan nomor rekening penerima : ");
                        String accountNumberTransfer = sc.next();

                         sendHttpRequest(action.transferInquiry(accountNumber, accountPIN, cashTransfer, accountNumberTransfer));
                         ss = new ServerSocket(7777);
                         socket = ss.accept(); // blocking call, this will wait until a connection is attempted on this port.
                         inputStream = socket.getInputStream();
                         dataInputStream = new DataInputStream(inputStream);
                         message = dataInputStream.readUTF();
                        System.out.println(message);

                        action.checkResponseCode(message);

                        System.out.println("Nama Penerima  :" + action.getAccountNameBeneficiary(message));
                        System.out.println("Nomor Rekening :" + action.getAccountNumberBeneficiary(message));
                        ss.close();
                        socket.close();
                        dataInputStream.close();

                        System.out.println("Lanjutkan transaksi ?\n 1 << Ya\n 2 << Tidak ");
                        int question = sc.nextInt();
                        if (question == 1) {
                            sendHttpRequest(action.transferSend(accountNumber, accountPIN, cashTransfer, accountNumberTransfer));
                            ss = new ServerSocket(7777);
                            socket = ss.accept(); // blocking call, this will wait until a connection is attempted on this port.
                           inputStream = socket.getInputStream();
                            dataInputStream = new DataInputStream(inputStream);
                            String messages = dataInputStream.readUTF();
                            action.checkResponseCode(messages);
                            if(action.checkPIN(messages)){

                            }
                            ss.close();
                            socket.close();
                            dataInputStream.close();

                        }
                        else if(question==2){
                        System.out.print("MASUKKAN PIN  : ");
                        continue ulang;
                    }
                    System.out.println("Transaksi selanjutnya: \n[1] Ya\n[2] Tidak");
                     nextStep = sc.nextInt();
                    if(nextStep==1){
                        System.out.print("MASUKKAN PIN  : ");
                        continue ulang;
                    }
                    }
                    else {
                        System.out.println("Masukkan nominal : ");
                        int cashTransfer = sc.nextInt();
                        System.out.println("Masukkan nomor rekening penerima : ");
                        System.out.println("Ketik 1 untuk melihat kode bank");
                         nextStep = sc.nextInt();
                        kodebank:
                        if(nextStep==1){
                            System.out.println("Kode bank.\n BANK BRI 002\n Bank BNI 009\n[1] Lanjut ke menu entry");
                             goTo = sc.nextInt();
                            if( goTo == 1){
                               break kodebank;
                            }
                        }
                        System.out.println("Masukkan nomor rekening penerima  : ");
                        String accountNumberTransfer = sc.next();
                        System.out.println("Nama Penerima  :" + action.getAccountNameBeneficiary(action.transferSwitchingInquiry(accountNumber, accountPIN, cashTransfer, accountNumberTransfer)));
                        System.out.println("Nomor Rekening :" + action.getAccountNumberBeneficiary(action.transferSwitchingInquiry(accountNumber, accountPIN, cashTransfer, accountNumberTransfer)));
                        System.out.println("Lanjutkan transaksi ?\n [1] Ya\n [2] Tidak ");
                        int question = sc.nextInt();
                        if (question == 1) {
                            //action.checkResponseCode(action.transferSwitching(accountNumber, accountPIN, cashTransfer, accountNumberTransfer));
                            //if(action.checkPIN(action.transferSwitching(accountNumber, accountPIN, cashTransfer, accountNumberTransfer))){
                                action.transferSwitching(accountNumber, accountPIN, cashTransfer, accountNumberTransfer);
                            //}
                            System.out.println("Transaksi selanjutnya: \n[1] Ya\n[2] Tidak");
                            nextStep = sc.nextInt();
                            if(nextStep==1){
                                System.out.print("MASUKKAN PIN  : ");
                                continue ulang;
                            }
                        } else if(question==2){
                            System.out.print("MASUKKAN PIN  : ");
                            continue ulang;
                        }
                        System.out.println("Transaksi selanjutnya: \n[1] Ya\n[2] Tidak");
                         nextStep = sc.nextInt();
                        if(nextStep==1){
                            System.out.print("MASUKKAN PIN  : ");
                            continue ulang;
                        }

                    }


                case 3:
                    System.out.println("Pilih Produk");
                    System.out.println("1<< Pulsa\n2<< Paket Data\n3<< PLN Prabayar");
                    int produk = sc.nextInt();
                    if(produk==3){
                        System.out.println("Masukkan token");
                        int token = sc.nextInt();
                        System.out.println("Pilih nominal");
                        System.out.println("1<< 20.000\n2<< 50.000\n3<< 100.000\n4<< 200.000\n");
                        int chooseAmount = sc.nextInt();
                        int amount = 0;
                        if(chooseAmount==1){
                             amount = 20000;
                        }
                        else if(chooseAmount==2){
                             amount = 50000;
                        }
                        else if(chooseAmount==3){
                             amount = 100000;
                        }
                        else if(chooseAmount==4){
                             amount = 200000;
                        }
                        System.out.println("Nama Penerima  :"+action.getAccountNameBeneficiary(action.purchaseInquiryPLN(accountNumber, accountPIN,amount)));


                        System.out.println("Lanjutkan transaksi ?\n 1 << Ya\n 2 << Tidak ");
                        int question = sc.nextInt();
                        if(question==1){

                            sendHttpRequest(action.purchasePLNSend(accountNumber, accountPIN,amount));
                            ss = new ServerSocket(7777);
                            socket = ss.accept(); // blocking call, this will wait until a connection is attempted on this port.
                            inputStream = socket.getInputStream();
                            dataInputStream = new DataInputStream(inputStream);
                            String messages = dataInputStream.readUTF();
                            action.checkResponseCode(messages);
                            if(action.checkPIN(messages)){

                            }
                            ss.close();
                            socket.close();
                            dataInputStream.close();



                        }
                        else if(question==2){
                            continue;
                        }
                        System.out.println("Transaksi selanjutnya: \n[1] Ya\n[2] Tidak");
                         nextStep = sc.nextInt();
                        if(nextStep==1){
                            System.out.print("MASUKKAN PIN  : ");
                            continue ulang;
                        }
                    }




                case 4:
                    sendHttpRequest(action.balanceInquiry(accountNumber, accountPIN));
                     ss = new ServerSocket(7777);
                     socket = ss.accept(); // blocking call, this will wait until a connection is attempted on this port.
                     inputStream = socket.getInputStream();
                     dataInputStream = new DataInputStream(inputStream);
                     message = dataInputStream.readUTF();
                    System.out.println(message);
                    action.checkResponseCode(message);
                    if(action.checkPIN(message)){
                        System.out.println("Saldo anda : " + action.getAccountBalance(message));
                    }
                    ss.close();
                    socket.close();



                    System.out.println("Transaksi selanjutnya: \n[1] Ya\n[2] Tidak");
                     nextStep = sc.nextInt();
                    if(nextStep==1){
                        System.out.print("MASUKKAN PIN  : ");
                        continue ulang;
                    }


                case 5:
                    System.out.println("Pilih Produk");
                    System.out.println("1<< Internet\t\t 5<< Telekomunikasi\n" +
                            "2<< Pendidikan\t\t 6<< Kabel TV\n" +
                            "3<< PLN\t\t\t\t 7<< Angsuran\n" +
                            "4<< PAM\t\t\t\t 8<< Asuransi");
                     produk = sc.nextInt();
                    if(produk==4){
                        System.out.println("Masukkan nomor token PAM:");
                        int tokenNumber = sc.nextInt();
                        System.out.println("Nama Penerima  :"+action.getAccountNameBeneficiary(action.paymentInquiryPAM(accountNumber, accountPIN)));

                    }
                    System.out.println("Lanjutkan transaksi ?\n 1 << Ya\n 2 << Tidak ");
                     int question = sc.nextInt();
                    if(question==1){
                        sendHttpRequest(action.paymentPAMSend(accountNumber, accountPIN));
                        ss = new ServerSocket(7777);
                        socket = ss.accept(); // blocking call, this will wait until a connection is attempted on this port.
                        inputStream = socket.getInputStream();
                        dataInputStream = new DataInputStream(inputStream);
                        String messages = dataInputStream.readUTF();
                        action.checkResponseCode(messages);
                        if(action.checkPIN(messages)){

                        }
                        ss.close();
                        socket.close();
                        dataInputStream.close();



                    }else if(question==2){
                        continue;
                    }
                    System.out.println("Transaksi selanjutnya: \n[1] Ya\n[2] Tidak");
                     nextStep = sc.nextInt();
                    if(nextStep==1){
                        System.out.print("MASUKKAN PIN  : ");
                        continue ulang;
                    }



                case 6:
                    System.exit(0);



            }

        } while (sc.hasNext());

    }
}
