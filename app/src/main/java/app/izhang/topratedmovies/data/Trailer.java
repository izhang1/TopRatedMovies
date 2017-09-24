package app.izhang.topratedmovies.data;

/**
 * Created by ivanzhang on 9/24/17.
 */

public class Trailer {
    private String name;
    private String source;

    public Trailer (String name, String source){
        this.name = name;
        this.source = source;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }
}
