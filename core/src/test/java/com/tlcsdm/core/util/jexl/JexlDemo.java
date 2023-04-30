/*
 * Copyright (c) 2023 unknowIfGuestInDream
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

package com.tlcsdm.core.util.jexl;

import org.apache.commons.jexl3.*;
import org.apache.commons.jexl3.introspection.JexlPermissions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/4/23 21:32
 */
@DisabledIfEnvironmentVariable(named = "ENV", matches = "workflow", disabledReason = "The scope of JEXL is provided")
public class JexlDemo {

    @Test
    public void testNew() throws Exception {
        // 描述一个人，他有两条腿
        Map<String, Object> person = new HashMap<String, Object>();
        person.put("skinColor", "red"); // 皮肤为红色
        person.put("age", 23); // 年龄23
        person.put("cash", 60.8); // 身上有60.8元现金

        // 左腿定义
        Map<String, Object> leg1 = new HashMap<String, Object>();
        leg1.put("leftOrRight", "left"); // 左腿
        leg1.put("length", 20.3); // 腿长多少
        leg1.put("hair", 3000); // 有多少腿毛

        // 右腿定义
        Map<String, Object> leg2 = new HashMap<String, Object>();
        leg2.put("leftOrRight", "right"); // 右腿
        leg2.put("length", 20.3); // 腿长多少
        leg2.put("hair", 3050); // 有多少腿毛
        // 给他两条腿
        List<Map<String, Object>> legs = new ArrayList<Map<String, Object>>();
        legs.add(leg1);
        legs.add(leg2);
        person.put("leg", legs);

        // 让这个人变成一个Context，以便Jexl认识他
        JexlContext context = new MapContext(person);

        JexlEngine engine = new JexlBuilder().strict(false).imports(Arrays.asList("java.lang", "java.math"))
            .permissions(null).cache(128).create();

        // 看看这个人是否年龄在30岁以上，并且身上有超过100元现金
        boolean yes = (Boolean) engine.createExpression("age>30 && cash>100").evaluate(context);
        System.out.println("年龄在30岁以上，并且身上有超过100元现金?  " + yes); // 他没有

        // 看看这个人是否左腿上有超过2500根汗毛
        yes = (Boolean) engine.createExpression("leg[0].hair>2500").evaluate(context);

        System.out.println("左腿上有超过2500根汗毛?  " + yes); // 是的，他有
    }

    @Test
    public void demo1() {
        JexlEngine engine = new JexlBuilder().strict(false).imports(Arrays.asList("java.lang", "java.math"))
            .permissions(null).cache(128).create();
        String calculateTax = "((G1 + G2 + G3) * 0.1) + G4";
        JexlContext context = new MapContext();
        context.set("G1", 1);
        context.set("G2", 2);
        context.set("G3", 3);
        context.set("G4", 4);
        Number result = (Number) engine.createExpression(calculateTax).evaluate(context);
        System.out.println(result);
    }

    @Test
    public void demo2() {
        // Restricting features; no loops, no side effects
        JexlFeatures features = new JexlFeatures().loops(true).sideEffectGlobal(false).sideEffect(false);
        // Restricted permissions to a safe set but with URI allowed
        JexlPermissions permissions = new JexlPermissions.ClassPermissions(java.net.URI.class);
        JexlEngine jexl = new JexlBuilder().features(features).strict(false)
            .imports(Arrays.asList("java.lang", "java.math")).permissions(permissions).cache(128).create();
        // let's assume a collection of uris need to be processed and transformed to be
        // simplified ;
        // we want only http/https ones, only the host part and forcing an https scheme
        List<URI> uris = Arrays.asList(URI.create("http://user@www.apache.org:8000?qry=true"),
            URI.create("https://commons.apache.org/releases/prepare.html"), URI.create("mailto:henrib@apache.org"));
        // Create the test control, the expected result of our script evaluation
        List<?> control = uris.stream()
            .map(uri -> uri.getScheme().startsWith("http") ? "https://" + uri.getHost() : null)
            .filter(x -> x != null).collect(Collectors.toList());
        Assertions.assertEquals(2, control.size());

        // Create scripts:
        // uri is the name of the variable used as parameter; the beans are exposed as
        // properties
        // note the starts-with operator =^
        // note that uri is also used in the back-quoted string that performs variable
        // interpolation
        JexlScript mapper = jexl.createScript("uri.scheme =^ 'http'? `https://${uri.host}` : null", "uri");
        // using the bang-bang / !! - JScript like - is the way to coerce to boolean in
        // the filter
        JexlScript transform = jexl
            .createScript("list.stream().map(mapper).filter(x -> !!x).collect(Collectors.toList())", "list");

        // Execute scripts:
        JexlContext sctxt = new StreamContext();
        // expose the static methods of Collectors; java.util.* is allowed by
        // permissions
        sctxt.set("Collectors", Collectors.class);
        // expose the mapper script as a global variable in the context
        sctxt.set("mapper", mapper);

        Object transformed = transform.execute(sctxt, uris);
        Assertions.assertTrue(transformed instanceof List<?>);
        Assertions.assertEquals(control, transformed);

    }

    /**
     * A MapContext that can operate on streams.
     */
    public static class StreamContext extends MapContext {
        /**
         * This allows using a JEXL lambda as a mapper.
         *
         * @param stream the stream
         * @param mapper the lambda to use as mapper
         * @return the mapped stream
         */
        public Stream<?> map(Stream<?> stream, final JexlScript mapper) {
            return stream.map(x -> mapper.execute(this, x));
        }

        /**
         * This allows using a JEXL lambda as a filter.
         * @param stream the stream
         * @param filter the lambda to use as filter
         * @return the filtered stream
         */
//        public Stream<?> filter(Stream<?> stream, final JexlScript filter) {
//            return stream.filter(x -> x !=null && Boolean.TRUE.equals(filter.execute(this, x)));
//        }
    }

    @Test
    public void demo3() {
        // 创建表达式引擎对象
        JexlEngine engine = new JexlBuilder().create();
        // 创建表达式语句
        String expressionStr = "money > 5000";
        // 创建Context对象，为表达式中的未知数赋值
        JexlContext context = new MapContext();
        context.set("money", "10000");
        // 使用表达式引擎创建表达式对象
        JexlExpression expression = engine.createExpression(expressionStr);
        // 使用表达式对象计算
        Object evaluate = expression.evaluate(context);
        // 输出结果：true
        System.out.println(evaluate);

        // 判断提交时间是否大于某一个时间点
//        String expressionStr = "submitTime.getTime() >= 1583856000000";
//        context.set("submitTime", new Date());
//        // 判断字符串是否包含“成功”
//        String expressionStr = "text.contains('成功')";
//        context.set("text", "请求成功");
//        // 判断字符串是否为空
//        String expressionStr = "text eq null || text.size() == 0"
//        // 判断是否属于数组中的任意一个
//        String expressionStr = "text =~ ['请求成功','啦啦','吧啦吧啦']"
//        // 判断是否不属于数组中的任意一个
//        String expressionStr = "text !~ ['请求成功','啦啦','吧啦吧啦']"
//        // 表达式为逻辑语句，运算结果为：2
//        String expressionStr = "if(a>b){c=a;}else{c=b};";
//        context.set("a", 1);
//        context.set("b", 2);
//        // 表达式为对象调用方法
//        String expressionStr = "person.getName()";
//        Person person = new Person();
//        person.setName("Sixj");
//        context.set("person", person);
    }

}
