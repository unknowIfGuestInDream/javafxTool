/*
 * Copyright (c) 2024 unknowIfGuestInDream.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of unknowIfGuestInDream, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tlcsdm.core.jmh;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

/**
 * https://hg.openjdk.org/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/
 * https://developer.aliyun.com/article/899469
 *
 * @author unknowIfGuestInDream
 */
public class JmhTest {

    /**
     * @BenchmarkMode(Mode.All) Mode有：
     * - Throughput: 整体吞吐量，例如“1秒内可以执行多少次调用” (thrpt)
     * - AverageTime: 调用的平均时间，例如“每次调用平均耗时xxx毫秒”。（avgt）
     * - SampleTime: 随机取样，最后输出取样结果的分布，例如“99%的调用在xxx毫秒以内，99.99%的调用在xxx毫秒以内”（simple）
     * - SingleShotTime: 以上模式都是默认一次 iteration 是 1s，唯有 SingleShotTime 是只运行一次。往往同时把 warmup 次数设为0，用于测试冷启动时的性能。（ss）
     * @OutputTimeUnit(TimeUnit.MILLISECONDS) 统计单位， 微秒、毫秒 、分、小时、天
     * @Setup 方法注解，会在执行 benchmark 之前被执行，正如其名，主要用于初始化
     * @TearDown (Level) 方法注解，与@Setup 相对的，会在所有 benchmark 执行结束以后执行，主要用于资源的回收等。
     * (Level)   用于控制 @Setup，@TearDown 的调用时机，默认是 Level.Trial。
     * Trial：每个benchmark方法前后；
     * Iteration：每个benchmark方法每次迭代前后；
     * Invocation：每个benchmark方法每次调用前后，谨慎使用，需留意javadoc注释；
     * @Param @Param注解接收一个String数组 ，
     * 可以用来指定某项参数的多种情况。特别适合用来测试一个函数在不同的参数输入的情况下的性能。
     * Options常用选项
     * include benchmark 所在的类的名字，这里可以使用正则表达式对所有类进行匹配
     * warmupIterations 预热次数，每次默认1秒。
     * measurementIterations 实际测量的迭代次数，每次默认1秒。
     */
    @Benchmark
    @Warmup(iterations = 1, time = 3)
    @Fork(1)
    @BenchmarkMode(Mode.Throughput)
    @Measurement(iterations = 1, time = 3)
    @Test
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testForEach() {
        PS.foreach();
    }
}
