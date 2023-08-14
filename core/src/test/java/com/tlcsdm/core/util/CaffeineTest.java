package com.tlcsdm.core.util;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.github.benmanes.caffeine.cache.Weigher;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.annotation.Nullable;

/**
 * Caffeine
 */
public class CaffeineTest {

    /**
     * 手动加载
     */
    @Test
    void manual() {
        // 初始化缓存，设置了1分钟的写过期，100的缓存最大个数
        Cache<Integer, Integer> cache = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).maximumSize(100)
            .build();
        int key1 = 1;
        // 使用getIfPresent方法从缓存中获取值。如果缓存中不存指定的值，则方法将返回 null：
        System.out.println(cache.getIfPresent(key1));

        // 也可以使用 get 方法获取值，该方法将一个参数为 key 的 Function 作为参数传入。如果缓存中不存在该 key
        // 则该函数将用于提供默认值，该值在计算后插入缓存中：
        System.out.println(cache.get(key1, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return 2;
            }
        }));

        // 校验key1对应的value是否插入缓存中
        System.out.println(cache.getIfPresent(key1));

        // 手动put数据填充缓存中
        int value1 = 2;
        cache.put(key1, value1);

        // 使用getIfPresent方法从缓存中获取值。如果缓存中不存指定的值，则方法将返回 null：
        System.out.println(cache.getIfPresent(1));

        // 移除数据，让数据失效
        cache.invalidate(1);
        System.out.println(cache.getIfPresent(1));
    }

    /**
     * 自动加载
     */
    @Test
    void loading() {
        // 初始化缓存，设置了1分钟的写过期，100的缓存最大个数
        LoadingCache<Integer, Integer> cache = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES)
            .maximumSize(100).build(new CacheLoader<Integer, Integer>() {
                @Nullable
                @Override
                public Integer load(@NonNull Integer key) {
                    return key + 1;
                }
            });

        int key1 = 1;
        // get数据，取不到则从数据库中读取相关数据，该值也会插入缓存中：
        Integer value1 = cache.get(key1);
        System.out.println(value1);

        // 支持直接get一组值，支持批量查找
        Map<Integer, Integer> dataMap = cache.getAll(Arrays.asList(1, 2, 3));
        System.out.println(dataMap);
    }

    /**
     * 异步加载
     */
    @Test
    void asynChronous() throws ExecutionException, InterruptedException {
        // 使用executor设置线程池
        AsyncCache<Integer, Integer> asyncCache = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES)
            .maximumSize(100)
            // .executor(Executors.newSingleThreadExecutor())
            .buildAsync();
        Integer key = 1;
        // get返回的是CompletableFuture
        CompletableFuture<Integer> future = asyncCache.get(key, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer key) {
                // 执行所在的线程不在是main，而是ForkJoinPool线程池提供的线程
                System.out.println("当前所在线程：" + Thread.currentThread().getName());
                int value = key + 1;
                return value;
            }
        });

        int value = future.get();
        System.out.println("当前所在线程：" + Thread.currentThread().getName());
        System.out.println(value);
    }

    /**
     * 基于大小淘汰
     */
    @Test
    void sizeEviction() throws InterruptedException {
        // 初始化缓存，缓存最大个数为1
        Cache<Integer, Integer> cache = Caffeine.newBuilder().maximumSize(1).build();

        cache.put(1, 1);
        // 打印缓存个数，结果为1
        System.out.println(cache.estimatedSize());

        cache.put(2, 2);
        // 稍微休眠一秒
        Thread.sleep(1000);
        // 打印缓存个数，结果为1
        System.out.println(cache.estimatedSize());
    }

    /**
     * 基于缓存权重
     */
    @Test
    void weightEviction() throws InterruptedException {
        // 初始化缓存，设置最大权重为2
        Cache<Integer, Integer> cache = Caffeine.newBuilder().maximumWeight(2).weigher(new Weigher<Integer, Integer>() {
            @Override
            public @NonNegative int weigh(@NonNull Integer key, @NonNull Integer value) {
                return key;
            }
        }).build();

        cache.put(1, 1);
        // 打印缓存个数，结果为1
        System.out.println(cache.estimatedSize());

        cache.put(2, 2);
        // 稍微休眠一秒
        Thread.sleep(1000);
        // 打印缓存个数，结果为1
        System.out.println(cache.estimatedSize());
    }

    /**
     * 访问后到期
     */
    @Test
    void testEvictionAfterProcess() throws InterruptedException {
        // 设置访问5秒后数据到期
        Cache<Integer, Integer> cache = Caffeine.newBuilder().expireAfterAccess(5, TimeUnit.SECONDS)
            .scheduler(Scheduler.systemScheduler()).build();
        cache.put(1, 2);
        System.out.println(cache.getIfPresent(1));

        Thread.sleep(6000);

        System.out.println(cache.getIfPresent(1));
    }

    /**
     * 写入后到期
     */
    @Test
    void testEvictionAfterWrite() throws InterruptedException {
        // 设置写入5秒后数据到期
        Cache<Integer, Integer> cache = Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS)
            .scheduler(Scheduler.systemScheduler()).build();
        cache.put(1, 2);
        System.out.println(cache.getIfPresent(1));

        Thread.sleep(6000);

        System.out.println(cache.getIfPresent(1));
    }

    /**
     * 自定义过期时间
     */
    @Test
    void testEvictionAfter() throws InterruptedException {
        Cache<Integer, Integer> cache = Caffeine.newBuilder().expireAfter(new Expiry<Integer, Integer>() {
            // 创建1秒后过期，可以看到这里必须要用纳秒
            @Override
            public long expireAfterCreate(@NonNull Integer key, @NonNull Integer value, long currentTime) {
                return TimeUnit.SECONDS.toNanos(1);
            }

            // 更新2秒后过期，可以看到这里必须要用纳秒
            @Override
            public long expireAfterUpdate(@NonNull Integer key, @NonNull Integer value, long currentTime,
                @NonNegative long currentDuration) {
                return TimeUnit.SECONDS.toNanos(2);
            }

            // 读3秒后过期，可以看到这里必须要用纳秒
            @Override
            public long expireAfterRead(@NonNull Integer key, @NonNull Integer value, long currentTime,
                @NonNegative long currentDuration) {
                return TimeUnit.SECONDS.toNanos(3);
            }
        }).scheduler(Scheduler.systemScheduler()).build();

        cache.put(1, 2);

        System.out.println(cache.getIfPresent(1));

        Thread.sleep(6000);

        System.out.println(cache.getIfPresent(1));
    }

    /**
     * 基于引用淘汰
     */
    @Test
    void testWeak() {
        Cache<Integer, Integer> cache = Caffeine.newBuilder()
            // 设置Key为弱引用，生命周期是下次gc的时候
            .weakKeys()
            // 设置value为弱引用，生命周期是下次gc的时候
            .weakValues().build();
        cache.put(1, 2);
        System.out.println(cache.getIfPresent(1));

        // 强行调用一次GC
        System.gc();

        System.out.println(cache.getIfPresent(1));
    }

    @Test
    void testSoft() {
        Cache<Integer, Integer> cache = Caffeine.newBuilder()
            // 设置value为软引用，生命周期是GC时并且堆内存不够时触发清除
            .softValues().build();
        cache.put(1, 2);
        System.out.println(cache.getIfPresent(1));

        // 强行调用一次GC
        System.gc();

        System.out.println(cache.getIfPresent(1));
    }

    /**
     * 刷新
     */
    @Test
    void refresh() throws InterruptedException {
        // 设置写入后3秒后数据过期，2秒后如果有数据访问则刷新数据
        LoadingCache<Integer, Integer> cache = Caffeine.newBuilder().refreshAfterWrite(2, TimeUnit.SECONDS)
            .expireAfterWrite(3, TimeUnit.SECONDS).build(new CacheLoader<Integer, Integer>() {
                @Nullable
                @Override
                public Integer load(@NonNull Integer key) {
                    return new Random().nextInt();
                }
            });
        cache.put(1, new Random().nextInt());

        // 休眠2.5秒，后取值
        Thread.sleep(2500);
        System.out.println(cache.getIfPresent(1));

        // 休眠1.5秒，后取值
        Thread.sleep(1500);
        System.out.println(cache.getIfPresent(1));
    }

    /**
     * 充当二级缓存用，生命周期仅活到下个gc
     */
    private Map<Integer, WeakReference<Integer>> secondCacheMap = new ConcurrentHashMap<>();

    /**
     * 二级缓存
     */
    @Test
    void writer() throws InterruptedException {
        // 设置最大缓存个数为1
//        LoadingCache<Integer, Integer> cache = Caffeine.newBuilder().maximumSize(1)
//            .evictionListener(new RemovalListener<Integer, Integer>() {
//
//                @Override
//                public void onRemoval(@org.checkerframework.checker.nullness.qual.Nullable Integer key,
//                    @org.checkerframework.checker.nullness.qual.Nullable Integer value, RemovalCause cause) {
//                    secondCacheMap.put(key, new WeakReference<>(value));
//                    switch (cause) {
//                    case EXPLICIT:
//                        secondCacheMap.remove(key);
//                        System.out.println("触发CacheWriter" + ".delete，清除原因：主动清除，将key = " + key + "从二级缓存清除");
//                        break;
//                    case SIZE:
//                        System.out.println("触发CacheWriter" + ".delete，清除原因：缓存个数超过上限，key = " + key);
//                        break;
//                    default:
//                        break;
//                    }
//                }
//
//            }).build(new CacheLoader<Integer, Integer>() {
//                @Nullable
//                @Override
//                public Integer load(@NonNull Integer key) {
//                    WeakReference<Integer> value = secondCacheMap.get(key);
//                    if (value == null) {
//                        return null;
//                    }
//
//                    System.out.println("触发CacheLoader.load，从二级缓存读取key = " + key);
//                    return value.get();
//                }
//            });
//
//        cache.put(1, 1);
//        cache.put(2, 2);
//        // 由于清除缓存是异步的，因而睡眠1秒等待清除完成
//        Thread.sleep(1000);
//
//        // 缓存超上限触发清除后
//        System.out.println("从Caffeine中get数据，key为1，value为" + cache.get(1));
    }

    /**
     * 统计
     */
    @Test
    void record() {
        LoadingCache<Integer, Integer> cache = Caffeine.newBuilder()
            // 开启记录
            .recordStats().build(new CacheLoader<Integer, Integer>() {
                @Override
                public @Nullable Integer load(@NonNull Integer key) {
                    return key + 1;
                }
            });
        cache.get(1);

        // 命中率
        System.out.println(cache.stats().hitRate());
        // 被剔除的数量
        System.out.println(cache.stats().evictionCount());
        // 加载新值所花费的平均时间[纳秒]
        System.out.println(cache.stats().averageLoadPenalty());
    }

    @Test
    void removalListener() throws InterruptedException {
        LoadingCache<Integer, Integer> cache = Caffeine.newBuilder().expireAfterAccess(1, TimeUnit.SECONDS)
            .scheduler(Scheduler.systemScheduler())
            // 增加了淘汰监听
            .removalListener(((key, value, cause) -> {
                System.out.println("淘汰通知，key：" + key + "，原因：" + cause);
            })).build(new CacheLoader<Integer, Integer>() {
                @Override
                public @Nullable Integer load(@NonNull Integer key) throws Exception {
                    return key;
                }
            });

        cache.put(1, 2);

        Thread.currentThread().sleep(2000);
    }
}
