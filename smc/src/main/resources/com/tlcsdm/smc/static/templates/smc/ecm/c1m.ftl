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
        <ErrorSource errorSourceId="${item.errorSourceId!''}" categoryId="${item.categoryId!''}" errorSourceNumber="${item.errorSourceNumber!''}" errorSourceEnName="${item.errorSourceEnName!''}" errorSourceJpName="${item.errorSourceJpName!''}">
            <Function>
<#list item.function as oper>
                <Operation funcId="${oper.funcId}" support="${oper.support}"<#if oper.errorNote?? && oper.errorNote != ""> errorNote="${oper.errorNote}"</#if>/>
</#list>
            </Function>
        </ErrorSource>
</#list>
    </ErrorSourceInfos>
</ECMInfos>
