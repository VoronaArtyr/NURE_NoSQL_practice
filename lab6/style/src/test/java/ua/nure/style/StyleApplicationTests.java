package ua.nure.style;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ua.nure.style.dao.MongoDBDAO;
import ua.nure.style.dao.MysqlDAO;

//@SpringBootTest
class StyleApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void aggregationTest() {
		MongoDBDAO dao = new MongoDBDAO();
		dao.aggregateBookingsByClient();
		dao.aggregateUsersByRole();
		dao.aggregateBookingsByServices();
		dao.aggregateBookingsByStatus();
		dao.clientSideAggregation();
	}

}
