<#list bindingSelContent as item>
<#if item.condition?? && item.condition?size gt 0>
${offset}<tagBinding id="SEL${item.channelNum}_Grp${item.groupNum}" key="SEL${item.channelNum}_Setting" value="_DMAC_REQUEST_16M_${item.channelNum}_GRP${item.groupNum}">
${offset}     <and failOnChildMasked="true">
${offset}          <simpleCondition optionId="requestSource" valueId="HWRequestGrp${item.groupNum}">
${offset}          </simpleCondition>
${offset}         <or>
<#list item.condition as valueId>
${offset}               <simpleCondition optionId="triggerSourceGrp${item.groupNum}" valueId="${valueId}">
${offset}              </simpleCondition>
</#list>
${offset}         </or>
${offset}    </and>
${offset}</tagBinding>
</#if>
</#list>
