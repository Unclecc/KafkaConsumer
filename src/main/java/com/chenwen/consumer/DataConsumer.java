package com.chenwen.consumer;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.chenwen.action.IAction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;

public class DataConsumer implements Runnable {

    private static Log log = LogFactory.get();

   IAction action;
    String topic;
    KafkaConsumer<String, String> kafkaConsumer;
    public DataConsumer(KafkaConsumer<String, String> kafkaConsumer, String topic, IAction action) {
        this.action = action;
        this.kafkaConsumer = kafkaConsumer;
        this.topic = topic;
    }


    @Override
    public void run() {
        kafkaConsumer.subscribe(Collections.singletonList(topic));

        log.info(Thread.currentThread().getName()+ "--" + topic + "--消费者创建成功..");

        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));

            Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
            Boolean acks = (Boolean) action.doAction(iterator);

            if(acks) {
                kafkaConsumer.commitSync();
            }

        }
    }


}













