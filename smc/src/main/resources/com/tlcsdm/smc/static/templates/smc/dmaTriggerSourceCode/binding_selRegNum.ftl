<#list bindingRegNumContent as item>
<#if item.condition?? && item.condition?size gt 0>
<tagBinding id="SELRegNum${item.channelNum}" key="SELRegNum" value="${item.channelNum}">
    <or>
<#list item.condition as meta>
        <and failOnChildMasked="true">
            <simpleCondition optionId="requestSource" valueId="HWRequestGrp${meta.groupNum}">
            </simpleCondition>
            <or>
                <simpleCondition optionId="triggerSourceGrp${meta.groupNum}" valueId="${meta.factor}">
                </simpleCondition>
            </or>
        </and>
</#list>
    </or>
</tagBinding>
</#if>
</#list>
