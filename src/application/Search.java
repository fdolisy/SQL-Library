package application;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
public class Search
{
    String searchTxt;
    static Connection conn=null;
    ResultSet rs;
    Search(String searchTxt)
    {
        this.searchTxt = searchTxt;
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
                rs = stmt.executeQuery("SELECT b.Isbn, b.Title, GROUP_CONCAT(a.Name) as Authors, " +
                        "(case when b.Isbn in(Select Isbn from library.book_loans where Date_in IS NULL) " +
                        "then 'no' else 'yes' end) AS Available FROM library.book " +
                        "AS b left outer join library.book_authors " +
                        "AS ba on b.Isbn=ba.Isbn left outer join library.authors as a on ba.Author_id=a.Author_id group by b.Isbn; ");
            }
            else
            {
                rs = stmt.executeQuery("SELECT b.Isbn, b.Title, GROUP_CONCAT(a.Name) as Authors, " +
                        "(case when b.Isbn in(Select Isbn from library.book_loans where Date_in IS NULL) " +
                        "then 'no' else 'yes' end) AS Available FROM library.book " +
                        "AS b left outer join library.book_authors " +
                        "AS ba on b.Isbn=ba.Isbn left outer join library.authors as a on ba.Author_id=a.Author_id " +
                        "group by b.Isbn having b.Isbn like '%"+searchTxt+"%' or b.Title like '%"+searchTxt+ "%' or Authors like '%"+searchTxt+"%';");
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
