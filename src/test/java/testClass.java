import cn.hutool.db.nosql.redis.RedisDS;
import cn.hutool.setting.GroupedMap;
import cn.hutool.setting.Setting;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.LinkedHashMap;
import java.util.List;

public class testClass {


    @Test
    public void testRedis() {


        Setting setting = new Setting("consumer.setting");

        List<String> groups = setting.getGroups();

        System.out.println(groups);

        GroupedMap groupedMap = setting.getGroupedMap();

        LinkedHashMap<String, String> datatoredis = groupedMap.get("redis");

        System.out.println(datatoredis);

        Jedis jedis = RedisDS.create().getJedis();

        System.out.println(jedis.isConnected());
    }


}

