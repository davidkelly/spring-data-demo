package com.example.demo;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.transactions.TransactionGetResult;
import com.couchbase.transactions.Transactions;
import com.couchbase.transactions.error.TransactionFailed;
import com.couchbase.transactions.log.LogDefer;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value="/airport")
@SpringBootApplication


/* remember to run this first, after asking for the travel-sample bucket:

UPDATE `travel-sample` SET _class="com.example.demo.Airport" where type="airport";

ALSO - I did this with a 2 node cluster, where the travel-sample bucket expects 1 replica.
Be sure your cluster is complete (enough running for all the replicas to be available).  The
default durability level in the transactions code expects it written to all replicas, so they
should be online or you will see errors/retries.
*/

public class DemoApplication {
	@Autowired
	private AirportRepository airportRepository;

	@Autowired
	Cluster cluster;

	@Autowired
	private ReactiveAirportRepository reactiveAirportRepository;

	@Autowired
	private Collection collection;

	Transactions transactions = null;

	static private Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	private Transactions getTransactions() {
		if (transactions == null) {
			transactions = Transactions.create(cluster);//template.getCouchbaseCluster());
		}
		return transactions;
	}

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

	@RequestMapping(value = "txn/{id1}/{id2}")
	public List<Airport> update(@PathVariable("id1") String id1, @PathVariable("id2") String id2) {
		// lets swap the citys of 2 airports, in a transaction
		// and return the final results.
		List<Airport> returnVal = new ArrayList<Airport>();
		try {
			getTransactions().run( (ctx) -> {
				TransactionGetResult a1Result = ctx.get(collection, id1);
				TransactionGetResult a2Result = ctx.get(collection, id2);
				Airport a1 = a1Result.contentAs(Airport.class);
				Airport a2 = a2Result.contentAs(Airport.class);
				String temp = a2.getCity();
				a2.setCity(a1.getCity());
				a1.setCity(temp);
				ctx.replace(a1Result, a1);
				ctx.replace(a2Result, a2);
				returnVal.add(ctx.get(collection, id1).contentAs(Airport.class));
				returnVal.add(ctx.get(collection, id2).contentAs(Airport.class));
			});
			return returnVal;
		} catch (TransactionFailed e ) {
			logger.error("Transaction " + e.result().transactionId() + " failed!");
			for(LogDefer err: e.result().log().logs()) {
				logger.error(err.toString());
			}
			return Arrays.asList();
		}
	}
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
