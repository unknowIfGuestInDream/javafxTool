<#include "./head.ftl">

<#assign linkman="鲁迅">
联系人: ${linkman}

<#assign mobile="15544447777" address="北京">
手机号: ${mobile}
地址: ${address}

Hello ${message}
大写字母  ${message?upperCase}
第一个字母转换为大写  ${message?capFirst}
字符长度  ${message?length}
<#assign message="局部变量">
Hello ${message}
Hello ${.globals.message}

<#assign users = [{"name":"Joe","hidden":false},
{"name":"James Bond", "hidden":true},
{"name":"Julia",      "hidden":false}]>
List of users:
数组size ${users?size}
<#list users as user>
索引 ${user?index}
计数 ${user?counter}
奇偶数 ${user?itemParity}
是奇数 ${user?isOddItem?c}
是偶数 ${user?isEvenItem?c}
是否隐藏 ${user.hidden?string("Y","N")}
item_cycle ${user?itemCycle("Odd","Even")}
    <#if !user.hidden>
- ${user.name}
    </#if>
</#list>
That's all.
数组filter
<#list users?filter(it -> it.hidden) as user>
    ${user.name}
</#list>
join  ${users?map(u->u.name)?join(", ")}

${"'"}
年份
${.now?string('yyyy')}
${.now?isoUtc}
${.now?isoLocal}
${.dataModel?isHash?c}
${.localeObject.toString()}
${.timeZone}
${.templateName}
${.outputFormat}
${.currentNode!'-'}
${.version}

getOptionalTemplate:
<#assign t = .getOptionalTemplate('missing.ftl')>
Exists: ${t.exists?c};
Include: ${t.include???c};
Import: ${t.import???c};
<#assign t = .getOptionalTemplate('head.ftl')>
Exists: ${t.exists?c};
Include: ${t.include???c};
Import: ${t.import???c};
<#assign t = .getOptionalTemplate('test2.ftl')>
Exists: ${t.exists?c};
Include: ${t.include???c};
Import: ${t.import???c};

callerTemplateName
<#macro m><#nested>${.callerTemplateName}</#macro>
<@m></@>

absoluteTemplateName 将模板名称转换为绝对名称
${'head.ftl'?absoluteTemplateName}

withArgs
行末填充${""}来让freemarkert添加换行符
<#macro m a b c>${a}, ${b}, ${c}</#macro>
<@m 1 2 3 />${""}
<@m?withArgs([1, 2, 3]) />
<@m?withArgs([1, 2]) 3 />
comment
<#-- 这是一段注释 -->
<!-- 这是可显示的注释 -->
${missing!'default'}
<#if mouse??>
  Mouse found
<#else>
  No mouse found
</#if>

字符串切片
<#assign s = "ABCDEF">
${s[2..3]}
${s[2..<4]}
${s[2..*3]}
${s[2..*100]}
${s[2..]}

数组串联
<#list ["Joe", "Fred"] + ["Julia", "Kate"] as user>
- ${user}
</#list>

数组切片
<#assign seq = ["A", "B", "C", "D", "E"]>
<#list seq[1..3] as i>${i}</#list>

计算
<#assign x = 5>
${100 - x * x}
${x / 2}
${12 % 10}
${(x/2)?int}
${1.1?int}
${1.999?int}
${-1.1?int}
${-1.999?int}
先将操作数截断为整数，然后 然后返回除法的其余部分：%
${12 % 5}   <#-- Prints 2 -->
${12.9 % 5} <#-- Prints 2 -->
${12.1 % 5} <#-- Prints 2 -->
${12 % 6}   <#-- Prints 0 -->
${12 % 6.9} <#-- Prints 0 -->

<#if true || true>
  ||
</#if>
<#if true && true>
  &&
</#if>
<#if !false>
  !
</#if>
<#if true &amp;&amp; true>
  &amp;&amp;
</#if>
<#if true \and true>
  \and
</#if>


