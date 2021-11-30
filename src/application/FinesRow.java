package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

public class FinesRow
{
    // SQL Table Rows
    private int loan_id;
    private double fine_amt;
    private boolean paid;

    // Display Table Rows
    private String borrowerNameCol;
    private String bookTitleCol;
    private String daysLateCol;
    private String fineAmtCol;
    private String finePaidCol;

    // Extra Attributes
    private LocalDate checkOutDate;
    private LocalDate checkInDate;
    private LocalDate dueDate;

    private long daysLate;

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yyyy");
    private final DecimalFormat dollarFormat = new DecimalFormat("#0.00");

    public FinesRow(int loan_id, double fine_amt, boolean paid)
    {
        this.loan_id = loan_id;
        this.fine_amt = fine_amt;
        this.paid = paid;
        setDisplayTableRows();
    }

    private void setDisplayTableRows()
    {
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library?useSSL=false", "root", "cs4347libraryproject2001");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT book.title AS BookTitle, borrower.bname AS BorrowerName, book_loans.Date_out AS CheckOutDate, book_loans.Date_in AS CheckInDate, book_loans.Due_date AS BookDueDate "
                    + "FROM fines, book_loans, borrower, book "
                    + "WHERE fines.loan_id=" + loan_id + " "
                    + "AND book_loans.loan_id=fines.loan_id "
                    + "AND book.isbn=book_loans.isbn "
                    + "AND borrower.card_id=book_loans.card_id;");

            while (rs.next())
            {
                borrowerNameCol = rs.getString("BorrowerName");
                bookTitleCol = rs.getString("BookTitle");
                String dateOut = rs.getString("CheckOutDate");
                String dateIn = rs.getString("CheckInDate");
                String dateDue = rs.getString("BookDueDate");
                getDateValues(dateOut, dateIn, dateDue);
                daysLateCol = "" + daysLate;
                fineAmtCol = "$" + dollarFormat.format(fine_amt);
                System.out.println(fineAmtCol);
                finePaidCol = paid? "Yes" : "No";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getDateValues(String checkOut, String checkIn, String dueDateString)
    {
        checkOutDate = LocalDate.parse(checkOut, dateFormat);
        if (checkIn != null)
        {
            checkInDate = LocalDate.parse(checkIn, dateFormat);
        }
        dueDate = LocalDate.parse(dueDateString, dateFormat);

        if (checkInDate == null)
        {
            daysLate = DAYS.between(dueDate.atStartOfDay(), LocalDate.now().atStartOfDay());
        }
        else
        {
            daysLate = DAYS.between(dueDate.atStartOfDay(), checkInDate.atStartOfDay());
        }
    }

    public void updateFine()
    {
        if (!paid)
        {
            fine_amt += 0.25;
        }
    }

    public int getLoanID()
    {
        return loan_id;
    }

    public double getFineAmount()
    {
        return fine_amt;
    }

    public String getBorrowerNameCol()
    {
        return borrowerNameCol;
    }

    public LocalDate getCheckInDate()
    {
        return checkInDate;
    }

    public String getBookTitleCol()
    {
        return bookTitleCol;
    }

    public String getDaysLateCol()
    {
        return daysLateCol;
    }

    public String getFineAmtCol()
    {
        return fineAmtCol;
    }

    public String getFinePaidCol()
    {
        return finePaidCol;
    }
}
