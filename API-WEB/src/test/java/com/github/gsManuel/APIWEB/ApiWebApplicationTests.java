package com.github.gsManuel.APIWEB;

import com.github.gsManuel.APIWEB.tests.ControllerTests;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiWebApplicationTests {

	@Test
	void AllTests() {
		ControllerTests controllerTests = new ControllerTests();
	}

}
