package com.example.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.concurrent.CompletableFuture;

public class RedisCli {

	public static RedisClient redisClient = RedisClient
			  .create("redis://@192.168.4.145:6379/0");
	
	public static StatefulRedisConnection<String, String> connection
			 = redisClient.connect();
	
    
}
