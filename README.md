# Jump Consistent Hash

A Java implementation of the jump consistent hash from Lamping and Veach.

Paper: [http://arxiv.org/ftp/arxiv/papers/1406/1406.2294.pdf](http://arxiv.org/ftp/arxiv/papers/1406/1406.2294.pdf)

## Abstract

Extracted from the paper

> We present jump consistent hash, a fast, minimal memory, consistent hash algorithm that can
be expressed in about 5 lines of code. In comparison to the algorithm of Karger et al., jump
consistent hash requires no storage, is faster, and does a better job of evenly dividing the key
space among the buckets and of evenly dividing the workload when the number of buckets
changes. Its main limitation is that the buckets must be numbered sequentially, which makes it
more suitable for data storage applications than for distributed web caching.

# Usage

Add the dependency to your pom.xml

    <dependency>
        <groupId>com.github.ssedano</groupId>
        <artifactId>jump-consistent-hash</artifactId>
        <version>1.0.0</version>
    </dependency>

Or download the `jar` from [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cjump-consistent-hash) and add it to your classpath.

Clone the repo and build it with maven and add the `jar` to your classpath.

    $ git clone https://github.com/ssedano/jump-consistent-hash
    $ cd jump-consistent-hash
    $ mvn clean install

Or copy the class and add it to your code base.

Then import it and use it.

    int jumpConsistentHash = JumpConsistentHash.jumpConsistenHash(key, buckets);

Additional info in the `javadoc`.

# License

Licensed under [Apache 2](http://www.apache.org/licenses/LICENSE-2.0).
