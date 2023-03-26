<#list groups as group>
${offset}<option defaultSelection="${group.defaultSelection}" enabled="true" id="triggerSourceGrp${group.groupNum}" name="triggerSourceGrp${group.groupNum}">
<#list group.settingContent as item>
<#if item.hasCondition!false>
${offset}    <staticItem enabled="true" id="${item.factor}" name="${item.factor}">
${offset}        <complexCondition class="com.renesas.smc.tools.swcomponent.codegenerator.rh850.dma.ip2.ValidInChipStingCondition" parameter="${item.parameter}">
${offset}        </complexCondition>
${offset}    </staticItem>
<#else>
${offset}    <staticItem enabled="true" id="${item.factor}" name="${item.factor}"/>
</#if>
</#list>
${offset}    <simpleCondition optionId="requestSource" valueId="HWRequestGrp${group.groupNum}"></simpleCondition>
${offset}</option>
</#list>
