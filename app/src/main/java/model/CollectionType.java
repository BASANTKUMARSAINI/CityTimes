package model;

import java.util.List;

public class CollectionType {
     String collectionName;
     String hicollectionName;
     String uid;
     String id;


    public CollectionType() {
    }

    public String getHicollectionName() {
        return hicollectionName;
    }

    public String getUid() {
        return uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setHicollectionName(String hicollectionName) {
        this.hicollectionName = hicollectionName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }


}
