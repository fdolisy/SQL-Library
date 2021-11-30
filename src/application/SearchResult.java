package application;

public class SearchResult {
    private String Isbn;
    private String Title;
    private String Author;
    private String Status;

    public SearchResult(String I, String T, String A, String S) {
        this.Isbn = I;
        this.Title = T;
        this.Author = A;
        this.Status = S;
    }

    public String getIsbn(){
        return Isbn;
    }
    public void setIsbn(String I){
        Isbn = I;
    }


    public String getTitle(){
        return Title;
    }
    public void setTitle(String T){
        Title = T;
    }

    public String getAuthor(){
        return Author;
    }
    public void setAuthor(String A){
        Author = A;
    }

    public String getStatus() {
        return Status;
    }
    public void setStatus(String S){
        Status = S;
    }

}
