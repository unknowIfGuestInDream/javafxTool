/ 取子节点
. 选取当前节点
.. 选取当前节点的父节点
@ 选取属性
<#list root["Setting/section"] as p>
  ${p.@id}
</#list>

filter
<#list root["Setting/section[@id='daliConfig']/option"] as p>
  ${p.@id}
</#list>

<#--
选取未知节点
*	匹配任何元素节点。
@*	匹配任何属性节点。
node()	匹配任何类型的节点。

/bookstore/*	选取 bookstore 元素的所有子元素。
//*	选取文档中的所有元素。
//title[@*]	选取所有带有属性的 title 元素。
-->
@
${root["Setting/section[last()]/@id"]}
..
${root["Setting/section[last()]/../section[last()-1]/@id"]}

last
${root["Setting/section[last()]"].@id}

count
${root["count(//section)"]}

<#--
选取若干路径
//book/title | //book/price	选取 book 元素的所有 title 和 price 元素。
//title | //price	选取文档中的所有 title 和 price 元素。
/bookstore/book/title | //price	选取属于 bookstore 元素的 book 元素的所有 title 元素，以及文档中所有的 price 元素。
-->

and
<#list root["//option[@id='enablePushButtons' and @selection='uncheck']"] as p>
  ${p.@id}
</#list>

<#--
ancestor	选取当前节点的所有先辈（父、祖父等）。
ancestor-or-self	选取当前节点的所有先辈（父、祖父等）以及当前节点本身。
attribute	选取当前节点的所有属性。
child	选取当前节点的所有子元素。
descendant	选取当前节点的所有后代元素（子、孙等）。
descendant-or-self	选取当前节点的所有后代元素（子、孙等）以及当前节点本身。
following	选取文档中当前节点的结束标签之后的所有节点。
following-sibling	选取当前节点之后的所有兄弟节点
namespace	选取当前节点的所有命名空间节点。
parent	选取当前节点的父节点。
preceding	选取文档中当前节点的开始标签之前的所有节点。
preceding-sibling	选取当前节点之前的所有同级节点。
self	选取当前节点
-->
descendant
<#list root["Setting/section[last()]/descendant::*"] as p>
  ${p.@id}
</#list>

function
<#-- https://www.w3school.com.cn/xpath/xpath_functions.asp -->
