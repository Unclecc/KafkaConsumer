package com.chenwen.action.impl;


import cn.hutool.db.nosql.redis.RedisDS;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.chenwen.action.IAction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

/**
 *
 * 由于当前我的系统只有Redis比较方便写入, 所以写个消费数据到Redis同步的动作.
 *
 * @author chenwen
 *
 */
public class DataToRedisAction implements IAction {

    private static final Log log = LogFactory.get();

    Jedis jedis;

    public DataToRedisAction() {
        this.jedis = RedisDS.create().getJedis();
        if(jedis.isConnected()) {
            log.info(Thread.currentThread().getName() + "--" + jedis.getClient().getHost() + "连接成功", Level.INFO);
        }
    }


    @Override
    public Object doAction(Iterator iterator) {

        while (iterator.hasNext()) {
            Object record_obj = iterator.next();
            ConsumerRecord<String, String> record = (ConsumerRecord<String, String>)record_obj;
            String key = Thread.currentThread().getName() + "_" +record.offset();
            JSONObject jsonObject = JSONObject.parseObject(record.value());

            Set<String> redisKeys = jsonObject.keySet();

            for (String  redisKey: redisKeys) {
                String value = jsonObject.getString(redisKey);
                jedis.hset(key, redisKey, value);
            }

            log.info(key + "插入成功..", Level.INFO);
        }

        return true;
    }

}
