package projek.server.controller;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.stereotype.Controller;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.Statement;

@Controller
public class AccountController {
    DatabaseController dbCon = new DatabaseController();

    public boolean checkAccountNumber(Statement stmt, String accountNumber) {

        try {
            ResultSet rs = stmt.executeQuery("select * from bank_account where id_account=" + "'" + accountNumber + "'");
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Gagal mengecek");
        }
        return false;
    }

    public boolean checkAccountPIN(Statement stmt, String accountNumber, String accountPIN) {
        boolean checker = false;
        String PIN = "";
        try {
            ResultSet rs = stmt.executeQuery("select pin_account from bank_account where id_account=" + "'" + accountNumber + "'");

            if (rs.next()) {
                int PINNumber = rs.getInt(1);
                PIN = String.format("%016d", PINNumber);
                if (accountPIN.equals(PIN)) {
                    checker = true;
                }
            }

        } catch (Exception e) {
            System.out.println("Gagal mengecek");
        }
        return checker;
    }

    public int getAccountBalance( String accountNumber) {
        Statement stmt = (Statement)dbCon.connectToDB().get(1);
        int balance = 0;
        try {
            ResultSet rs = stmt.executeQuery("select amount from bank_account where id_account=" + "'" + accountNumber + "'");
            if (rs.next()) {
                balance = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return balance;
    }
    public String getAccountName(Statement stmt, String accountNumber) {
        String name = "";
        try {
            ResultSet rs = stmt.executeQuery("select name_account from bank_account where id_account=" + "'" + accountNumber + "'");
            if (rs.next()) {
                name = rs.getString(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return name;
    }




}
