package io.pivotal.cdavis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;




/**
 * Created by cdavis on 12/26/16.
 */

@RestController
public class ProductController {

	private static final Logger log = LoggerFactory.getLogger(CachingSampleApplication.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping("/products")
    public List<Product> getProducts() {

		log.info("Querying all product records:");
		List<Product> products = jdbcTemplate.query(
				"SELECT id, name FROM products ",
				(rs, rowNum) -> new Product(rs.getLong("id"), rs.getString("name")));
		return products;
        
    }
	
	@RequestMapping("/products/{id}")
    public Product getProduct(@PathVariable long id) {

		log.info("Querying for product by id:");
		List<Product> products = jdbcTemplate.query(
				"SELECT id, name FROM products WHERE id = '"+id+"'",
				(rs, rowNum) -> new Product(rs.getLong("id"), rs.getString("name")));
		return products.get(0);
        
    }
	
	@RequestMapping(value = "/products", method = RequestMethod.POST)
    public void createProduct(@RequestBody Product product) {

		log.info("Creating new product record:");
		String productName = product.getName();
        log.info("Creating new product record: " + productName);

		jdbcTemplate.execute("INSERT INTO products(name) VALUES ('"+productName+"')");
        
    }
	
}
