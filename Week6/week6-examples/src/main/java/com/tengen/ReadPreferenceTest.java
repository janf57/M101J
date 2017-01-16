package com.tengen;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.Arrays;

public class ReadPreferenceTest {

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        MongoClient client = new MongoClient(Arrays.asList(
                new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019)
        ));
        client.setReadPreference(ReadPreference.primary());

        DB db = client.getDB("course");
        db.setReadPreference(ReadPreference.primary());
        DBCollection coll = db.getCollection("write.test");
        coll.setReadPreference(ReadPreference.primaryPreferred());

        DBCursor cursor = coll.find().setReadPreference(ReadPreference.nearest());
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
    }
}
