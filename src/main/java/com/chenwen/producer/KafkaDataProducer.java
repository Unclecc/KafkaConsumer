package com.chenwen.producer;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.Setting;
import com.alibaba.fastjson.JSON;
import com.chenwen.producer.entity.Student;
import org.apache.kafka.clients.producer.*;

import java.util.*;

public class KafkaDataProducer {

    private static Log log = LogFactory.get();
    public static void main(String[] args) {
        List<Student> studentList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            studentList.add(new Student(i, "person"+i, i, new Date(), 0.5f + i));
        }

        produceProcess("mongodb", studentList);
    }

    public static void produceProcess(String dbType, Collection collection) {
        Setting setting = new Setting("producer.setting");
        Properties props = setting.getProperties(dbType);
        // 查找拦截器
        String interceptors = props.get("interceptors").toString();
        if(StrUtil.isNotBlank(interceptors)) {
            props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptors);
        }
        Producer<String, String> producer = new KafkaProducer<>(props);
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()){
            try {
                Object next = iterator.next();
                // 调用get可以使kafka所有partition都完成acks才commit
                producer.send(new ProducerRecord<String, String>(props.get("topic").toString(), JSON.toJSONString(next)), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        log.info("offset ="+ recordMetadata.offset()+", partition = "+ recordMetadata.partition()+", value = " + next.toString());
                    }
                }).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        producer.close();

    }

}
