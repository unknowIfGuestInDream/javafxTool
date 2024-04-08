<#function f(a, b, c='d3')>
<#return 'a=${a}; b=${b}; c=${c}'>
</#function>

<#function fCA(a, b, o...)>
<#local r>
<#compress>
a=${a}; b=${b}; o=<#if o?isSequence>[<#list o as v>${v!'null'}<#sep>, </#list>]
<#else>
{<#list o as k,v>${k}=${v!'null'}<#sep>, </#list>}
</#if>
</#compress>
</#local>
<#return r>
</#function>
