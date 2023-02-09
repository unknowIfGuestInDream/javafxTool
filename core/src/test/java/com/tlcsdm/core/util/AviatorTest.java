package com.tlcsdm.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

/**
 * 规则引擎aviator
 */
public class AviatorTest {

    @Test
    public void add() {
        Long result = (Long) AviatorEvaluator.execute("1+2+3");
        assertEquals(6, result);
    }

    /**
     * 逻辑表达式
     */
    @Test
    public void expression() {
        Boolean result = (Boolean) AviatorEvaluator.execute("3 > 1 && 2 != 4 || true");
        assertEquals(true, result);
        // 三元表达式
        String ternary = (String) AviatorEvaluator.execute("3>0? 'yes':'no'");
        assertEquals("yes", ternary);
    }

    @Test
    public void option() {
        AviatorEvaluator.setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
        AviatorEvaluator.setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
        assertEquals(1.5D, ((BigDecimal) AviatorEvaluator.execute("3/2")).doubleValue());

        // 两种运行模式
        // 默认 AviatorEvaluator 以执行速度优先:
        AviatorEvaluator.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.EVAL);
        // 你可以修改为编译速度优先,这样不会做编译优化:
        AviatorEvaluator.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.COMPILE);
    }

    /**
     * execute()，需要传递Map格式参数
     */
    @Test
    public void execute() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", "Bob");
        assertEquals("hello: Bob!", AviatorEvaluator.execute("'hello: ' + username  + '!'", map));
    }

    /**
     * 内置函数
     */
    @Test
    public void func() {
        // sysdate() 获取当前时间Date
        // date_to_string(date,format) date转为String
        System.out.println(AviatorEvaluator.execute("date_to_string(sysdate(),'yyyy-MM-dd HH:mm:ss')"));
        // 求字符串长度,不能用String.length();
        System.out.println("string.length('hello') = " + AviatorEvaluator.execute("string.length('hello')"));
        // 判断字符串中是否包含某个字符串
        System.out.println(
                "string.contains('hello', 'h') = " + AviatorEvaluator.execute("string.contains('hello', 'h')"));
        System.out.println("math.pow(-3, 2) = " + AviatorEvaluator.execute("math.pow(-3, 2)"));
        System.out.println("math.sqrt(9.0) = " + AviatorEvaluator.execute("math.sqrt(9.0)"));
        System.out.println("max: " + AviatorEvaluator.execute("max(2,4,8)"));
        // System.out.println("scale: " +
        // AviatorEvaluator.execute("scale(15.344,0,2)"));
        // 向上取整
        // System.out.println("ceil: " + AviatorEvaluator.execute("ceil(196)"));
        // System.out.println("sum: " + AviatorEvaluator.execute("sum(2,4,5)"));
    }

    /**
     * 自定义函数
     */
    @Test
    public void function() {
        // 注册
        AviatorEvaluator.addFunction(new AddFunction());
        // 方式1
        System.out.println();
        assertEquals(9.93, AviatorEvaluator.execute("myAdd(12.23, -2.3)"));
        // 方式2
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("a", 12.23);
        params.put("b", -2.3);
        assertEquals(9.93, AviatorEvaluator.execute("myAdd(a, b)", params));
    }

    class AddFunction extends AbstractFunction {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
            double num1 = FunctionUtils.getNumberValue(arg1, env).doubleValue();
            double num2 = FunctionUtils.getNumberValue(arg2, env).doubleValue();
            return new AviatorDouble(num1 + num2);
        }

        public String getName() {
            return "myAdd";
        }
    }

    /**
     * 其实每次执行Aviator.execute()时，背后都经过了编译和执行的操作。
     * 那么我们可以先编译表达式，返回一个编译后的结果，然后传入不同的env来重复使用编译的结果，这样可以提高性能。
     * 
     * 编译表达式和未编译表达式性能测试
     * 
     * 编译后的结果可以自己缓存，也可以交给Aviator来帮你缓存，AviatorEvaluator内部又一个全局的缓存池，如果想通过Aviator来帮你缓存，可以通过如下方法：
     * public static Expression complie(String expression, boolean cached);
     */
    @Test
    public void compile() {
        String expression = "a * (b + c)";
        Map<String, Object> env = new HashMap<>();
        env.put("a", 3.32);
        env.put("b", 234);
        env.put("c", 324.2);
        // 编译表达式
        Expression compliedExp = AviatorEvaluator.compile(expression);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Double result = (Double) compliedExp.execute(env);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("预编译的耗时为：" + (endTime - startTime));
        long startTime2 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Double result = (Double) AviatorEvaluator.execute(expression, env);
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("无编译的耗时为：" + (endTime2 - startTime2));
    }

    /**
     * 访问数组和集合
     */
    @Test
    public void array() {
        final List<String> list = new ArrayList<String>();
        list.add("hello");
        list.add(" world");
        final int[] array = new int[3];
        array[0] = 0;
        array[1] = 1;
        array[2] = 3;
        final Map<String, Date> map = new HashMap<String, Date>();
        map.put("date", new Date());
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("list", list);
        env.put("array", array);
        env.put("mmap", map);
        assertEquals("hello world", AviatorEvaluator.execute("list[0]+list[1]", env));
        assertEquals("array[0]+array[1]+array[2]=4",
                AviatorEvaluator.execute("'array[0]+array[1]+array[2]=' + (array[0]+array[1]+array[2])", env));
        System.out.println(AviatorEvaluator.execute("'today is ' + mmap.date ", env));
    }

    /**
     * 正则表达式
     * 
     * Aviator 支持类 Ruby 和 Perl 风格的表达式匹配运算,通过=~操作符, 如下面这个例子匹配 email 并提取用户名返回:
     * email与正则表达式/([\w0-8]+@\w+[\.\w+]+)/通过=~操作符来匹配,结果为一个 Boolean 类 型, 
     * 因此可以用于三元表达式判断,匹配成功的时候返回$1,指代正则表达式的分组 1,也就是用户名,否则返回unknown。
     * 
     * Aviator 在表达式级别支持正则表达式,通过//括起来的字符序列构成一个正则表达式,正则表 达式可以用于匹配(作为=~的右操作数)、
     * 比较大小,匹配仅能与字符串进行匹配。匹配成功后, Aviator 会自动将匹配成功的分组放入$num的变量中,
     * 其中$0 指代整个匹配的字符串,而$1表示第一个分组,以此类推。
     * Aviator 的正则表达式规则跟 Java 完全一样,因为内部其实就是使用java.util.regex.Pattern做编译的。
     */
    @Test
    public void regex() {
        String email = "killme2008@gmail.com";
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("email", email);
        String username = (String) AviatorEvaluator.execute("email=~/([\\w0-8]+)@\\w+[\\.\\w+]+/ ? $1 : 'unknow' ",
                env);
        assertEquals("killme2008", username);
    }

    /**
     * Aviator 有个方便用户使用变量的语法糖, 当你要访问变量a中的某个属性b, 那么你可以通过a.b访问到, 
     * 更进一步, a.b.c将访问变量a的b属性中的c属性值, 推广开来也就是说 Aviator 可以将变量声明为嵌套访问的形式。
     * 
     * nil是 Aviator 内置的常量,类似 java 中的null,表示空的值。nil跟null不同的在于,在 java 中null只能使用在==、!=的比较运算符,
     * 而nil还可以使用>、>=、<、<=等比较运算符。 Aviator 规定,任何对象都比nil大除了nil本身。用户传入的变量如果为null,将自动以nil替代。
     */
    @Test
    public void nil() {
        assertTrue((Boolean) AviatorEvaluator.execute("nil == nil"));
        assertTrue((Boolean) AviatorEvaluator.execute(" 3> nil"));
        assertTrue((Boolean) AviatorEvaluator.execute(" true!= nil"));
        assertTrue((Boolean) AviatorEvaluator.execute(" ' '>nil "));
        assertTrue((Boolean) AviatorEvaluator.execute(" a==nil "));
    }

    /**
     * Aviator 并不支持日期类型,如果要比较日期,你需要将日期写字符串的形式,并且要求是形如 “yyyy-MM-dd HH:mm:ss:SS”的字符串,否则都将报错。
     * 字符串跟java.util.Date比较的时候将自动转换为Date对象进行比较:
     */
    @Test
    public void date() {
        Map<String, Object> env = new HashMap<String, Object>();
        final Date date = new Date();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS").format(date);
        env.put("date", date);
        env.put("dateStr", dateStr);
        assertTrue((Boolean) AviatorEvaluator.execute("date==dateStr", env));
        assertTrue((Boolean) AviatorEvaluator.execute("date > '2010-12-20 00:00:00:00' ", env));
        assertTrue((Boolean) AviatorEvaluator.execute("date < '2200-12-20 00:00:00:00' ", env));
        assertTrue((Boolean) AviatorEvaluator.execute("date==date ", env));
    }

    /**
     * aviator 拥有强大的操作集合和数组的 seq 库。整个库风格类似函数式编程中的高阶函数。在 aviator 中, 
     * 数组以及java.util.Collection下的子类都称为seq,可以直接利用 seq 库进行遍历、过滤和聚合等操作。
     * 
     * 求长度: count(list)
     * 求和: reduce(list,+,0), reduce函数接收三个参数,第一个是seq,第二个是聚合的函数,如+等,第三个是聚合的初始值
     * 过滤: filter(list,seq.gt(9)), 过滤出list中所有大于9的元素并返回集合; seq.gt函数用于生成一个谓词,表示大于某个值
     * 判断元素在不在集合里: include(list,10)
     * 排序: sort(list)
     * 遍历整个集合: map(list,println), map接受的第二个函数将作用于集合中的每个元素,这里简单地调用println打印每个元素
     */
    @Test
    public void seq() {
        Map<String, Object> env = new HashMap<String, Object>();
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(3);
        list.add(20);
        list.add(10);
        env.put("list", list);
        Object result = AviatorEvaluator.execute("count(list)", env);
        assertEquals(3L, result);
        result = AviatorEvaluator.execute("reduce(list,+,0)", env);
        assertEquals(33L, result);
        result = AviatorEvaluator.execute("filter(list,seq.gt(9))", env);
        assertEquals("[20, 10]", result.toString());
        assertTrue((Boolean) AviatorEvaluator.execute("include(list,10)", env));
        result = AviatorEvaluator.execute("sort(list)", env);
        assertEquals("[3, 10, 20]", result.toString());
    }
}
