package com.chenwen.consumer;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.GroupedMap;
import cn.hutool.setting.Setting;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;
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
            Properties properties = setting.getProperties(group);
            String topic = properties.get("topic").toString();
            Integer threads = Integer.parseInt(properties.get("consumer.threads").toString());
            String actionClass = properties.get("action.class").toString();

            if(threads == null || threads == 0) {
                // 若没有指定线程数则默认按照partitions数量开启线程;
                KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
                int partitions = kafkaConsumer.partitionsFor(topic).size();
                log.info("消费组" + group + "无指定消费线程数, 正在按照topic的partition数量 "+ partitions +"创建线程数目..");

                consumerPool.submit(new DataConsumer(kafkaConsumer, topic, ReflectUtil.newInstance(actionClass)));
                for (int i = 1; i < partitions; i++) {
                    KafkaConsumer<String, String> kafkaConsumer2 = new KafkaConsumer<>(properties);
                    consumerPool.submit(new DataConsumer(kafkaConsumer2, topic, ReflectUtil.newInstance(actionClass)));
                }
            } else {
                // 指定了线程数;
                log.info("消费者组" + group + "指定了线程数量" + threads + ", 正在创建线程..");

                for (int i = 0; i < threads ; i++) {
                    KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
                    consumerPool.submit(new DataConsumer(kafkaConsumer, topic, ReflectUtil.newInstance(actionClass)));
                }

            }
                log.info(topic + "消费者创建成功..");
        }

    }

}
