import cn.hutool.core.util.StrUtil;
import org.junit.Test;

public class testClass {


    @Test
    public void testRedis() {


//        Setting setting = new Setting("consumer.setting");
//
//        List<String> groups = setting.getGroups();
//
//        System.out.println(groups);
//
//        GroupedMap groupedMap = setting.getGroupedMap();
//
//        LinkedHashMap<String, String> datatoredis = groupedMap.get("redis");
//
//        System.out.println(datatoredis);
//
//        Jedis jedis = RedisDS.create().getJedis();
//
//        System.out.println(jedis.isConnected());

        String str = "1";

        String[] strings = StrUtil.splitToArray(str, ',');

        System.out.println(strings[0]);
    }


}

