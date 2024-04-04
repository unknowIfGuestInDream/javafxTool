${book}
${book.id}
${book.name}
${book.pages}
${book.flag?c}
${book.ignore!''}

${category}
${category.name}
${category.book}
${category.book.id}
${category.book.name}

${category.books?size}
<#list category.books as bo>
    ${bo.name}
</#list>
