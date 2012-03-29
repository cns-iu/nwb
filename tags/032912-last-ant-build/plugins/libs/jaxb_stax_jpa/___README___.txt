IMPORTANT: Client bundles should import *aggressively* from this package, as unresolved dependencies often will not be caught until runtime (and even then, reported poorly if at all).
In particular, you may need to import com.sun.xml.bind.v2 (even if you don't think you need it).


Things to know before editing this plug-in:
* JAXB is part of JRE 6, but not JRE 5.
* JAXB does not play nice with OSGi (classloader access issues).


This bundle was put together using http://digiassn.blogspot.com/2008/09/osgi-jaxb-with-osgi.html as a rough guide.

It incorporates JAXB, JPA, and STAX jars created by people at DynamicJava.org with OSGi compatibility in mind.
http://www.dynamicjava.org/projects/jsr-api


Do not change the execution environment of this plug-in from J2SE-1.5.