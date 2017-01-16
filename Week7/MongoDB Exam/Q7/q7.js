// Your task is to write a program to remove every image from the images
// collection that appears in no album. Or put another way, if an image does not
// appear in at least one album, it's an orphan and should be removed from the
// images collection.

// Start by using mongoimport to import your albums.json and images.json
// collections. (Did you notice the links in the previous sentence?) When you are
// done removing the orphan images from the collection, there should be 90,017
// documents in the images collection. To prove you did it correctly, what are
// the total number of images with the tag 'sunrises' after the removal of
// orphans? As as a sanity check, there are 50,054 images that are tagged
// 'sunrises' before you remove the images. Hint: you might consider creating an
// index or two or your program will take a long time to run.

db.albums.ensureIndex({"images" : 1});null;

cur = db.images.find({}); null;
while(cur.hasNext()){
	img = cur.next(); null;
	if (db.albums.find({"images" : img._id}).count() == 0)
		db.images.remove({"_id":img._id});
}

db.images.find({"tags":"sunrises"}).count();