# Python test set -- part 1, grammar.
# This just tests whether the parser accepts them all.

from test_support import *

print_test('Parser (test_grammar.py)', 1)

print_test('Tokenizer', 2)

print_test('Backslashes', 3)

# Backslash means line continuation:
x = 1 \
+ 1
assert x == 2, 'backslash for line continuation'

# Backslash does not means continuation in comments :\
x = 0
assert x == 0, 'backslash ending comment'

print_test('Numeric literals', 3)

print_test('Plain integers', 4)
assert 0xff == 255, 'hex int'
assert 0377 == 255, 'octal int'
assert 2147483647 == 017777777777, 'large positive int'
try:
	from sys import maxint
except ImportError:
	maxint = 2147483647
if maxint == 2147483647:
	assert -2147483647-1 == 020000000000, 'max negative int'
	# XXX -2147483648
	assert 037777777777 == -1, 'oct -1'
	if 0xffffffff != -1: raise TestFailed, 'hex -1'
	for s in '2147483648', '040000000000', '0x100000000':
		try:
			x = eval(s)
		except (OverflowError, SyntaxError):
			continue
##		raise TestFailed, \
		print \
			  'No OverflowError on huge integer literal ' + `s`
elif eval('maxint == 9223372036854775807'):
	if eval('-9223372036854775807-1 != 01000000000000000000000'):
		raise TestFailed, 'max negative int'
	if eval('01777777777777777777777') != -1: raise TestFailed, 'oct -1'
	if eval('0xffffffffffffffff') != -1: raise TestFailed, 'hex -1'
	for s in '9223372036854775808', '02000000000000000000000', \
		 '0x10000000000000000':
		try:
			x = eval(s)
		except (OverflowError, SyntaxError):
			continue
		raise TestFailed, \
			  'No OverflowError on huge integer literal ' + `s`
else:
	print 'Weird maxint value', maxint

print_test('Long integers', 4)
x = 0L
x = 0l
x = 0xffffffffffffffffL
x = 0xffffffffffffffffl
x = 077777777777777777L
x = 077777777777777777l
x = 123456789012345678901234567890L
x = 123456789012345678901234567890l

print_test('Floating point', 4)
x = 3.14
x = 314.
x = 0.314
# XXX x = 000.314
x = .314
x = 3e14
x = 3E14
x = 3e-14
x = 3e+14
x = 3.e14
x = .3e14
x = 3.1e4

print_test('String literals', 3)

##def assert(s):
##	if not s: raise TestFailed, 'see traceback'

x = ''; y = ""; assert(len(x) == 0 and x == y)
x = '\''; y = "'"; assert(len(x) == 1 and x == y and ord(x) == 39)
x = '"'; y = "\""; assert(len(x) == 1 and x == y and ord(x) == 34)
x = "doesn't \"shrink\" does it"
y = 'doesn\'t "shrink" does it'
assert(len(x) == 24 and x == y)
x = "does \"shrink\" doesn't it"
y = 'does "shrink" doesn\'t it'
assert(len(x) == 24 and x == y)
x = """
The "quick"
brown fox
jumps over
the 'lazy' dog.
"""
y = '\nThe "quick"\nbrown fox\njumps over\nthe \'lazy\' dog.\n'
assert(x == y)
y = '''
The "quick"
brown fox
jumps over
the 'lazy' dog.
'''; assert(x == y)
y = "\n\
The \"quick\"\n\
brown fox\n\
jumps over\n\
the 'lazy' dog.\n\
"; assert(x == y)
y = '\n\
The \"quick\"\n\
brown fox\n\
jumps over\n\
the \'lazy\' dog.\n\
'; assert(x == y)

#Two quick tests of \x in String literals
#assert ord('\x0f1b') == 0x0f1b
assert '\x61xyz' == 'axyz'

print_test('Grammar', 2)

print_test('single_input', 3) # NEWLINE | simple_stmt | compound_stmt NEWLINE
# XXX can't test in a script -- this rule is only used when interactive

print_test('file_input') # (NEWLINE | stmt)* ENDMARKER
# Being tested as this very moment this very module

print_test('expr_input') # testlist NEWLINE
# XXX Hard to test -- used only in calls to input()

print_test('eval_input') # testlist ENDMARKER
x = eval('1, 0 or 1')

print_test('funcdef')
### 'def' NAME parameters ':' suite
### parameters: '(' [varargslist] ')'
### varargslist: (fpdef ['=' test] ',')* '*' NAME
###            | fpdef ['=' test] (',' fpdef ['=' test])* [',']
### fpdef: NAME | '(' fplist ')'
### fplist: fpdef (',' fpdef)* [',']
def f1(): pass
def f2(one_argument): pass
def f3(two, arguments): pass
def f4(two, (compound, (argument, list))): pass
def a1(one_arg,): pass
def a2(two, args,): pass
def v0(*rest): pass
def v1(a, *rest): pass
def v2(a, b, *rest): pass
def v3(a, (b, c), *rest): pass
def d01(a=1): pass
d01()
d01(1)
def d11(a, b=1): pass
d11(1)
d11(1, 2)
def d21(a, b, c=1): pass
d21(1, 2)
d21(1, 2, 3)
def d02(a=1, b=2): pass
d02()
d02(1)
d02(1, 2)
def d12(a, b=1, c=2): pass
d12(1)
d12(1, 2)
d12(1, 2, 3)
def d22(a, b, c=1, d=2): pass
d22(1, 2)
d22(1, 2, 3)
d22(1, 2, 3, 4)
def d01v(a=1, *rest): pass
d01v()
d01v(1)
d01v(1, 2)
def d11v(a, b=1, *rest): pass
d11v(1)
d11v(1, 2)
d11v(1, 2, 3)
def d21v(a, b, c=1, *rest): pass
d21v(1, 2)
d21v(1, 2, 3)
d21v(1, 2, 3, 4)
def d02v(a=1, b=2, *rest): pass
d02v()
d02v(1)
d02v(1, 2)
d02v(1, 2, 3)
def d12v(a, b=1, c=2, *rest): pass
d12v(1)
d12v(1, 2)
d12v(1, 2, 3)
d12v(1, 2, 3, 4)
def d22v(a, b, c=1, d=2, *rest): pass
d22v(1, 2)
d22v(1, 2, 3)
d22v(1, 2, 3, 4)
d22v(1, 2, 3, 4, 5)

### stmt: simple_stmt | compound_stmt
# Tested below

### simple_stmt: small_stmt (';' small_stmt)* [';']
print_test('simple_stmt')
x = 1; pass; del x

### small_stmt: expr_stmt | print_stmt  | pass_stmt | del_stmt | flow_stmt | import_stmt | global_stmt | access_stmt | exec_stmt
# Tested below

print_test('expr_stmt') # (exprlist '=')* exprlist
1
1, 2, 3
x = 1
x = 1, 2, 3
x = y = z = 1, 2, 3
x, y, z = 1, 2, 3
abc = a, b, c = x, y, z = xyz = 1, 2, (3, 4)
# NB these variables are deleted below

print_test('print_stmt') # 'print' (test ',')* [test]

beginCapture()
print 1, 2, 3
print 1, 2, 3,
print 
print 0 or 1, 0 or 1,
print 0 or 1
s = endCapture()
expected = """1 2 3
1 2 3
1 1 1
"""

assert s == expected, 'print %s' % repr(s)

print_test('del_stmt') # 'del' exprlist
del abc
del x, y, (z, xyz)

print_test('pass_stmt') # 'pass'
pass

print_test('flow_stmt') # break_stmt | continue_stmt | return_stmt | raise_stmt
# Tested below

print_test('break_stmt', 4) # 'break'
while 1: break

print_test('continue_stmt') # 'continue'
i = 1
while i: i = 0; continue

print_test('return_stmt') # 'return' [testlist]
def g1(): return
def g2(): return 1
g1()
x = g2()

print_test('raise_stmt') # 'raise' test [',' test]
try: raise RuntimeError, 'just testing'
except RuntimeError: pass
try: raise KeyboardInterrupt
except KeyboardInterrupt: pass

print_test('import_stmt', 3) # 'import' NAME (',' NAME)* | 'from' NAME 'import' ('*' | NAME (',' NAME)*)
import sys
import time, sys
from time import time
#from sys import *
from sys import path, argv

print_test('global_stmt') # 'global' NAME (',' NAME)*
def f():
	global a
	global a, b
	global one, two, three, four, five, six, seven, eight, nine, ten

print_test('exec_stmt') # 'exec' expr ['in' expr [',' expr]]
def f():
	z = None
	del z
	exec 'z=1+1\n'
	if z <> 2: raise TestFailed, 'exec \'z=1+1\'\\n'
	del z
	exec 'z=1+1'
	if z <> 2: raise TestFailed, 'exec \'z=1+1\''
f()
g = {}
exec 'z = 1' in g
if g.has_key('__builtins__'): del g['__builtins__']
if g <> {'z': 1}: raise TestFailed, 'exec \'z = 1\' in g'
g = {}
l = {}
exec 'global a; a = 1; b = 2' in g, l
if g.has_key('__builtins__'): del g['__builtins__']
if l.has_key('__builtins__'): del l['__builtins__']
if (g, l) <> ({'a':1}, {'b':2}): raise TestFailed, 'exec ... in g, l'


### compound_stmt: if_stmt | while_stmt | for_stmt | try_stmt | funcdef | classdef
# Tested below

print_test('if_stmt') # 'if' test ':' suite ('elif' test ':' suite)* ['else' ':' suite]
if 1: pass
if 1: pass
else: pass
if 0: pass
elif 0: pass
if 0: pass
elif 0: pass
elif 0: pass
elif 0: pass
else: pass

print_test('while_stmt') # 'while' test ':' suite ['else' ':' suite]
while 0: pass
while 0: pass
else: pass

print_test('for_stmt') # 'for' exprlist 'in' exprlist ':' suite ['else' ':' suite]
for i in 1, 2, 3: pass
for i, j, k in (): pass
else: pass
class Squares:
	def __init__(self, max):
		self.max = max
		self.sofar = []
	def __len__(self): return len(self.sofar)
	def __getitem__(self, i):
		if not 0 <= i < self.max: raise IndexError
		n = len(self.sofar)
		while n <= i:
			self.sofar.append(n*n)
			n = n+1
		return self.sofar[i]
n = 0
for x in Squares(10): n = n+x
if n != 285: raise TestFailed, 'for over growing sequence'

print_test('try_stmt')
### try_stmt: 'try' ':' suite (except_clause ':' suite)+ ['else' ':' suite]
###         | 'try' ':' suite 'finally' ':' suite
### except_clause: 'except' [expr [',' expr]]
try:
	1/0
except ZeroDivisionError:
	pass
else:
	pass
try: 1/0
except EOFError: pass
except TypeError, msg: pass
except RuntimeError, msg: pass
except: pass
else: pass
try: 1/0
except (EOFError, TypeError, ZeroDivisionError): pass
try: 1/0
except (EOFError, TypeError, ZeroDivisionError), msg: pass
try: pass
finally: pass

print_test('suite') # simple_stmt | NEWLINE INDENT NEWLINE* (stmt NEWLINE*)+ DEDENT
if 1: pass
if 1:
	pass
if 1:
	#
	#
	#
	pass
	pass
	#
	pass
	#

print_test('classdef') # 'class' NAME ['(' testlist ')'] ':' suite
class B: pass
class C1(B): pass
class C2(B): pass
class D(C1, C2, B): pass
class C:
	def meth1(self): pass
	def meth2(self, arg): pass
	def meth3(self, a1, a2): pass


print_test('operators')
print_test('test', 4)
### and_test ('or' and_test)*
### and_test: not_test ('and' not_test)*
### not_test: 'not' not_test | comparison
if not 1: pass
if 1 and 1: pass
if 1 or 1: pass
if not not not 1: pass
if not 1 and 1 and 1: pass
if 1 and 1 or 1 and 1 and 1 or not 1 and 1: pass

print_test('comparison')
### comparison: expr (comp_op expr)*
### comp_op: '<'|'>'|'=='|'>='|'<='|'<>'|'!='|'in'|'not' 'in'|'is'|'is' 'not'
if 1: pass
x = (1 == 1)
if 1 == 1: pass
if 1 != 1: pass
if 1 <> 1: pass
if 1 < 1: pass
if 1 > 1: pass
if 1 <= 1: pass
if 1 >= 1: pass
if 1 is 1: pass
if 1 is not 1: pass
if 1 in (): pass
if 1 not in (): pass
if 1 < 1 > 1 == 1 >= 1 <= 1 <> 1 != 1 in 1 not in 1 is 1 is not 1: pass

print_test('binary mask ops')
x = 1 & 1
x = 1 ^ 1
x = 1 | 1

print_test('shift ops')
x = 1 << 1
x = 1 >> 1
x = 1 << 1 >> 1

print_test('additive ops')
x = 1
x = 1 + 1
x = 1 - 1 - 1
x = 1 - 1 + 1 - 1 + 1

print_test('multiplicative ops')
x = 1 * 1
x = 1 / 1
x = 1 % 1
x = 1 / 1 * 1 % 1

print_test('unary ops')
x = +1
x = -1
x = ~1
x = ~1 ^ 1 & 1 | 1 & 1 ^ -1
x = -1*1/1 + 1*1 - ---1*1

print_test('selectors')
### trailer: '(' [testlist] ')' | '[' subscript ']' | '.' NAME
### subscript: expr | [expr] ':' [expr]
f1()
f2(1)
f2(1,)
f3(1, 2)
f3(1, 2,)
f4(1, (2, (3, 4)))
v0()
v0(1)
v0(1,)
v0(1,2)
v0(1,2,3,4,5,6,7,8,9,0)
v1(1)
v1(1,)
v1(1,2)
v1(1,2,3)
v1(1,2,3,4,5,6,7,8,9,0)
v2(1,2)
v2(1,2,3)
v2(1,2,3,4)
v2(1,2,3,4,5,6,7,8,9,0)
v3(1,(2,3))
v3(1,(2,3),4)
v3(1,(2,3),4,5,6,7,8,9,0)
import sys, time
c = sys.path[0]
x = time.time()
x = sys.modules['time'].time()
a = '01234'
c = a[0]
c = a[-1]
s = a[0:5]
s = a[:5]
s = a[0:]
s = a[:]
s = a[-5:]
s = a[:-1]
s = a[-4:-3]

print_test('atoms')
### atom: '(' [testlist] ')' | '[' [testlist] ']' | '{' [dictmaker] '}' | '`' testlist '`' | NAME | NUMBER | STRING
### dictmaker: test ':' test (',' test ':' test)* [',']

x = (1)
x = (1 or 2 or 3)
x = (1 or 2 or 3, 2, 3)

x = []
x = [1]
x = [1 or 2 or 3]
x = [1 or 2 or 3, 2, 3]
x = []

x = {}
x = {'one': 1}
x = {'one': 1,}
x = {'one' or 'two': 1 or 2}
x = {'one': 1, 'two': 2}
x = {'one': 1, 'two': 2,}
x = {'one': 1, 'two': 2, 'three': 3, 'four': 4, 'five': 5, 'six': 6}

x = `x`
x = `1 or 2 or 3`
x = x
x = 'x'
x = 123

### exprlist: expr (',' expr)* [',']
### testlist: test (',' test)* [',']
# These have been exercised enough above

