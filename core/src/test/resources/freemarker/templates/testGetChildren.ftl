<#assign children = mementoMethod("getChildren")>
Children IDs: <#list children as child>${child.id}<#if child?has_next>, </#if></#list>
