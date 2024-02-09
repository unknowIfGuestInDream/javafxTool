<#ftl output_format="HTML">
<#-- Let's assume we have "HTML" output format by default. -->
${"'"}  <#-- Prints: &#39; -->
<#outputformat "XML">
  ${"'"}  <#-- Prints: &apos; -->
</#outputformat>
${"'"}  <#-- Prints: &#39; -->

${'<b>test</b>'}  <#-- prints: &lt;b&gt;test&lt;/b&gt; -->
${'<b>test</b>'?no_esc}  <#-- prints: <b>test</b> -->


${'&'}  <#-- prints: &amp; -->
<#noautoesc>
  ${'&'}  <#-- prints: & -->
  ...
  ${'&'}  <#-- prints: & -->
  强制转义
  ${'&'?esc}  <#-- prints: &amp; -->
</#noautoesc>
${'&'}  <#-- prints: &amp; -->

<#-- <#ftl autoesc=false> -->
${'&'}  <#-- prints: & -->
<#autoesc>
  ${'&'}  <#-- prints: &amp; -->
  ...
  ${'&'}  <#-- prints: &amp; -->
</#autoesc>
${'&'}  <#-- prints: & -->
