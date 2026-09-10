package com.example.employee.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.example.employee.event.EmployeeCreatedEvent;
import com.example.employee.event.EmployeeDeletedEvent;

@Configuration
public class KafkaConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	private Map<String, Object> consumerProperties() {

		Map<String, Object> props = new HashMap<>();

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

		props.put(ConsumerConfig.GROUP_ID_CONFIG, "attendance-group");

		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		return props;
	}

	@Bean
	public ConsumerFactory<String, EmployeeCreatedEvent> employeeCreatedConsumerFactory() {

		JsonDeserializer<EmployeeCreatedEvent> jsonDeserializer = new JsonDeserializer<>(EmployeeCreatedEvent.class);

		jsonDeserializer.setUseTypeHeaders(false);

		ErrorHandlingDeserializer<EmployeeCreatedEvent> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(
				jsonDeserializer);

		return new DefaultKafkaConsumerFactory<>(consumerProperties(), new StringDeserializer(),
				errorHandlingDeserializer);
	}

	@Bean
	public ConsumerFactory<String, EmployeeDeletedEvent> employeeDeletedConsumerFactory() {

		JsonDeserializer<EmployeeDeletedEvent> jsonDeserializer = new JsonDeserializer<>(EmployeeDeletedEvent.class);

		jsonDeserializer.setUseTypeHeaders(false);

		ErrorHandlingDeserializer<EmployeeDeletedEvent> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(
				jsonDeserializer);

		return new DefaultKafkaConsumerFactory<>(consumerProperties(), new StringDeserializer(),
				errorHandlingDeserializer);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, EmployeeCreatedEvent> employeeCreatedKafkaListenerContainerFactory() {

		ConcurrentKafkaListenerContainerFactory<String, EmployeeCreatedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();

		factory.setConsumerFactory(employeeCreatedConsumerFactory());

		return factory;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, EmployeeDeletedEvent> employeeDeletedKafkaListenerContainerFactory() {

		ConcurrentKafkaListenerContainerFactory<String, EmployeeDeletedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();

		factory.setConsumerFactory(employeeDeletedConsumerFactory());

		return factory;
	}
}