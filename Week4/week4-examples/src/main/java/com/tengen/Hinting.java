package com.tengen;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Hinting {
	public static void main(final String[] args) throws UnknownHostException {
		final MongoClient client = new MongoClient();
		final DB db = client.getDB("test");

		final BasicDBObject query = new BasicDBObject("a", 40000);
		query.append("b", 40000);
		query.append("c", 40000);

		final DBCollection c = db.getCollection("foo");

		final DBObject doc = c.find(query).hint("a_1_b_-1_c_1").explain();

		// final BasicDBObject myHint = new BasicDBObject("a", 1).append("b",
		// -1).append("c", 1);
		// final DBObject doc = c.find(query).hint(myHint).explain();

		for (final String s : doc.keySet()) {
			System.out.printf("%25s:%s\n", s, doc.get(s));
		}
	}
}
