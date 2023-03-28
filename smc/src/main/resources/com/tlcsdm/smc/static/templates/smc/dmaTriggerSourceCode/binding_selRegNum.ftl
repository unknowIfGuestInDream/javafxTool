<#list bindingRegNumContent as item>
<#if item.condition?? && item.condition?size gt 0>
${offset}<tagBinding id="SELRegNum${item.channelNum}" key="SELRegNum" value="${item.channelNum}">
${offset}    <or>
<#list item.condition as meta>
${offset}        <and failOnChildMasked="true">
${offset}            <simpleCondition optionId="requestSource" valueId="HWRequestGrp${meta.groupNum}">
${offset}            </simpleCondition>
${offset}            <simpleCondition optionId="triggerSourceGrp${meta.groupNum}" valueId="${meta.factor}">
${offset}            </simpleCondition>
${offset}        </and>
</#list>
${offset}    </or>
${offset}</tagBinding>
</#if>
</#list>
