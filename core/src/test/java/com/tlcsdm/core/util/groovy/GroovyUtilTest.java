package com.tlcsdm.core.util.groovy;

import com.tlcsdm.core.util.GroovyUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/4/24 22:07
 */
@Disabled
public class GroovyUtilTest {
    /**
     * 测试没有参数的方法调用
     */
    @Test
    public void testGroovyWithoutParam() throws Exception {
        String result = (String) GroovyUtil.invokeMethod("hello2.groovy", "helloWithoutParam");
        System.out.println("testGroovy4: " + result + "\n");
    }

    /**
     * 测试携带参数的方法调用
     */
    @Test
    public void testGroovyWithParam() throws Exception {
        Person person = new Person("wchi", "nanjing", 30);
        String result = (String) GroovyUtil.invokeMethod("hello2.groovy", "helloWithParam", person, "testGroovy4");
        System.out.println("testGroovy4: " + result + "\n");
    }
}
