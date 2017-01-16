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

public class DotNotationTest {
	public static void main(final String[] args) throws UnknownHostException {
		final MongoClient client = new MongoClient();
		final DB db = client.getDB("course");
		final DBCollection lines = db.getCollection("DotNotationTest");
		lines.drop();
		final Random rand = new Random();

		// insert 10 lines with random start and end points
		for (int i = 0; i < 10; i++) {
			lines.insert(new BasicDBObject("_id", i).append(
					"start",
					new BasicDBObject("x", rand.nextInt(90) + 10).append("y",
							rand.nextInt(90) + 10)).append(
					"end",
					new BasicDBObject("x", rand.nextInt(90) + 10).append("y",
							rand.nextInt(90) + 10)));
		}

		// final QueryBuilder builder = QueryBuilder.start();//
		// "start.x").greaterThan(50);
		final QueryBuilder builder = QueryBuilder.start("start.x").greaterThan(
				50);

		// final DBCursor cursor = lines.find(builder.get());
		final DBCursor cursor = lines.find(builder.get(), new BasicDBObject(
				"start.y", true).append("_id", false));

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
