package application;

import java.sql.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;
import javafx.scene.control.ButtonType;

public class Borrower {
    String SSN;
    String firstName;
    String lastName;
    String address;
    String city;
    String state;
    String phone;
    String Card_id;
    String Bname;

    private static Connection con = null;
    private static final Alert errorAlert = new Alert (AlertType.ERROR, "Borrower already exists.", ButtonType.OK);

    public Borrower(String stat, String lastN, String phoneNum, String social, String addr, String cit, String firstN) {
        this.firstName = firstN;
        this.lastName = lastN;
        this.SSN = social;
        this.address = addr;
        this.city = cit;
        this.state = stat;
        this.phone = phoneNum;
        this.Bname = firstN + " " + lastN;
    }

    public static void connectDatabase ()
    {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library?useSSL=false","root","cs4347libraryproject2001");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createNewBorrower (String SSN, String Bname, String address, String city, String state, String phone)
    {
        String latestID = "";
        String nextID = "";
        try {
            Statement stmt1 = con.createStatement();
            ResultSet resultSet1 = stmt1.executeQuery("SELECT * FROM borrower WHERE Ssn in ('"+SSN+"')");
            if (resultSet1.next())
            {
                errorAlert.show();
            }
            else
            {
                Statement stmt2 = con.createStatement();
                ResultSet resultSet2 = stmt2.executeQuery("SELECT MAX(Card_id) FROM borrower");
                if (resultSet2.next())
                {
                    latestID = resultSet2.getString(1);
                }
                String temporary = latestID.substring(4, latestID.length());
                int temporaryID = Integer.parseInt(temporary);
                temporaryID++;
                nextID = "ID00" + temporaryID;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO borrower (Card_id, Ssn, Bname, address, phone, city, state) VALUE ('"+nextID+"','"+SSN+"','"+Bname+"','"+address+"','"+phone+"','"+city+"','"+state+"')");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}