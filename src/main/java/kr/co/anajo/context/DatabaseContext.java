package kr.co.anajo.context;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import kr.co.anajo.context.annotation.Component;
import kr.co.anajo.context.annotation.Initialize;

@Component
public class DatabaseContext {

	private MongoClient client;
	private MongoDatabase database;

	@Initialize
	public void initialize() {
		MongoClientOptions.Builder options = new MongoClientOptions.Builder();
		options.connectionsPerHost(5);
		options.connectTimeout(10);
		options.maxWaitTime(10);
		options.socketTimeout(30);
		client = new MongoClient(new ServerAddress("localhost", 5050), options.build());
		database = client.getDatabase("anajo");
	}

	public MongoClient getClient() {
		return client;
	}

	public MongoDatabase getDatabase() {
		return database;
	}

	public void destroy() {
		client.close();
	}
}
