package com.example.demo;

import com.couchbase.client.core.env.TimeoutConfig;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.env.ClusterEnvironment;
import com.couchbase.transactions.Transactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableCouchbaseRepositories(basePackages = {"com.example"})
public class ServiceConfiguration extends AbstractCouchbaseConfiguration {

    @Autowired
    Cluster cluster;

    @Override
    protected List<String> getBootstrapHosts() {
        return Collections.singletonList("localhost");
    }

    @Override
    protected String getBucketName() {
        return "travel-sample";
    }


    // Make a user named travel-sample, put the password in here
    @Override
    protected String getPassword() {
        return "123456";
    }

    @Override
    public ClusterEnvironment couchbaseEnvironment() {
        return ClusterEnvironment.builder().timeoutConfig(
                TimeoutConfig.builder().connectTimeout(Duration.ofMillis(10000))
        ).build();
    }

    @Bean
    Transactions transactions() {
        return Transactions.create(cluster);
    }
}
