package com.example.demo;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AirportRepository extends CrudRepository<Airport, String> {
    List<Airport> findByAirportname(String name);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND airportname LIKE $1")
    List<Airport> findAirportsLike(String like);
}
