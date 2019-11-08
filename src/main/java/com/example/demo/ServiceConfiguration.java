package com.example.demo;

import com.couchbase.client.core.env.TimeoutConfig;
import com.couchbase.client.java.env.ClusterEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableCouchbaseRepositories(basePackages = {"com.example"})
public class ServiceConfiguration extends AbstractCouchbaseConfiguration {

    @Override
    protected List<String> getBootstrapHosts() {
        return Arrays.asList("10.112.195.101");
    }

    @Override
    protected String getBucketName() {
        return "travel-sample";
    }

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
}
