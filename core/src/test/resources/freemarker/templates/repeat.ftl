<#assign x = 2>
foo
<@repeat count=x>
    Test ${x}
    <#assign x++>
</@repeat>

<@repeat count=3 hr=true>
    Test
</@repeat>

<@repeat count=3; cnt>
    ${cnt}. Test
</@repeat>
