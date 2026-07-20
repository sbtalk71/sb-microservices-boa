package com.demo.spring.kafka;

import java.util.HashMap;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.demo.spring.EmpDTO;

@Configuration
@EnableKafka
public class KafkaConfig {

	@Bean
	ProducerFactory<String, String> producerConfigFactory() {
		HashMap<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return new DefaultKafkaProducerFactory<>(config);
	}
	
	@Bean
	KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> factory){
		return new KafkaTemplate<>(factory);
	}
	
	//JSON Part COnfig
	
	@Bean
	ProducerFactory<String, EmpDTO> producerConfigFactoryJson() {
		HashMap<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(config);
	}
	
	@Bean
	KafkaTemplate<String, EmpDTO> kafkaTemplateJson(@Qualifier("producerConfigFactoryJson") ProducerFactory<String, EmpDTO> factory){
		return new KafkaTemplate<>(factory);
	}
}
