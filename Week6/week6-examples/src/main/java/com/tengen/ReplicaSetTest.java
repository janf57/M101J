package com.tengen;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.Arrays;
/*
mkdir \data\rs1
mkdir \data\rs2
mkdir \data\rs3
mongod --replSet m101 --logpath "1.log" --dbpath /data/rs1 --port 27017 --smallfiles
mongod --replSet m101 --logpath "2.log" --dbpath /data/rs2 --port 27018 --smallfiles
mongod --replSet m101 --logpath "3.log" --dbpath /data/rs3 --port 27019 --smallfiles

config = { _id: "m101",
members:[
{ _id : 0, host : "localhost:27017", priority:1},
{ _id : 1, host : "localhost:27018", priority:0, slaveDelay:5},
{ _id : 2, host : "localhost:27019", priority:0} ]
}
rs.initiate(config)
 */
public class ReplicaSetTest {

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
   // 	MongoClient client = new MongoClient(new ServerAddress("localhost", 27017));
        MongoClient client = new MongoClient(Arrays.asList(
                new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019)
        ));

        DBCollection test = client.getDB("course").getCollection("replica.test");
        test.drop();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            test.insert(new BasicDBObject("_id", i));
            System.out.println("Inserted document: " + i);
            Thread.sleep(500);
        }
    }
}
