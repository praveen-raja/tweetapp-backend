package com.tweetapp.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
	
	private static final Logger log = LogManager.getLogger(KafkaConsumerService.class);

	@KafkaListener(groupId = "tweet-1", topics = "tweet", containerFactory = "kafkaListenerContainerFactory")
	public void consumeLogin(String message) {
		log.info("Kafka login: " + message);
	}

}
