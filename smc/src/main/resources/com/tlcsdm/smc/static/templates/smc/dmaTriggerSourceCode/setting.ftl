<#list groups as group>
${offset}<option defaultSelection="${group.defaultSelection}" enabled="true" id="triggerSourceGrp${group.groupNum}" name="triggerSourceGrp${group.groupNum}">
<#list group.settingContent as item>
${offset}    <staticItem enabled="true" id="${item.factor}" name="${item.factor}"/>
</#list>
${offset}</option>
</#list>
