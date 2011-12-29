#!/usr/local/bin/python
# -*- mode: python -*-
# $Id: test_re.py,v 1.1 2005/10/05 20:19:28 eytanadar Exp $

import sys
sys.path=['.']+sys.path

from test_support import *
import re
import sys, os, string #, traceback

print_test('re (test_re.py)', 1)

# Misc tests from Tim Peters' re.doc

print_test('basic functions', 2)

print_test('search', 3)
assert re.search('x*', 'axx').span(0) == (0, 0)
assert re.search('x*', 'axx').span() == (0, 0)
assert re.search('x+', 'axx').span(0) == (1, 3)
assert re.search('x+', 'axx').span() == (1, 3)
assert re.search('x', 'aaa') == None

print_test('match')
assert re.match('a*', 'xxx').span(0) == (0, 0)
assert re.match('a*', 'xxx').span() == (0, 0)
assert re.match('x*', 'xxxa').span(0) == (0, 3)
assert re.match('x*', 'xxxa').span() == (0, 3)
assert re.match('a+', 'xxx') == None


print_test('sub')
assert re.sub("(?i)b+", "x", "bbbb BBBB") == 'x x'

def bump_num(matchobj):
    int_value = int(matchobj.group(0))
    return str(int_value + 1)

assert re.sub(r'\d+', bump_num, '08.2 -2 23x99y') == '9.3 -3 24x100y'
assert re.sub(r'\d+', bump_num, '08.2 -2 23x99y', 3) == '9.3 -3 23x99y'

assert re.sub('.', lambda m: r"\n", 'x') == '\\n'
assert re.sub('.', r"\n", 'x') == '\n'

s = r"\1\1"
assert re.sub('(.)', s, 'x') == 'xx'
assert re.sub('(.)', re.escape(s), 'x') == s
assert re.sub('(.)', lambda m: s, 'x') == s

assert re.sub('(?P<a>x)', '\g<a>\g<a>', 'xx') == 'xxxx'
assert re.sub('(?P<a>x)', '\g<a>\g<1>', 'xx') == 'xxxx'
assert re.sub('(?P<unk>x)', '\g<unk>\g<unk>', 'xx') == 'xxxx'
assert re.sub('(?P<unk>x)', '\g<1>\g<1>', 'xx') == 'xxxx'

assert re.sub('a', r'\t\n\v\r\f\a\b\B\Z\a\A\w\W\s\S\d\D', 'a') == '\t\n\v\r\f\a\b\\B\\Z\a\\A\\w\\W\\s\\S\\d\\D'
assert re.sub('a', '\t\n\v\r\f\a', 'a') == '\t\n\v\r\f\a'
assert re.sub('a', '\t\n\v\r\f\a', 'a') == (chr(9)+chr(10)+chr(11)+chr(13)+chr(12)+chr(7))

assert re.sub('^\s*', 'X', 'test') == 'Xtest'

assert re.sub('a', 'b', 'aaaaa') == 'bbbbb'
assert re.sub('a', 'b', 'aaaaa', 1) == 'baaaa'


print_test('symbolic references', 4)

try:
    re.sub('(?P<a>x)', '\g<a', 'xx')
except re.error, reason:
    pass
else:
    raise TestFailed, "symbolic reference"

try:
    re.sub('(?P<a>x)', '\g<', 'xx')
except re.error, reason:
    pass
else:
    raise TestFailed, "symbolic reference"

try:
    re.sub('(?P<a>x)', '\g', 'xx')
except re.error, reason:
    pass
else:
    raise TestFailed, "symbolic reference"

try:
    re.sub('(?P<a>x)', '\g<a a>', 'xx')
except IndexError, reason:
    pass
else:
    raise TestFailed, "symbolic reference"

try:
    re.sub('(?P<a>x)', '\g<1a1>', 'xx')
except IndexError, reason:
    pass
else:
    raise TestFailed, "symbolic reference"

try:
    re.sub('(?P<a>x)', '\g<ab>', 'xx')
except (IndexError, re.error), reason:
    pass
else:
    raise TestFailed, "symbolic reference"

try:
    re.sub('(?P<a>x)|(?P<b>y)', '\g<b>', 'xx')
except re.error, reason:
    pass
else:
    raise TestFailed, "symbolic reference"

try:
    re.sub('(?P<a>x)|(?P<b>y)', '\\2', 'xx')
except re.error, reason:
    pass
else:
    raise TestFailed, "symbolic reference"


print_test('subn', 3)

assert re.subn("(?i)b+", "x", "bbbb BBBB") == ('x x', 2)
assert re.subn("b+", "x", "bbbb BBBB") == ('x BBBB', 1)
assert re.subn("b+", "x", "xyz") == ('xyz', 0)
assert re.subn("b*", "x", "xyz") == ('xxxyxzx', 4)
assert re.subn("b*", "x", "xyz", 2) == ('xxxyz', 2)

print_test('split')

assert re.split(":", ":a:b::c") == ['', 'a', 'b', '', 'c']
assert re.split(":*", ":a:b::c") == ['', 'a', 'b', 'c']
assert re.split("(:*)", ":a:b::c") == ['', ':', 'a', ':', 'b', '::', 'c']
assert re.split("(?::*)", ":a:b::c") == ['', 'a', 'b', 'c']
assert re.split("(:)*", ":a:b::c") == ['', ':', 'a', ':', 'b', ':', 'c']
assert re.split("([b:]+)", ":a:b::c") == ['', ':', 'a', ':b::', 'c']
assert re.split("(b)|(:+)", ":a:b::c") == \
       ['', None, ':', 'a', None, ':', '', 'b', None, '', None, '::', 'c']
assert re.split("(?:b)|(?::+)", ":a:b::c") == ['', 'a', '', '', 'c']
    
assert re.split(":", ":a:b::c", 2) == ['', 'a', 'b::c']
assert re.split(':', 'a:b:c:d', 2) == ['a', 'b', 'c:d']

assert re.split("(:)", ":a:b::c", 2) == ['', ':', 'a', ':', 'b::c']
assert re.split("(:*)", ":a:b::c", 2) == ['', ':', 'a', ':', 'b::c']

print_test('match.groups', 2)


# No groups at all
m = re.match('a', 'a') ; assert m.groups() == ()
# A single group
m = re.match('(a)', 'a') ; assert m.groups() == ('a',)

pat = re.compile('((a)|(b))(c)?')
assert pat.match('a').groups() == ('a', 'a', None, None)
assert pat.match('b').groups() == ('b', None, 'b', None)
assert pat.match('ac').groups() == ('a', 'a', None, 'c')
assert pat.match('bc').groups() == ('b', None, 'b', 'c')



print_test('match.group', 2)

# A single group
m = re.match('(a)', 'a')
assert m.group(0) == 'a' ; assert m.group(0) == 'a'
assert m.group(1) == 'a' ; assert m.group(1, 1) == ('a', 'a')

pat = re.compile('(?:(?P<a1>a)|(?P<b2>b))(?P<c3>c)?')
assert pat.match('a').group(1, 2, 3) == ('a', None, None)
assert pat.match('b').group('a1', 'b2', 'c3') == (None, 'b', None)
assert pat.match('ac').group(1, 'b2', 3) == ('a', None, 'c')

print_test('escape', 2)

p=""
for i in range(0, 256):
    p = p + chr(i)
    assert re.match(re.escape(chr(i)), chr(i)) != None
    assert re.match(re.escape(chr(i)), chr(i)).span() == (0,1)

pat=re.compile( re.escape(p) )
assert pat.match(p) != None
assert pat.match(p).span() == (0,256)


#if verbose:
#    print 'Pickling a RegexObject instance'

#import pickle
#pat = re.compile('a(?:b|(c|e){1,2}?|d)+?(.)')
#s = pickle.dumps(pat)
#pat = pickle.loads(s)

print_test('flags', 2)

assert re.I == re.IGNORECASE
assert re.L == re.LOCALE
assert re.M == re.MULTILINE
assert re.S == re.DOTALL
assert re.X == re.VERBOSE

for flags in [re.I, re.M, re.X, re.S, re.L]:
    r = re.compile('^pattern$', flags)

from re_tests import *

print_test("symbolic groups in regex's", 2)
print_test("perl5-compatible regex's", 2)

for t in tests:
    sys.stdout.flush()
    pattern=s=outcome=repl=expected=None
    if len(t)==5:
        pattern, s, outcome, repl, expected = t
    elif len(t)==3:
        pattern, s, outcome = t
    else:
        raise ValueError, ('Test tuples should have 3 or 5 fields',t)

    try:
        obj=re.compile(pattern)
    except re.error:
        if outcome==SYNTAX_ERROR: pass  # Expected a syntax error
        else:
            print '=== Syntax error:', t
    except KeyboardInterrupt: raise KeyboardInterrupt
    except:
        print '*** Unexpected error ***', t
        #if verbose:
        #    traceback.print_exc(file=sys.stdout)
    else:
        try:
            result=obj.search(s)
        except (re.error), msg:
            print '=== Unexpected exception', t, repr(msg)
        if outcome==SYNTAX_ERROR:
            # This should have been a syntax error; forget it.
            pass
        elif outcome==FAIL:
            if result is None: pass   # No match, as expected
            else: print '=== Succeeded incorrectly', t
        elif outcome==SUCCEED:
            if result is not None:
                # Matched, as expected, so now we compute the
                # result string and compare it to our expected result.
                start, end = result.span(0)
                vardict={'found': result.group(0),
                         'groups': result.group(),
                         'flags': result.re.flags}
                for i in range(1, 100):
                    try:
                        gi = result.group(i)
                        # Special hack because else the string concat fails:
                        if gi is None:
                            gi = "None"
                    except IndexError:
                        gi = "Error"
                    vardict['g%d' % i] = gi
                for i in result.re.groupindex.keys():
                    try:
                        gi = result.group(i)
                        if gi is None:
                            gi = "None"
                    except IndexError:
                        gi = "Error"
                    vardict[i] = gi
                repl=eval(repl, vardict)
                if repl!=expected:
                    print '=== grouping error', t,
                    print repr(repl)+' should be '+repr(expected)
            else:
                print '=== Failed incorrectly', t
                continue

            # Try the match with the search area limited to the extent
            # of the match and see if it still succeeds.  \B will
            # break (because it won't match at the end or start of a
            # string), so we'll ignore patterns that feature it.

            if pattern[:2]!='\\B' and pattern[-2:]!='\\B':
                obj=re.compile(pattern)
                result=obj.search(s, result.start(0), result.end(0)+1)
                if result==None:
                    print '=== Failed on range-limited match', t

            # Try the match with IGNORECASE enabled, and check that it
            # still succeeds.
            obj=re.compile(pattern, re.IGNORECASE)
            result=obj.search(s)
            if result==None:
                print '=== Fails on case-insensitive match', t

            # Try the match with LOCALE enabled, and check that it
            # still succeeds.
            obj=re.compile(pattern, re.LOCALE)
            result=obj.search(s)
            if result==None:
                print '=== Fails on locale-sensitive match', t
