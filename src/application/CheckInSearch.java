package application;

import java.sql.*;

public class CheckInSearch {
    String searchTxt;
    static Connection conn=null;
    ResultSet rs;

    CheckInSearch(String s){
        this.searchTxt = s;
    }

    public ResultSet searchData()
    {
        try{
            //jdbc connection to database
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library?useSSL=false","root","cs4347libraryproject2001");
            Statement stmt= conn.createStatement();
            //ResultSet rs = null;

            // result set is all books
            if(searchTxt.equals(" ")|| searchTxt.equals(""))
            {
                rs = stmt.executeQuery("SELECT book_loans.loan_id, book_loans.isbn, book_loans.card_id, book_loans.date_out, book_loans.due_date, book_loans.date_in FROM book_loans;");
            }
            else
            {
                rs = stmt.executeQuery("Select distinct book_loans.loan_id, book_loans.isbn, book_loans.card_id, book_loans.date_out," +
                        "book_loans.due_date, book_loans.date_in FROM book_loans, borrower where book_loans.isbn LIKE '%"+searchTxt+"%' " +
                        "OR book_loans.card_id LIKE '%"+searchTxt+"%' OR (Borrower.Bname LIKE '%"+searchTxt+"%' AND borrower.Card_id = book_loans.Card_id) order by book_loans.loan_id;");
            }
            return rs;
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return rs;
    }
}
