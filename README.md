# spring-data-demo

This is a simple demo app of new spring-data-couchbase 4.0.0.M1, which has not yet been released.

# getting started
The travel-sample bucket needs to be created first.  In couchbase admin page, go to Settings->Sample Buckets, and click
on travel-sample.  Then, go to Query and execute the following:
```UPDATE `travel-sample` SET _class="com.example.demo.Airport" where type="airport";```

Now, the demo app should work.  Run it (mvn spring-boot:run).   You can hit the various endpoints with curl, for instance:

```
curl -X POST -d'{"id":100000, "airportname":"Couchbase International", "city":"Santa Clara", "country":"USA"}' -H 'Content-Type: application/json' http://localhost:8080/airport
{"key":"airport_100000","id":100000,"airportname":"Couchbase International","city":"Santa Clara","country":"USA"}
```
will create a new airport.  You can find it with:

```
curl http://localhost:8080/airport/airport_100000
{"key":"airport_100000","id":100000,"airportname":"Couchbase International","city":"Santa Clara","country":"USA"}
```
