package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value="/airport")
@SpringBootApplication


/* remember to run this first
UPDATE `travel-sample` SET _class="com.example.demo.Airport" where type="airport";
*/

public class DemoApplication {
	@Autowired
	private AirportRepository airportRepository;

	@Autowired
	private ReactiveAirportRepository reactiveAirportRepository;


	static private Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	@RequestMapping("/name/{name}")
	public List<Airport> findByName(@PathVariable("name") String name) {
		return airportRepository.findByAirportname(name);
	}

	@RequestMapping("/reactive/name/{name}")
	public Flux<Airport> reactiveFindByName(@PathVariable("name") String name) {
		return reactiveAirportRepository.findAirportsLike(name);
	}

	@RequestMapping("like/{like}")
	public List<Airport> findLike(@PathVariable("like") String like) {
		// append and prepend a % for the like clause...
		return airportRepository.findAirportsLike("%"+like+"%");
	}

	@RequestMapping("reactive/like/{like}")
	public Flux<Airport> reactiveFindLike(@PathVariable("like") String like) {
		return reactiveAirportRepository.findAirportsLike("%"+like+"%");
	}

	/* you could do something like:
			curl -X POST -d'{"id":100000, "airportname":"Couchbase International", "city":"Santa Clara", "country":"USA"}' -H 'Content-Type: application/json' http://localhost:8080/airport
	   for instance.
	*/
	@RequestMapping(value="", method= RequestMethod.POST)
	public Airport post(@RequestBody Airport airport) {
		logger.info("airport:" + airport.toString());
		return airportRepository.save(airport);
	}

	@RequestMapping(value="reactive", method= RequestMethod.POST)
	public Mono<Airport> reactivePost(@RequestBody Airport airport) {
		logger.info("airport:" + airport.toString());
		return reactiveAirportRepository.save(airport);
	}

	@RequestMapping(value = "/{key}")
	public Optional<Airport> find(@PathVariable("key") String key) {
		return airportRepository.findById(key);
	}

	@RequestMapping(value = "/reactive/{key}")
	public Mono<Airport> reactiveFind(@PathVariable("key") String key) {
		return reactiveAirportRepository.findById(key);
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
