package com.chenwen.producer.interceptor;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class Interceptor02 implements ProducerInterceptor {

    Log log = LogFactory.get();

    @Override
    public ProducerRecord onSend(ProducerRecord producerRecord) {
        log.info(producerRecord.value() + "通过 " + this.getClass().getName());
        return producerRecord;
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
