package com.wolf.test.mongodb;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.set;
import static java.util.Arrays.asList;

/**
 * Description:
 * Created on 2021/3/25 4:47 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class MongodbTest {

    private static final Logger log = LoggerFactory.getLogger(MongodbTest.class);

    public static void main(String[] args) {
        // baseSelect();

        //testCondition();

        //testMap();

        //testAggregate();

        testAggregate2();


        //testInsert();

        //testUpdate();

        //testDelete();
    }

    private static void testDelete() {
        // delete 操作
        MongoClient mongoClient = null;
        MongoCollection<Document> blogs = mongoClient.getDatabase("testdb").getCollection("blog");
        //删除第一条满足查询条件的文档记录，对应 delete(<query>,{justOne:true}) 方法
        blogs.deleteOne(eq("author", "Alex"));

        //删除所有满足查询条件的文档记录，对应 delete(<query>,{justOne:false}) 方法
        blogs.deleteMany(gte("likes", 900));
    }

    // update 操作
    private static void testUpdate() {
        MongoClient mongoClient = null;
        MongoCollection<Document> blogs = mongoClient.getDatabase("testdb").getCollection("blog");

        //更改第一条匹配条件的文档记录,对应 update(<query>,{muti:false}) 方法
        blogs.updateOne(and(eq("author", "assad"), eq("title", "SqlLite Overview")),
                and(set("author", "assad_1"), set("likes", 0)));// todo待确定

        //更改所有匹配条件的文档记录,对应 update(<query>,{muti:true}) 方法
        blogs.updateMany(lte("likes", 300)
                , inc("likes", 100));

        //完全替换一条文档记录，对应 save() 方法
        Document doc = new Document("title", "Mysql Overview")
                .append("description", "Musql is a RDBMS")
                .append("author", "assad")
                .append("url", "http://blog.assad.article/233")
                .append("tages", Arrays.asList("mysql", "RDBMS", "sql"))
                .append("likes", 100);
        blogs.replaceOne(eq("author", "Alex"), doc);
    }

    // insert 插入操作
    private static void testInsert() {
        MongoClient mongoClient = null;
        MongoCollection<Document> blogs = mongoClient.getDatabase("testdb").getCollection("blog");

        //插入一个文档
        Document doc = new Document("title", "Mysql Overview")
                .append("description", "Musql is a RDBMS")
                .append("author", "assad")
                .append("url", "xxx")
                .append("tages", Arrays.asList("mysql", "RDBMS", "sql"))
                .append("likes", 100);
        blogs.insertOne(doc);

        //插入多个文档
        Document doc1 = new Document("title", "DB2 Overview")
                .append("author", "Alex");
        Document doc2 = new Document("title", "Redis Overview")
                .append("author", "Orlda");
        List<Document> docList = new ArrayList<>();
        docList.add(doc1);
        docList.add(doc2);
        blogs.insertMany(docList);
    }

    // aggregate 数据聚合操作
    private static void testAggregate() {
        MongoClient mongoClient = null;
        MongoCollection<Document> blogs = mongoClient.getDatabase("testdb").getCollection("blog");

        //数据聚合: db.blog.aggregate([ {$group:{_id:"$author",likes_count:{$sum:"$likes"}}} ])
        blogs.aggregate(asList(group("$author", sum("likes_count", "$likes"))))
                .forEach((Block<Document>) document -> {
                    log.debug(document.toJson());
                });

        //数据聚合: db.blog.aggregate([ {$group:{_id:"$author",likes_avg:{$avg:"$likes"}}}, {$match:{_id:"assad"}} ])
        blogs.aggregate(asList(
                group("$author", avg("likes_avg", "$likes")),
                match(eq("_id", "assad"))))
                .forEach((Block<Document>) document -> {
                    log.debug(document.toJson());
                });
    }

    // 汇总
    // 按照appId=2e1800b22ae70600 && leaveTime>2017-07-12T00:00:00 && leaveTime<2017-07-13T00:00:00过滤
    // 对上述结果进行分组，以leaveMethod字段分组，每组的数量*1，每组的列有两个：_id/count
    // 对上述结果进行排序，按照_id正序
    // db.getCollection('parking_record').aggregate(
    //{$match : {"appId" : "2e1800b22ae70600", "leaveTime" : {"$gt" : ISODate("2017-07-12T00:00:00"), "$lt" : ISODate("2017-07-13T00:00:00")}}},
    //{$group : {"_id" : "$leaveMethod", "count" : {$sum : 1}}},
    //{$sort : {"_id" : 1}}
    //)
    private static void testAggregate2() {
        MongoClient mongoClient = null;
        MongoCollection<Document> collection = mongoClient.getDatabase("x").getCollection("a");

        int app_id = 1;
        Date beginDate = new Date();
        Date endDate = new Date();

        Document sub_match = new Document();
        sub_match.put("appId", app_id);
        sub_match.put("leaveTime", new Document("$gt", beginDate).append("$lt", endDate));

        Document sub_group = new Document();
        sub_group.put("_id", "$leaveMethod");
        sub_group.put("count", new Document("$sum", 1));

        Document match = new Document("$match", sub_match);
        Document group = new Document("$group", sub_group);
        Document sort = new Document("$sort", new Document("_id", 1));

        List<Document> aggregateList = new ArrayList<Document>();
        aggregateList.add(match);
        aggregateList.add(group);
        aggregateList.add(sort);

        JSONObject ret_obj = new JSONObject();
        AggregateIterable<Document> resultset = collection.aggregate(aggregateList);

        MongoCursor<Document> cursor = resultset.iterator();
        try {
            while (cursor.hasNext()) {
                Document item_doc = cursor.next();
                int leaveMethod = item_doc.getInteger("_id", 0);
                int count = item_doc.getInteger("count", 0);
                System.out.println(leaveMethod + "_" + count);
            }
        } finally {
            cursor.close();
        }
    }

    // 查询结果映射，排序，限制
    private static void testMap() {
        MongoClient mongoClient = null;
        MongoCollection<Document> blogs = mongoClient.getDatabase("testdb").getCollection("blog");

        //映射操作, db.blog.find({"author:"assad""},{"title":1,"url":1,"likes":1,"_id":0})
        blogs.find(new Document("author", "assad"))
                .projection(new Document("title", 1).append("url", 1).append("likes", 1).append("_id", 0))
                .forEach((Block<Document>) document -> {
                    log.debug(document.toJson());
                });
        blogs.find(eq("author", "assad"))
                .projection(fields(include("title", "url", "likes"), excludeId()))
                .forEach((Block<Document>) document -> {
                    log.debug(document.toJson());
                });

        //查询排序、限制： db.blog.find().sort({"likes":1,"title":-1}).limit(5)
        blogs.find()
                .sort(and(ascending("likes"), descending("title")))
                .limit(5)
                .forEach((Block<Document>) document -> {
                    log.debug(document.toJson());
                });

        blogs.find()
                .sort(new Document("likes", 1).append("title", -1))
                .limit(5)
                .forEach((Block<Document>) document -> {
                    log.debug(document.toJson());
                });
    }

    // 条件查询
    private static void testCondition() {
        MongoClient mongoClient = null;
        MongoCollection<Document> blogs = mongoClient.getDatabase("testdb").getCollection("blog");
        //条件查询: db.blog.find({"author":"assad"})
        blogs.find(new Document("author", "assad")).forEach((Block<Document>) document -> {
            log.debug(document.toJson());
        });
        // 使用 Filters 中的各种静态方法
        blogs.find(eq("author", "assad")).forEach((Block<Document>) document -> {
            log.debug(document.toJson());
        });


        //条件查询: db.blog.find({"likes":{$gte:200,$lte:500}})
        blogs.find(new Document("likes", new Document("$gte", 200).append("$lte", 500)))
                .forEach((Block<Document>) document -> {
                    log.debug(document.toJson());
                });
        blogs.find(and(gte("likes", 200), lte("likes", 500)))
                .forEach((Block<Document>) document -> {
                    log.debug(document.toJson());
                });

        //条件查询：db.blog.find({"author":"assad","title":/mongodb*/i })
        blogs.find(new Document("author", "assad").append("title", new Document("$regex", "mongodb*").append("$options", "i")))
                .forEach((Block<Document>) document -> {
                    log.debug(document.toJson());
                });
        blogs.find(and(eq("author", "assad"), regex("title", "mongodb*", "i")))
                .forEach((Block<Document>) document -> {
                    log.debug(document.toJson());
                });
    }

    private static void baseSelect() {
        MongoClient mongoClient = null;

        MongoCollection<Document> blogs = mongoClient.getDatabase("testdb").getCollection("blog");
        //查询所有文档，遍历输出结果,用for
        for (Document document1 : blogs.find())
            log.debug(document1.toJson());

        //查询所有文档，使用遍历器遍历结果，用iterator
        MongoCursor<Document> cursor = blogs.find().iterator();
        while (cursor.hasNext())
            log.debug(cursor.next().toJson());

        //查询所有文档，对所有结果使用阻塞回调方法
        blogs.find().forEach((Block<Document>) document -> {
            log.debug(document.toJson());
        });

        //查询所有文档，获取结果文档中的 title 字段（string）、likes 字段（int32）、tags 字段（array）
        blogs.find().forEach((Block<Document>) document -> {
            String title = document.getString("title");
            int likes = document.getInteger("likes");
            List<String> tags = (List<String>) document.get("tags");
            log.debug(title + " - " + likes + " - " + tags);
        });
    }
}
