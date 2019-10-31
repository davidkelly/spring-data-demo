package com.example.demo;

import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.annotation.Id;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdAttribute;
import org.springframework.data.couchbase.core.mapping.id.IdPrefix;

@Document
public class Airport {
        @Id
        @GeneratedValue(strategy= GenerationStrategy.USE_ATTRIBUTES, delimiter = "_")
        private String key;

        @IdPrefix
        private String airportPrefix = "airport";

        @IdAttribute(order=0)
        private int id;

        @Override
        public String toString() {
                return "Airport{" +
                        "key='" + key + '\'' +
                        ", airportPrefix='" + airportPrefix + '\'' +
                        ", id=" + id +
                        ", airportname='" + airportname + '\'' +
                        ", city='" + city + '\'' +
                        ", country='" + country + '\'' +
                        '}';
        }

        public String getKey() {
                return key;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getAirportname() {
                return airportname;
        }

        public void setAirportname(String airportname) {
                this.airportname = airportname;
        }

        public String getCity() {
                return city;
        }

        public void setCity(String city) {
                this.city = city;
        }

        public String getCountry() {
                return country;
        }

        public void setCountry(String country) {
                this.country = country;
        }

        private String airportname;
        private String city;
        private String country;
}
