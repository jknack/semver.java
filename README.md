semver.java
===========

A Java Implementation of http://semver.org/

usage
======

```java

assertTrue(Semver.create(">0.1.2").matches("0.1.3"));

assertTrue(Semver.create("1.2.x").matches("1.2.3"));

assertTrue(Semver.create(">=1.0.2 <2.1.2").matches("1.1"));

assertTrue(Semver.create("1.3.4 || 1.3.5").matches("1.3.5"));

assertEquals("<1.0.0 || >=2.3.1 <2.4.5 || >=2.5.2 <3.0.0",
        Semver.create("<1.0.0 || >=2.3.1 <2.4.5 || >=2.5.2 <3.0.0"));
```

maven
======
Stable version: **0.1.0**


```xml
  <dependency>
    <groupId>com.github.jknack</groupId>
    <artifactId>semver.java</artifactId>
    <version>${semver.java-version}</version>
  </dependency>
```
 
Development version: **0.1.1-SNAPSHOT**

SNAPSHOT versions are NOT synchronized to Central. If you want to use a snapshot version you need to add the https://oss.sonatype.org/content/repositories/snapshots/ repository to your pom.xml.

dependencies
======

```
+- org.slf4j:slf4j-api:jar:1.6.4
+- org.apache.commons:commons-lang3:jar:3.1
```

help and support
======
 [Bugs, Issues and Features](https://github.com/jknack/semver/issues)

related projects
======
 [semver](http://semver.org/)

author
======
 [@edgarespina](https://twitter.com/edgarespina)

license
======
[Apache License 2](http://www.apache.org/licenses/LICENSE-2.0.html)

