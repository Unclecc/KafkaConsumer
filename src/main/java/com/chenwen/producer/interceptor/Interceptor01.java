package com.chenwen.producer.interceptor;


import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 *
 *
 * 时间拦截器
 *
 * @author chenwen
 *
 */
public class Interceptor01 implements ProducerInterceptor<String, String> {

    Log log = LogFactory.get();

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        log.info(record.value()+ "通过" + this.getClass().getName());
        return record;
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
