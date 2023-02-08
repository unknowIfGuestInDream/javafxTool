<#assign num=18>
<#if num lt 11><#-- 条件指令 -->
    ${num}  小于 11
    <#elseif num gt 14>
    ${num} 大于14
    <#else>
    ${num} 在11和14 之间
</#if>
 <#-- switch 指令 -->
<#switch num>
    <#case 13>
        ${num}  等于 13
        <#break>
    <#case 14>
        ${num}  等于 14
        <#break>
    <#default>
        what you say?
</#switch>
 <#-- 循环指令 -->
 <#assign seq=[1,2,3,5,6]>
 <#list seq as s>
     ${s_index},${s}<#-- _index 为隐藏变量 -->
     ${s_has_next?c}<#-- _has_next 也是一个隐藏变量 -->
     <#if s_has_next >;</#if>
     <#if s==7>
         <#break><#-- 跳出循环 -->
     </#if>
 </#list>
 <#-- 包含指令：可以在当前模板中插入其他内容 -->
 <#include "head.ftl">
 <#-- 不转义 标签中的内容 -->
 <#noparse>
     ${aa}
 </#noparse>
 <#-- 压缩 去除空格和换行-->
 <#assign str="   aaa aa

">
 str=  ${str}
 <#compress>
str=  ${str}
 </#compress>
------------------
 <#-- 自定义指令  并调用-->
 