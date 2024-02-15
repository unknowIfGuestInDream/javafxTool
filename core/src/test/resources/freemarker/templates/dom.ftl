${root.Setting.general.programType.@value}
${root.Setting.section[0].@id}
${root.Setting.section?size}

<#list root.Setting.section as ch>
${ch.@id}
</#list>

<#assign daliConfig = root.Setting.section[0]>
${daliConfig.option[0].@id}
${daliConfig.option[0].@@next_sibling_element.@id}
${daliConfig.option[0].item[0]?nextSibling}
${daliConfig.option[0].item[0]?nextSibling?trim}
${daliConfig.option[2].@@previous_sibling_element.@id}
${daliConfig.option[2].item[0]?previousSibling?trim}
${daliConfig.option?? ?c}
${daliConfig.option[0].item[0]?parent.@id}
${daliConfig.option[0]?nodeType}
${daliConfig.option[0]?nodeName}
${daliConfig.option[0]?nodeNamespace}
<#list daliConfig.option[0]?children as chi>
${chi.@@qname}
</#list>

${daliConfig.option[0].item[0]['following-sibling::item'].@id}
${daliConfig.option[0]['item']?size}
${daliConfig.option[0]['item'][0].@id}
${daliConfig.option[0].@@attributes_markup}
${daliConfig.option[0].@@start_tag}

markup
${root.@@markup}

