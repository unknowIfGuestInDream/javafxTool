<#assign upper="com.tlcsdm.core.freemarker.template.UpperDirective"?new()>

<@upper>
  upper 内<br>
  <#list ["red", "green", "blue"] as color>
    ${color}
  </#list>
</@upper>
