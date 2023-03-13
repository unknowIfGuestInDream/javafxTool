<#list cgdmaContent! as item>
#define ${item.macro}${item.offset}(${item.hex}) /* DMAC group ${item.groupNum} ${item.factor} */
</#list>
