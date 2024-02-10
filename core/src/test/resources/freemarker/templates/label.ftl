[#ftl]

[#assign x = true]
[#assign ambigousMsg = "Multiple compatible overloaded"]
[#if x]
[=1][=2]${y}
${ambigousMsg}
[/#if]
