package projek.server.controller;

import projek.Model.Account;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseController {
    public  Properties readProp(){
        Properties prop = new Properties();
        InputStream in = DatabaseController.class.getClassLoader().getResourceAsStream("database.properties");
        try {
            prop.load(in);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }

    public  ArrayList<Object> connectToDB(){
        Properties newProp = readProp();
        ArrayList<Object> ObjDB = new ArrayList<Object>();

        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://" + newProp.getProperty("SERVER_DB") + ":" + newProp.getProperty("PORT_DB") + "/" + newProp.getProperty("NAME_DB") + "?useTimezone=true&serverTimezone=UTC", newProp.getProperty("USER_DB"), newProp.getProperty("PASSWORD_DB"));
            Statement stmt = con.createStatement();
            ObjDB.add(con);
            ObjDB.add(stmt);
        }catch(Exception e){
            System.out.println(e);
        }
        return ObjDB;
    }

    public static void showTable(String namaTbl, Statement stmt){
        try{
            ResultSet rs=stmt.executeQuery("select * from "+namaTbl+"");
            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3)+"  "+rs.getString(4));
        }catch(Exception e){
            System.out.println("Koneksi dulu dong bangg. Ketik 1 dulu bang");
        }
    }
    public void setAmount(Statement stmt,int amount, String accountNumber)  {
        try{
            int result=stmt.executeUpdate("Update bank_account set `amount`=" + amount + "  where `id_account`=" + accountNumber + " ");
        }catch (Exception e){
            System.out.println("Gagal");
        }


    }

    public  List<Account> DBToAccountList(){
        ArrayList<Account> AccountList = new ArrayList<Account>();

        try {
            Connection con = (Connection) this.connectToDB().get(0);
            String sql ="select * from bank_account";
            PreparedStatement Stmt = con.prepareStatement(sql);
            ResultSet rs = Stmt.executeQuery(sql);

            while(rs.next()){
                Account account = new Account();
                account.setAccountName(rs.getString("name_account"));
                account.setAccountAddress(rs.getString("address_account"));
                account.setAccountDateBirth(rs.getDate("datebirth_account"));
                account.setAccountJob(rs.getString("job_account"));
                account.setAmount(rs.getInt("amount"));
                AccountList.add(account);
            }
            rs.close();
            con.close();

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return AccountList;
    }



    public static void closeConnection(Connection con){
        try{
            if(!con.isClosed()){
                con.close();
            }
        }catch(Exception e){
            System.out.println("Koneksi tidak dapat close");
        }

        System.out.println("Sudah diclose bang!");
    }
}
