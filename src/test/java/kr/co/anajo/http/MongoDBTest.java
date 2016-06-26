package kr.co.anajo.http;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBTest {

	public MongoClient connection() {
		MongoClientOptions.Builder options = new MongoClientOptions.Builder();
		options.connectionsPerHost(5);
		options.connectTimeout(10);
		options.maxWaitTime(10);
		options.socketTimeout(30);
		MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 5050), options.build());
		return mongoClient;
	}

	@Test
	public void getDataBase() {
		MongoClient mongoClient = connection();
		MongoDatabase database1 = mongoClient.getDatabase("anajo");
		MongoDatabase database2 = mongoClient.getDatabase("anajo");
		Assert.assertEquals(database1, database2);
		mongoClient.close();
	}

	@Test
	public void getCollection() {
		MongoClient mongoClient = connection();
		MongoDatabase database = mongoClient.getDatabase("anajo");
		MongoCollection<Document> collection1 = database.getCollection("member");
		MongoCollection<Document> collection2 = database.getCollection("member");
		Assert.assertNotEquals(collection1, collection2);
		FindIterable<Document> iterable1 = collection1.find();
		FindIterable<Document> iterable2 = collection1.find();
		Assert.assertEquals(iterable1, iterable2);
		iterable1.forEach((Document document) -> System.out.println(document));
		mongoClient.close();
	}
}
