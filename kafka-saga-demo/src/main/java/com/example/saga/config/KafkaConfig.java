package com.example.saga.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

	@Bean
	public NewTopic orderTopic() {

		return TopicBuilder.name("order-topic").partitions(1).replicas(1).build();
	}

	@Bean
	public NewTopic inventoryTopic() {

		return TopicBuilder.name("inventory-topic").partitions(1).replicas(1).build();
	}

	@Bean
	public NewTopic paymentTopic() {

		return TopicBuilder.name("payment-topic").partitions(1).replicas(1).build();
	}
}