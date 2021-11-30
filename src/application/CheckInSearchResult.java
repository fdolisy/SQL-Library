package application;

public class CheckInSearchResult {
    private String LoanID;
    private String Isbn;
    private String CardID;
    private String DateOut;
    private String DateDue;
    private String DateIn;

    public CheckInSearchResult(String L, String I, String C, String O, String D, String In) {
        this.LoanID = L;
        this.Isbn = I;
        this.CardID = C;
        this.DateOut = O;
        this.DateDue = D;
        this.DateIn = In;
    }

    public String getDateIn() {
        return DateIn;
    }

    public void setDateIn(String dateIn) {
        DateIn = dateIn;
    }

    public String getDateDue() {
        return DateDue;
    }

    public void setDateDue(String dateDue) {
        DateDue = dateDue;
    }

    public String getDateOut() {
        return DateOut;
    }

    public void setDateOut(String dateOut) {
        DateOut = dateOut;
    }

    public String getCardID() {
        return CardID;
    }

    public void setCardID(String cardID) {
        CardID = cardID;
    }

    public String getIsbn() {
        return Isbn;
    }

    public void setIsbn(String isbn) {
        Isbn = isbn;
    }

    public String getLoanID() {
        return LoanID;
    }

    public void setLoanID(String loanID) {
        LoanID = loanID;
    }
}
