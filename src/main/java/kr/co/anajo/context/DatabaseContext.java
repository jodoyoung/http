package kr.co.anajo.context;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class DatabaseContext {

	private static MongoClient client;
	private static MongoDatabase database;

	public static synchronized void initialize() {
		MongoClientOptions.Builder options = new MongoClientOptions.Builder();
		options.connectionsPerHost(5);
		options.connectTimeout(10);
		options.maxWaitTime(10);
		options.socketTimeout(30);
		client = new MongoClient(new ServerAddress("localhost", 5050), options.build());
		database = client.getDatabase("anajo");
	}

	public static MongoClient getClient() {
		return client;
	}

	public static MongoDatabase getDatabase() {
		return database;
	}

	public static synchronized void destroy() {
		client.close();
	}
}
