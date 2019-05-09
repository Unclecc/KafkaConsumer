package com.chenwen.consumer;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.GroupedMap;
import cn.hutool.setting.Setting;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumerMain {

    private static Log log = LogFactory.get();

    public static void main(String[] args) {

        // 0. 创建线程池
        ExecutorService consumerPool = Executors.newCachedThreadPool();

        // 1. 获取所有消费者的配置
        Setting setting = new Setting("consumer.setting");
        GroupedMap groupedMap = setting.getGroupedMap();
        Set<String> groups = groupedMap.keySet();
        // 2. 遍历所有分组分别创建消费者
        for (String group : groups) {
            LinkedHashMap<String, String> consumerSetting = groupedMap.get(group);
            Map<String, Object> propsMap = new HashMap<>();
            Set<Map.Entry<String, String>> entries = consumerSetting.entrySet();
            // 迭代配置文件
            for (Map.Entry<String, String> entry : entries) {
                propsMap.put(entry.getKey(), entry.getValue());
            }

            String topic = consumerSetting.get("topic");
            Integer threads = Integer.parseInt(consumerSetting.get("consumer.threads"));
            String actionClass = consumerSetting.get("action.class");
            if(threads == null || threads == 0) {
                // 若没有指定线程数则默认按照partitions数量开启线程;
                KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(propsMap);
                int partitions = kafkaConsumer.partitionsFor(topic).size();
                consumerSetting.put("partitions", partitions+"");
                consumerPool.submit(new DataConsumer(kafkaConsumer, topic, ReflectUtil.newInstance(actionClass)));
                for (int i = 1; i < partitions; i++) {
                    KafkaConsumer<String, String> kafkaConsumer2 = new KafkaConsumer<>(propsMap);
                    consumerPool.submit(new DataConsumer(kafkaConsumer2, topic, ReflectUtil.newInstance(actionClass)));
                }
            } else {
                // 指定了线程数;
                log.info(topic + "消费者开启"+ threads + "个线程, 消费者即将创建..");

                for (int i = 0; i < threads ; i++) {
                    KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(propsMap);
                    consumerPool.submit(new DataConsumer(kafkaConsumer, topic, ReflectUtil.newInstance(actionClass)));
                }

            }
                log.info(topic + "消费者创建成功..");
        }

    }

}
