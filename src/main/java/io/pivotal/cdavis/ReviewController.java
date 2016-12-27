package io.pivotal.cdavis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;


import org.springframework.web.bind.annotation.*;

/**
 * Created by cdavis on 12/26/16.
 */

@RestController
public class ReviewController {

	private static final Logger log = LoggerFactory.getLogger(CachingSampleApplication.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping("/reviews/count")
    public int getRatingsCount() {

		log.info("Querying reviews records to get total count:");
		int numReviews = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) AS NumberofReviews FROM reviews", null, Integer.class);
		return numReviews;
        
    }


	@RequestMapping("/reviews/{id}")
	public List<Integer> getRatingsForAProduct(@PathVariable int id) {
		log.info("Querying for reviews by product id:");
		List<Integer> reviews = jdbcTemplate.query(
				"SELECT id, rating FROM reviews WHERE id = '"+id+"'",
				(rs, rowNum) -> rs.getInt("rating"));
		return reviews;
	}

	
	@RequestMapping(value = "/reviews/{id}", method = RequestMethod.POST)
    public void createProduct(@PathVariable int id, @RequestBody int review) {

		log.info("Creating new review record:");

        jdbcTemplate.execute("INSERT INTO reviews(id, rating) VALUES ("+id+","+review+")");
//        jdbcTemplate.batchUpdate("INSERT INTO reviews(id, rating) VALUES (?, ?)", new Object[] {id, 5});
//		jdbcTemplate.execute("INSERT INTO reviews(rating) VALUES ('"+4+"')");
        
    }
	
}
