package sg.edu.nus.iss.vttp5a_day16wsA.model;


public class BoardGame {
    
    private Integer id;
    private String name;
    private Integer year;
    private Integer ranking;
    private Integer rating;
    private String url;


    public BoardGame() {
    }

    public BoardGame(Integer id, String name, Integer year, Integer ranking, Integer rating, String url) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.ranking = ranking;
        this.rating = rating;
        this.url = url;
    }

    
    @Override
    public String toString() {
        return id + "," + name + "," + year + "," + ranking + ","
                + rating + "," + url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    
    
}
