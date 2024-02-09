${d?string.@epoch} ${d?string.@epoch}
<#setting locale='de_DE'>
${d?string.@epoch}

<#assign d = d?datetime>
${d} ${d?string.@epoch}
<#setting locale='de_DE'>
${d}

<#assign d = d?datetime>
${d} ${d?string.@htmlIso}
<#setting locale='de_DE'>
${d}

${d?string.@loc}
<#setting locale='en_US'>
${d?string.@loc}

${d?string.@loc}
<#setting timeZone='GMT+01:00'>
${d?string.@loc}

${dt}
${dt?string}
${dt?string.@div_100}

${.now?string.yyyy}

${.now?string.@fileDate}


