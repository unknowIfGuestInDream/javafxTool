a = [ 45, 3, 19, 8 ]
b = [ 'sam', 'max', 56, 98.9, 3, 10, 'jill' ]
print (a + b).join(' '), "\n"
print a[2], " ", b[4], " ", b[-2], "\n"
print a.sort.join(' '), "\n"
a << 57 << 9 << 'phil'
print "A: ", a.join(' '), "\n"

b << 'alex' << 48 << 220
print "B: ", b.join(' '), "\n"
print "pop: ", b.pop, "\n"
print "shift: ", b.shift, "\n"
print "C: ", b.join(' '), "\n"

b.delete_at(2)
b.delete('alex')
print "D: ", b.join(' '), "\n"
