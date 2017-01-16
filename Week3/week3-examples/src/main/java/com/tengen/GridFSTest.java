package com.tengen;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GridFSTest {
    public static void main(String[] args) throws IOException {
        MongoClient client = new MongoClient();
        DB db = client.getDB("course");
        FileInputStream inputStream = null;

        GridFS videos = new GridFS(db, "videos");

        try {
            inputStream = new FileInputStream("video.mp4");
        } catch (FileNotFoundException e) {
            System.out.println("Can't open file");
            System.exit(1);
        }

        GridFSInputFile video = videos.createFile(inputStream, "video.mp4");

        // create some meta data for the object
        BasicDBObject meta = new BasicDBObject("description", "Jennifer Singing");
        List<String> tags = new ArrayList<String>();
        tags.add("Singing");
        tags.add("Opera");
        meta.append("tags", tags);

        video.setMetaData(meta);
        video.save();

        System.out.println("Objetc ID in Files Collection: " + video.get("_id"));


        System.out.println("Saved the file to MongoDB");
        System.out.println("Now lets read it back out");

        GridFSDBFile gridFile = videos.findOne(new BasicDBObject("filename", "video.mp4"));

        FileOutputStream outputStream = new FileOutputStream("video_copy.mp4");
        gridFile.writeTo(outputStream);

        System.out.println("write the file back");
    }
}
