package io.pivotal.cdavis;

/**
 * Created by cdavis on 12/23/16.
 */
public class Product {

    private long id;
    private String name;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product(long id, String name) {
        this.id = id;
        this.name = name;
    }
	public Product() {
		this.id = -1;
		this.name = "foo";
	}

    @Override
    public String toString () {
        return String.format("Product[id=%d, name=%s]", id, name);
    }
}
