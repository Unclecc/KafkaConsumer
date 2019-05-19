package com.chenwen.test;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.db.nosql.mongo.MongoDS;
import cn.hutool.db.nosql.mongo.MongoFactory;
import com.chenwen.producer.entity.Student;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TestMongodb
 *
 * @author chenwen
 * @desc
 * @Date 2019/5/18 17:11
 */
public class TestMongodb {

    @Test
    public void testConnect() {

        MongoDS mongoDS = MongoFactory.getDS("centos01", 27017);

        MongoCollection<Document> collection = mongoDS.getCollection("test", "testindex");

        List<Document> documentList = new ArrayList<>();

        for (int i = 0; i < 20000; i++) {
            Student student = new Student(i, "person" + i, RandomUtil.randomInt(30), new Date(), 98.7f);
            Document document = new Document();
            document.append("id", student.getId());
            document.append("name", student.getStuName());
            document.append("date", student.getBirthday());
            document.append("grade", student.getGrade());
            document.append("age", student.getAge());

            documentList.add(document);
        }

        collection.insertMany(documentList);
        mongoDS.close();
    }

}



















