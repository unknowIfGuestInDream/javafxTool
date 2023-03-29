<#list cgdmaContent! as item>
<#if item.hasNote?? && item.hasNote>
/* sDMAC transfer request group ${item.groupNum} */
</#if>
#define ${item.macro}${item.offset}(${item.hex}) /* DMAC group ${item.groupNum} ${item.factor} */
</#list>
