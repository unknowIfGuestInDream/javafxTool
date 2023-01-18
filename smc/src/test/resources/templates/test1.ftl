<#include "./head.ftl">

<#assign linkman="鲁迅">
联系人: ${linkman}

<#assign mobile="15544447777" address="北京">
手机号: ${mobile}
地址: ${address}

Hello ${message}

<#assign users = [{"name":"Joe",        "hidden":false},
                  {"name":"James Bond", "hidden":true},
                  {"name":"Julia",      "hidden":false}]>
List of users:
<#list users as user>
  <#if !user.hidden>
  - ${user.name}
  </#if>
</#list>
That's all.