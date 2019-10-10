package top.thinkin.lightd.db;

import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import top.thinkin.lightd.exception.KitDBException;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
public class RMapTest {

    static int availProcessors = Runtime.getRuntime().availableProcessors();
    static ExecutorService executorService = Executors.newFixedThreadPool(availProcessors * 8);
    static DB db;

    @Before
    public void setUp() throws Exception {
        if (db == null) {
            try {
                db = DB.build("D:\\temp\\db", true);
            } catch (Exception e) {
                log.error("error", e);
                e.printStackTrace();
            }
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        Thread.sleep(5000);
    }

    @Test
    public void put() throws Exception {
        String head = "put0";
        RMap map = db.getMap();
        int num = 10 * 10000;
        try {
            for (int i = 0; i < num; i++) {
                map.put(head, "hello" + i, ("world" + i).getBytes());
            }

            for (int i = 0; i < num; i++) {
                byte[] bytes = map.get(head, ("hello" + i).getBytes());
                Assert.assertEquals(new String(bytes), ("world" + i));
            }
        } finally {
            map.delete(head);
        }
    }

    @Test
    public void putTTL() throws Exception {
        String head = "putTTL0";
        RMap map = db.getMap();
        int num = 10 * 10000;

        try {
            log.debug("start");
            for (int i = 0; i < num; i++) {
                map.putTTL(head, "hello" + i, ("world" + i).getBytes(), 3);
                if (i == num - 10) {
                    Thread.sleep(2800);
                }
            }
            log.debug("over");

            Thread.sleep(500);
            for (int i = 0; i < num; i++) {
                byte[] bytes = map.get(head, ("hello" + i).getBytes());
                Assert.assertEquals(new String(bytes), ("world" + i));
            }
            Thread.sleep(2500);

            for (int i = 0; i < num; i++) {
                byte[] bytes = map.get(head, ("hello" + i).getBytes());
                Assert.assertNull(bytes);
            }
        } finally {
            //map.delete(head);
        }
    }

    @Test
    public void putMayTTL() throws Exception {
        String head = "putMayTTL0";
        RMap map = db.getMap();
        int num = 10 * 10000;
        for (int i = 0; i < num; i++) {
            map.putMayTTL(head, 3, "hello" + i, ("world" + i).getBytes());
            if (i == num - 5) {
                map.putMayTTL(head, 300, "hello" + i, ("world" + i).getBytes());
            } else {
                map.putMayTTL(head, 3, "hello" + i, ("world" + i).getBytes());

            }
        }
        Thread.sleep(3000);
        for (int i = 0; i < num; i++) {
            byte[] bytes = map.get(head, ("hello" + i).getBytes());
            Assert.assertNull(bytes);
        }
    }

    @Test
    public void get() {

    }

    @Test
    public void get1() throws KitDBException {
        String head = "get1";
        RMap map = db.getMap();
        int num = 1000;
        try {
            String[] keys = new String[num];
            for (int i = 0; i < num; i++) {
                map.put(head, "hello" + i, ("hello" + i).getBytes());
                keys[i] = "hello" + i;
            }
            Map<String, byte[]> my_map = map.get(head, keys);
            Assert.assertEquals(num, my_map.size());
            for (String key : my_map.keySet()) {
                Assert.assertEquals(new String(my_map.get(key)), key);
            }
        } finally {
            map.delete(head);
        }
    }

    @Test
    public void remove() {

    }

    @Test
    public void delete() {
    }

    @Test
    public void getKeyIterator() {
    }

    @Test
    public void deleteFast() {
    }

    @Test
    public void getEntry() {
    }

    @Test
    public void getTtl() {
    }

    @Test
    public void delTtl() {
    }

    @Test
    public void ttl() {
    }

    @Test
    public void isExist() {
    }

    @Test
    public void size() {
    }
}