package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.service.PriceException;
import com.udacity.pricing.service.PricingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PricingServiceApplicationTests {

	@LocalServerPort
	int port;

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
	}

	@Test(expected = PriceException.class)
	public void exceptionThrownWhenNonExistingId() throws PriceException {
		Long nonExistingVehicleId = -1L;
		PricingService.getPrice(nonExistingVehicleId);
	}

	@Test
	public void priceIsCorrect() throws PriceException {
		Long vehicleId = 1L;
		Price price = PricingService.getPrice(vehicleId);

		assertThat(price, is(notNullValue()));
		assertThat(price.getVehicleId(), is(equalTo(vehicleId)));
		assertThat(price.getCurrency(), is(equalTo("USD")));
		assertThat(price.getPrice(), is(greaterThan(BigDecimal.ZERO)));
	}

	@Test
	public void pricingServiceIsCallable() {
		ResponseEntity<Price> response =
				this.restTemplate.getForEntity("http://localhost:" + port + "/prices/1", Price.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
	}

}
