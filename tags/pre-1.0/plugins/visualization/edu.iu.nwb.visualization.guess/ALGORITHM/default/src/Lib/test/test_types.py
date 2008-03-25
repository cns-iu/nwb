# Python test set -- part 6, built-in types

from test_support import *

print_test('Built-in types (test_types.py)', 1)

print_test('Truth value testing', 2)
assert not None
assert not 0
assert not 0L
assert not 0.0
assert not ''
assert not ()
assert not []
assert not {}
assert 1
assert 1L
assert 1.0
assert 'x'
assert (1, 1)
assert [1]
assert {'x': 1}

def f(): pass
class C: pass
import sys
x = C()
assert f
assert C
assert sys
assert x

print_test('Boolean operations')

assert not 0 or 0
assert 1 and 1
assert not not 1

print_test('Comparisons')

if 0 < 1 <= 1 == 1 >= 1 > 0 <> 1: pass
else: raise TestFailed, 'int comparisons failed'
if 0L < 1L <= 1L == 1L >= 1L > 0L <> 1L: pass
else: raise TestFailed, 'long int comparisons failed'
if 0.0 < 1.0 <= 1.0 == 1.0 >= 1.0 > 0.0 <> 1.0: pass
else: raise TestFailed, 'float comparisons failed'
if '' < 'a' <= 'a' == 'a' < 'abc' < 'abd' < 'b': pass
else: raise TestFailed, 'string comparisons failed'
if 0 in [0] and 0 not in [1]: pass
else: raise TestFailed, 'membership test failed'
if None is None and [] is not []: pass
else: raise TestFailed, 'identity test failed'

print_test('Numeric types')
print_test('integers', 3)
if 12 + 24 <> 36: raise TestFailed, 'int op'
if 12 + (-24) <> -12: raise TestFailed, 'int op'
if (-12) + 24 <> 12: raise TestFailed, 'int op'
if (-12) + (-24) <> -36: raise TestFailed, 'int op'
if not 12 < 24: raise TestFailed, 'int op'
if not -24 < -12: raise TestFailed, 'int op'
# Test for a particular bug in integer multiply
xsize, ysize, zsize = 238, 356, 4
if not (xsize*ysize*zsize == zsize*xsize*ysize == 338912):
	raise TestFailed, 'int mul commutativity'
	
print_test('long integers')
if 12L + 24L <> 36L: raise TestFailed, 'long op'
if 12L + (-24L) <> -12L: raise TestFailed, 'long op'
if (-12L) + 24L <> 12L: raise TestFailed, 'long op'
if (-12L) + (-24L) <> -36L: raise TestFailed, 'long op'
if not 12L < 24L: raise TestFailed, 'long op'
if not -24L < -12L: raise TestFailed, 'long op'

print_test('floats')
if 12.0 + 24.0 <> 36.0: raise TestFailed, 'float op'
if 12.0 + (-24.0) <> -12.0: raise TestFailed, 'float op'
if (-12.0) + 24.0 <> 12.0: raise TestFailed, 'float op'
if (-12.0) + (-24.0) <> -36.0: raise TestFailed, 'float op'
if not 12.0 < 24.0: raise TestFailed, 'float op'
if not -24.0 < -12.0: raise TestFailed, 'float op'

print_test('conversions')
if 0 <> 0L or 0 <> 0.0 or 0L <> 0.0: raise TestFailed, 'mixed comparisons'
if 1 <> 1L or 1 <> 1.0 or 1L <> 1.0: raise TestFailed, 'mixed comparisons'
if -1 <> -1L or -1 <> -1.0 or -1L <> -1.0:
	raise TestFailed, 'int/long/float value not equal'
if int(1.9) == 1 == int(1.1) and int(-1.1) == -1 == int(-1.9): pass
else: raise TestFailed, 'int() does not round properly'
if long(1.9) == 1L == long(1.1) and long(-1.1) == -1L == long(-1.9): pass
else: raise TestFailed, 'long() does not round properly'
if float(1) == 1.0 and float(-1) == -1.0 and float(0) == 0.0: pass
else: raise TestFailed, 'float() does not work properly'

print_test('Sequence types', 2)

print_test('Strings', 3)
if len('') <> 0: raise TestFailed, 'len(\'\')'
if len('a') <> 1: raise TestFailed, 'len(\'a\')'
if len('abcdef') <> 6: raise TestFailed, 'len(\'abcdef\')'
if 'xyz' + 'abcde' <> 'xyzabcde': raise TestFailed, 'string concatenation'
if 'xyz'*3 <> 'xyzxyzxyz': raise TestFailed, 'string repetition *3'
if 0*'abcde' <> '': raise TestFailed, 'string repetition 0*'
if min('abc') <> 'a' or max('abc') <> 'c': raise TestFailed, 'min/max string'
if 'a' in 'abc' and 'b' in 'abc' and 'c' in 'abc' and 'd' not in 'abc': pass
else: raise TestFailed, 'in/not in string'
x = 'x'*103
if '%s!'%x != x+'!': raise TestFailed, 'nasty string formatting bug'

print_test('Tuples')
if len(()) <> 0: raise TestFailed, 'len(())'
if len((1,)) <> 1: raise TestFailed, 'len((1,))'
if len((1,2,3,4,5,6)) <> 6: raise TestFailed, 'len((1,2,3,4,5,6))'
if (1,2)+(3,4) <> (1,2,3,4): raise TestFailed, 'tuple concatenation'
if (1,2)*3 <> (1,2,1,2,1,2): raise TestFailed, 'tuple repetition *3'
if 0*(1,2,3) <> (): raise TestFailed, 'tuple repetition 0*'
if min((1,2)) <> 1 or max((1,2)) <> 2: raise TestFailed, 'min/max tuple'
if 0 in (0,1,2) and 1 in (0,1,2) and 2 in (0,1,2) and 3 not in (0,1,2): pass
else: raise TestFailed, 'in/not in tuple'

print_test('Lists')
if len([]) <> 0: raise TestFailed, 'len([])'
if len([1,]) <> 1: raise TestFailed, 'len([1,])'
if len([1,2,3,4,5,6]) <> 6: raise TestFailed, 'len([1,2,3,4,5,6])'
if [1,2]+[3,4] <> [1,2,3,4]: raise TestFailed, 'list concatenation'
if [1,2]*3 <> [1,2,1,2,1,2]: raise TestFailed, 'list repetition *3'
if 0*[1,2,3] <> []: raise TestFailed, 'list repetition 0*'
if min([1,2]) <> 1 or max([1,2]) <> 2: raise TestFailed, 'min/max list'
if 0 in [0,1,2] and 1 in [0,1,2] and 2 in [0,1,2] and 3 not in [0,1,2]: pass
else: raise TestFailed, 'in/not in list'

print_test('Additional list operations', 4)
a = [0,1,2,3,4]
a[0] = 5
a[1] = 6
a[2] = 7
if a <> [5,6,7,3,4]: raise TestFailed, 'list item assignment [0], [1], [2]'
a[-2] = 8
a[-1] = 9
if a <> [5,6,7,8,9]: raise TestFailed, 'list item assignment [-2], [-1]'
a[:2] = [0,4]
a[-3:] = []
a[1:1] = [1,2,3]
if a <> [0,1,2,3,4]: raise TestFailed, 'list slice assignment'
del a[1:4]
if a <> [0,4]: raise TestFailed, 'list slice deletion'
del a[0]
if a <> [4]: raise TestFailed, 'list item deletion [0]'
del a[-1]
if a <> []: raise TestFailed, 'list item deletion [-1]'
a.append(0)
a.append(1)
a.append(2)
if a <> [0,1,2]: raise TestFailed, 'list append'
a.insert(0, -2)
a.insert(1, -1)
a.insert(2,0)
if a <> [-2,-1,0,0,1,2]: raise TestFailed, 'list insert'
if a.count(0) <> 2: raise TestFailed, ' list count'
if a.index(0) <> 2: raise TestFailed, 'list index'
a.remove(0)
if a <> [-2,-1,0,1,2]: raise TestFailed, 'list remove'
a.reverse()
if a <> [2,1,0,-1,-2]: raise TestFailed, 'list reverse'
a.sort()
if a <> [-2,-1,0,1,2]: raise TestFailed, 'list sort'
def revcmp(a, b): return cmp(b, a)
a.sort(revcmp)
if a <> [2,1,0,-1,-2]: raise TestFailed, 'list sort with cmp func'
# The following dumps core in unpatched Python 1.5:
def myComparison(x,y):
    return cmp(x%3, y%7)
z = range(12)
z.sort(myComparison)

print_test('Mappings and Dictionaries', 2)
d = {}
if d.keys() <> []: raise TestFailed, '{}.keys()'
if d.has_key('a') <> 0: raise TestFailed, '{}.has_key(\'a\')'
if len(d) <> 0: raise TestFailed, 'len({})'
d = {'a': 1, 'b': 2}
if len(d) <> 2: raise TestFailed, 'len(dict)'
k = d.keys()
k.sort()
if k <> ['a', 'b']: raise TestFailed, 'dict keys()'
if d.has_key('a') and d.has_key('b') and not d.has_key('c'): pass
else: raise TestFailed, 'dict keys()'
if d['a'] <> 1 or d['b'] <> 2: raise TestFailed, 'dict item'
d['c'] = 3
d['a'] = 4
if d['c'] <> 3 or d['a'] <> 4: raise TestFailed, 'dict item assignment'
del d['b']
if d <> {'a': 4, 'c': 3}: raise TestFailed, 'dict item deletion'
d = {1:1, 2:2, 3:3}
d.clear()
if d != {}: raise TestFailed, 'dict clear'
d.update({1:100})
d.update({2:20})
d.update({1:1, 2:2, 3:3})
if d != {1:1, 2:2, 3:3}: raise TestFailed, 'dict update'
if d.copy() != {1:1, 2:2, 3:3}: raise TestFailed, 'dict copy'
if {}.copy() != {}: raise TestFailed, 'empty dict copy'
# dict.get()
d = {}
if d.get('c') != None: raise TestFailed, 'missing {} get, no 2nd arg'
if d.get('c', 3) != 3: raise TestFailed, 'missing {} get, w/ 2nd arg'
d = {'a' : 1, 'b' : 2}
if d.get('c') != None: raise TestFailed, 'missing dict get, no 2nd arg'
if d.get('c', 3) != 3: raise TestFailed, 'missing dict get, w/ 2nd arg'
if d.get('a') != 1: raise TestFailed, 'present dict get, no 2nd arg'
if d.get('a', 3) != 1: raise TestFailed, 'present dict get, w/ 2nd arg'
