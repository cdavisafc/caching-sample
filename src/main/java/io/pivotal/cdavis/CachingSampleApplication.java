package io.pivotal.cdavis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class CachingSampleApplication implements CommandLineRunner {


	private static final Logger log = LoggerFactory.getLogger(CachingSampleApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(CachingSampleApplication.class, args);
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... strings) throws Exception {

		log.info("Creating tables");

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

		// Use a Java 8 stream to print out each tuple of the list
		productNames.forEach(name -> log.info(String.format("Inserting product record for %s", name)));

		// Uses JdbcTemplate's batchUpdate operation to bulk load data
		jdbcTemplate.batchUpdate("INSERT INTO products(name) VALUES (?)", productNames);

		log.info("Querying all product records:");
		jdbcTemplate.query(
				"SELECT id, name FROM products ",
				(rs, rowNum) -> new Product(rs.getLong("id"), rs.getString("name"))
		).forEach(product -> log.info(product.toString()));

		log.info("Creating rating for each product:");
		List<Object[]> reviews = jdbcTemplate.query(
				"SELECT id FROM products ",
				(rs, rowNum) -> new Object[]{rs.getLong("id"), 5});

		// Use a Java 8 stream to print out each tuple of the list
		reviews.forEach(review -> log.info(String.format("Inserting review for %d - %d stars", review[0], review[1])));

		// Uses JdbcTemplate's batchUpdate operation to bulk load data
		jdbcTemplate.batchUpdate("INSERT INTO reviews(id, rating) VALUES (?, ?)", reviews);

		log.info("Querying all product records:");
		jdbcTemplate.query(
				"SELECT id, rating FROM reviews ",
				(rs, rowNum) -> new Review(rs.getLong("id"), rs.getInt("rating"))
		).forEach(review -> log.info(review.toString()));
/*

						new Product(rs.getLong("id"), rs.getString("name"))
		).stream()
				.map(id -> new Object[]{id, 5})
				.collect(Collectors.toList());*/

	}
}
