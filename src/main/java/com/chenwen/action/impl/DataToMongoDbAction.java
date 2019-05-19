package com.chenwen.action.impl;

import cn.hutool.db.nosql.mongo.MongoDS;
import cn.hutool.db.nosql.mongo.MongoFactory;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.Setting;
import com.alibaba.fastjson.JSONObject;
import com.chenwen.action.IAction;
import com.mongodb.client.MongoCollection;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * DataToMongoDbAction
 *
 * @author chenwen
 * @desc
 * @Date 2019/5/19 15:24
 */
public class DataToMongoDbAction implements IAction {

    private static final Log log = LogFactory.get();
    MongoDS mongoDS;
    MongoCollection<Document> mongoCollection;
    public DataToMongoDbAction() {
        Setting setting = new Setting("config/mongodb.setting");
        mongoDS = MongoFactory.getDS(setting.getStr("mongodb.host"), setting.getInt("mongodb.port"));
        mongoCollection = mongoDS.getCollection(setting.getStr("mongodb.db"), setting.getStr("mongodb.collection"));
        log.info(Thread.currentThread().getName() + "--" + mongoCollection.getNamespace() + "连接成功..");
    }

    @Override
    public Object doAction(Iterator iterator) {
        // 构建批量插入的list;
        List<Document> documentList = new ArrayList<>();

        while (iterator.hasNext()) {
            Object record_obj = iterator.next();
            ConsumerRecord<String, String> record = (ConsumerRecord<String, String>) record_obj;

            String value = record.value();

            // 将写入kafka的Json转成document;
            JSONObject jsonObject = JSONObject.parseObject(value);
            Set<String> keys = jsonObject.keySet();
            Document document = new Document();
            for (String key : keys) {
                Object o = jsonObject.get(key);
                document.append(key, o);
            }

            documentList.add(document);

        }

        if (documentList.size() > 0) {
            mongoCollection.insertMany(documentList);
        }

        return true;
    }
}
