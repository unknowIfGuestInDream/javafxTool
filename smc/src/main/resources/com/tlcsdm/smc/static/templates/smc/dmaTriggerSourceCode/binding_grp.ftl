<#list bindingSelContent as item>
${offset}<tagBinding id="Trigger${item.factor}" key="Trigger_Source" value="${item.macro}">
${offset}    <and>
${offset}        <simpleCondition optionId="requestSource" valueId="HWRequestGrp${item.groupNum}">
${offset}        </simpleCondition>
${offset}        <simpleCondition optionId="triggerSourceGrp${item.groupNum}" valueId="${item.factor}">
${offset}        </simpleCondition>
${offset}    </and>
${offset}</tagBinding>
</#list>

 <#assign seq=[1,2,3,5,6]>
<#list seq as s>
     ${(s_index/16)?int}
     ${s_index%16}
 </#list>
