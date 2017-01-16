/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.tengen;

import java.net.UnknownHostException;
import java.util.Random;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

public class FindCriteriaTest {
	public static void main(final String[] args) throws UnknownHostException {
		final MongoClient client = new MongoClient();
		final DB db = client.getDB("course");
		final DBCollection collection = db.getCollection("findCriteriaTest");
		collection.drop();

		// insert 10 documents with two random integers
		for (int i = 0; i < 10; i++) {
			collection.insert(new BasicDBObject("x", new Random().nextInt(2))
					.append("y", new Random().nextInt(100)));
		}

		final QueryBuilder builder = QueryBuilder.start("x").is(0).and("y")
				.greaterThan(10).lessThan(70);
		final DBObject query = new BasicDBObject("x", 0).append("y",
				new BasicDBObject("$gt", 10).append("$lt", 90));

		System.out.println("\nCount:");
		// final long count = collection.count(query);
		final long count = collection.count(builder.get());
		System.out.println(count);

		System.out.println("\nFind all: ");
		final DBCursor cursor = collection.find(builder.get());
		try {
			while (cursor.hasNext()) {
				final DBObject cur = cursor.next();
				System.out.println(cur);
			}
		} finally {
			cursor.close();
		}
	}
}
