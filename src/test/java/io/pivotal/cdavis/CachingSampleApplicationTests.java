package io.pivotal.cdavis;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CachingSampleApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Before
	public void loadData() {
		jdbcTemplate.execute("DROP TABLE products IF EXISTS");
		jdbcTemplate.execute("CREATE TABLE products(" +
				"id SERIAL, name VARCHAR(255))");
		jdbcTemplate.execute("DROP TABLE reviews IF EXISTS");
		jdbcTemplate.execute("CREATE TABLE reviews(" +
				"id BIGINT, rating INTEGER)");

		// Create a list of product names
		List<Object[]> productNames = Arrays.asList("Book", "Snowboard", "Kayak", "Bike").stream()
				.map(name -> new String[]{name})
				.collect(Collectors.toList());

		// Uses JdbcTemplate's batchUpdate operation to bulk load data
		jdbcTemplate.batchUpdate("INSERT INTO products(name) VALUES (?)", productNames);

		List<Object[]> reviews = jdbcTemplate.query(
				"SELECT id FROM products ",
				(rs, rowNum) -> new Object[]{rs.getLong("id"), 5});

		// Uses JdbcTemplate's batchUpdate operation to bulk load data
		jdbcTemplate.batchUpdate("INSERT INTO reviews(id, rating) VALUES (?, ?)", reviews);

	}

	@Test
	public void checkDBLoads() {
		List<Product> products = jdbcTemplate.query(
				"SELECT id, name FROM products ",
				(rs, rowNum) -> new Product(rs.getLong("id"), rs.getString("name")));
		assertEquals(4, products.size());

		products = jdbcTemplate.query(
				"SELECT id, name FROM products WHERE name = 'Kayak'",
				(rs, rowNum) -> new Product(rs.getLong("id"), rs.getString("name")));
		assertEquals(1, products.size());

		List<Review> reviews = jdbcTemplate.query(
				"SELECT id, rating FROM reviews ",
				(rs, rowNum) -> new Review(rs.getLong("id"), rs.getInt("rating")));
		assertEquals(4, reviews.size());

		reviews.forEach(review -> assertEquals(5, review.getRating()));

	}

}
