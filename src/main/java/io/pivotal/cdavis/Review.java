package io.pivotal.cdavis;

/**
 * Created by cdavis on 12/23/16.
 */
public class Review {

    private long id;
    private int rating;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Review(long id, int rating) {
        this.id = id;
        this.rating = rating;
    }

    @Override
    public String toString () {
        return String.format("Rating[id=%d, rating=%d]", id, rating);
    }
}
