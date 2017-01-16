package com.tengen;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

public class ImportTweets {
    public static void main(String[] args) throws IOException, ParseException {
        final String screenName = args.length > 0 ? args[0] : "10gen";

        List<DBObject> tweets = getLatestTweets(screenName);

        MongoClient client = new MongoClient();
        DBCollection tweetsCollection = client.getDB("course").getCollection("twitter");
//        tweetsCollection.drop();

        for (DBObject tweet : tweets) {
            tweet.put("screen_name", screenName);
            massageTweetId(tweet);
            massageTweet(tweet);
            tweetsCollection.update(new BasicDBObject("_id", tweet.get("_id")), tweet, true, false);
        }

        System.out.println("Tweet count:" + tweets.size());
        client.close();
    }

    private static void massageTweet(DBObject tweet) throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat("EEE MMM d H:m:s Z y");
        tweet.put("created_at", fmt.parse((String) tweet.get("created_at")));

        DBObject userDoc = (DBObject) tweet.get("user");
        Iterator<String> keyIterator = userDoc.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            if (!(key.equals("id") || key.equals("name") || key.equals("screen_name"))) {
                keyIterator.remove();
            }
        }
    }

    private static void massageTweetId(DBObject tweet) {
        Object id = tweet.get("id");
        tweet.removeField("id");
        tweet.put("_id", id);
    }

    private static List<DBObject> getLatestTweets(String screenName) throws IOException {
        URL url = new URL("http://api.twitter.com/1/statuses/user_timeline.json?screen_name=" + screenName + "&include_rts=1");
        InputStream is = url.openStream();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int retVal;
        while ((retVal = is.read()) != -1) {
            os.write(retVal);
        }
        final String tweetsString = os.toString();
        return (List<DBObject>) JSON.parse(tweetsString);
    }
}
