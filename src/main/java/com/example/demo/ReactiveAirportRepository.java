package com.example.demo;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ReactiveAirportRepository extends ReactiveCrudRepository<Airport, String> {
    Flux<Airport> findByAirportname(String name);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND airportname LIKE $1")
    Flux<Airport> findAirportsLike(String like);
}
