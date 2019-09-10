package android.example.newsapp;

public class News {
    private String sectionName;
    private String title;
    private String date;
    private String time;
    private String url;

    public News(String sectionName,String title,String date,String time,String url){
        this.sectionName = sectionName;
        this.title = title;
        this.date  = date;
        this.time = time;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getTitle() {
        return title;
    }
}
