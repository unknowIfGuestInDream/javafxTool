<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE xml>
<ECMInfos>
    <CategoryInfos>
<#list categoryInfos as item>
        <Category categoryId="${item.categoryId!''}" categoryEnName="${item.categoryEnName!''}" categoryJpName="${item.categoryJpName!''}"/>
</#list>
    </CategoryInfos>
    <ErrorSourceInfos>
<#list errorSourceInfos as item>
        <ErrorSource errorSourceId="${item.errorSourceId!''}" categoryId="${item.categoryId!''}" errorSourceNumber="${item.errorSourceNumber!''}" errorSourceEnName="${item.errorSourceEnName!''}<#if item.errorSourceDesc?length gt 0>(${item.errorSourceDesc})</#if>" errorSourceJpName="${item.errorSourceJpName!''}">
            <Function>
<#list item.function as oper>
                <Operation funcId="${oper.funcId}" support="${oper.support}"<#if oper.errorNote?? && oper.errorNote != ""> errorNote="${oper.errorNote}"</#if>/>
</#list>
            </Function>
            <Tags>
<#list item.tag as t>
                <Tag key="${t.key}" value="${t.value}"/>
</#list>
            </Tags>
        </ErrorSource>
</#list>
    </ErrorSourceInfos>
</ECMInfos>
