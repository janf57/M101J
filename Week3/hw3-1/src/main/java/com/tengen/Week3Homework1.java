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
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Week3Homework1 {

	public static void main(final String[] args) throws UnknownHostException {
		final MongoClient client = new MongoClient();

		final DB database = client.getDB("school");
		final DBCollection collection = database.getCollection("students");

		final DBCursor cursor = collection.find();

		while (cursor.hasNext()) {
			final DBObject student = cursor.next();

			final List<DBObject> scores = (List<DBObject>) student
					.get("scores");

			final DBObject minimalHomeWorkScore = minimalHomeWork(scores);
			collection.update(new BasicDBObject("_id", student.get("_id")),
					new BasicDBObject("$pull", new BasicDBObject("scores",
							minimalHomeWorkScore)), true, false);

		}

	}

	private static DBObject minimalHomeWork(final List<DBObject> scores) {
		DBObject minimalHomeWork = null;
		for (final DBObject score : scores) {
			if (score.get("type").equals("homework")) {
				minimalHomeWork = minimalScore(minimalHomeWork, score);
			}
		}
		return minimalHomeWork;
	}

	private static DBObject minimalScore(final DBObject oldScore,
			final DBObject newScore) {

		if (oldScore == null) {
			return newScore;
		}

		if (newScore == null) {
			return oldScore;
		}

		final Double oldscore = (Double) oldScore.get("score");
		final Double newscore = (Double) newScore.get("score");

		if (Double.compare(oldscore, newscore) > 0) {
			return newScore;
		}
		return oldScore;
	}
}
