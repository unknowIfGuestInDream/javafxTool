def colors = [red: '#FF0000', green: '#00FF00', blue: '#0000FF']

assert colors['red'] == '#FF0000'
assert colors.green  == '#00FF00'

colors['pink'] = '#FF00FF'
colors.yellow  = '#FFFF00'

assert colors.pink == '#FF00FF'
assert colors['yellow'] == '#FFFF00'

assert colors instanceof java.util.LinkedHashMap

// 如果您尝试访问映射中不存在的密钥
assert colors.unknown == null

def emptyMap = [:]
assert emptyMap.anyKey == null

// 在上面的示例中，我们使用了字符串键，但您也可以使用其他类型的值作为键
def numbers = [1: 'one', 2: 'two']

assert numbers[1] == 'one'

//在这里，我们使用数字作为键，因为数字可以明确地识别为数字， 所以 Groovy 不会像我们之前的例子那样创建字符串键。 
//但是考虑一下你想传递一个变量来代替键的情况，以使该变量的值成为键：
def key = 'name'
def person = [key: 'Guillaume']

assert !person.containsKey('name')
assert person.containsKey('key')

//当您需要在映射定义中将变量值作为键传递时，必须用括号将变量或表达式括起来：
person = [(key): 'Guillaume']

assert person.containsKey('name')
assert !person.containsKey('key')
