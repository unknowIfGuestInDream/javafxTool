<#list bindingSelContent as item>
${offset}<tagBinding id="Trigger${item.factor}" key="Trigger_Source" value="${item.macro}">
${offset}    <and failOnChildMasked="true">
${offset}        <simpleCondition optionId="requestSource" valueId="HWRequestGrp${item.groupNum}">
${offset}        </simpleCondition>
${offset}        <simpleCondition optionId="triggerSourceGrp${item.groupNum}" valueId="${item.factor}">
${offset}        </simpleCondition>
${offset}    </and>
${offset}</tagBinding>
</#list>

<#assign x = true>
<#assign seq=[1,2,3,5,6]>
${groups[1].groupNum}
${seq[1]}
<#list seq as s>
     <#assign x = true>
     ${(s?index/16)?int}
     ${s?index%16}
<#if s == 2>
     <#assign x = false>
</#if>
${x?c}
</#list>
