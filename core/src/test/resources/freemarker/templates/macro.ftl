<#macro copyright_for_c>
  <p>Copyright (C) ${.now?string('yyyy')} Someone. All rights reserved.</p>
</#macro>

<#macro greet person>
  <span style="font-size: 144%; ">Hello ${person}!</span>
</#macro>

嵌套内容
<#macro border>
  <table border=4 cellspacing=0 cellpadding=4><tr><td>
    <#nested>
  </td></tr></table>
</#macro>

<#macro do_thrice>
  <#nested>
  <#nested>
  <#nested>
</#macro>

<#macro repeat count>
  <#local y = "test">
  <#list 1..count as x>
    ${y} ${count}/${x}: <#nested>
  </#list>
</#macro>

<#macro do_thriceFor>
  <#nested 1>
  <#nested 2>
  <#nested 3>
</#macro>

<#macro repeatFor count>
  <#list 1..count as x>
    <#nested x, x/2, x==count>
  </#list>
</#macro>

<#assign mail="macro.ftl">
