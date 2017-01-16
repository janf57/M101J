package course;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test {
    
    public static void main(String[] args) throws IOException {
        MongoClient c =  new MongoClient(new MongoClientURI("mongodb://localhost"));
        DB db = c.getDB("finaltask");
        int i =0;
        DBCollection album = db.getCollection("albums");
        DBCollection image = db.getCollection("images");
        
        DBCursor cur = image.find();
        cur.next();
        
        while (cur.hasNext()){
           Object id = cur.curr().get("_id");

           System.out.println("Imagen "+id);
           DBCursor curalbum = album.find(new BasicDBObject("images", id));
           if(!curalbum.hasNext()){
               image.remove(new BasicDBObject("_id", id));
           }
           cur.next();
        }
        System.out.println("Eliminados huerfanos");        
    }
}
