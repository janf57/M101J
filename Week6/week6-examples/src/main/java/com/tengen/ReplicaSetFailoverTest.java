package com.tengen;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.Arrays;

public class ReplicaSetFailoverTest {

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        MongoClient client = new MongoClient(Arrays.asList(
                new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019)
        ));

        DBCollection test = client.getDB("course").getCollection("replica.test");
        test.drop();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            for (int retries = 0; retries <= 2; retries++) {
                try {
                    test.insert(new BasicDBObject("_id", i));
                    System.out.println("Inserted document: " + i);
                    break;
                } catch (MongoException.DuplicateKey e) {
                    System.out.println("Document already inserted: " + i);
                } catch (MongoException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Retrying");
                    Thread.sleep(5000);
                }
            }
            Thread.sleep(500);
        }
    }
}
