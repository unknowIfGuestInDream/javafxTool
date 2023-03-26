<#list bindingContent as item>
${offset}<tagBinding id="Trigger${item.factor}" key="Trigger_Source" value="${item.macro}">
${offset}    <and failOnChildMasked="true">
${offset}        <simpleCondition optionId="requestSource" valueId="HWRequestGrp${item.groupNum}">
${offset}        </simpleCondition>
${offset}        <simpleCondition optionId="triggerSourceGrp${item.groupNum}" valueId="${item.factor}">
${offset}        </simpleCondition>
${offset}    </and>
${offset}</tagBinding>
</#list>
