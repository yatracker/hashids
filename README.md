[![hashids](https://hashids.org/public/img/hashids.gif 'Hashids')](https://hashids.org/)

[README](README.md) | [中文文档](README_zh.md)

This is a new library of [Hashids](https://hashids.org) implemented in Java, which almost fully compatible with [niieani/hashids.js](https://github.com/niieani/hashids.js). Here are some of its nice features.

- generates short, unique, non-sequential ids from numbers.
- support for any Unicode character set encoding, such as emoji characters
- support `Long.MAX_VALUE` and `BigInteger` numbers

## Getting started

Add the dependency.

```xml
<dependency>
    <groupId>com.yatracker</groupId>
    <artifactId>hashids</artifactId>
    <version>2.2.10</version>
</dependency>
```

Initialize `Hashids` instances by default, each parameter has a default value and can be left out.

```java
Hashids hashids = new HashidsBuilder().build();
```

Or build with custom construction parameters.

```java
Hashids hashids = new HashidsBuilder()
        .salt("this is my salt") // pass a salt to make your ids unique
        .minLength(12) // guaranteed minimum length of generated ids
        .alphabet("🍭😚abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890")
        .seps("cfhistuCFHISTU") // tries to avoid generating most common English curse words by generating ids that never have the following letters next to each other
        .build();
```

## Quick example

For compatibility with type `BigInteger`, all parameters and return types use `java.lang.Number`

```java
String encoded = hashids.encode(
        1, // Integer
        Long.MAX_VALUE, // Long
        BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE) // BigInteger
);

// When decoding, if the value is less than `Long.MAX_VALUE` it will return `Long`, otherwise it will return `BigInteger`.
Number[] decoded = hashids.decode(encoded);
```

Hex encoding and decoding is also supported, there is no limit to how large a hexadecimal number can be passed, and 
you can join any hex string to a long hex string. But the decoding will only be treated as one hex number, since 
both the parameter and the return result are `String` type.

```java
Number[] numbers = new Number[] {
        Long.MAX_VALUE, 
        BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE)
};
String joinedHex = Arrays.stream(numbers).map(number -> {
    if (number instanceof BigInteger) {
        return ((BigInteger) number).toString(16);
    } else {
        return Long.toString(number.longValue(), 16);
    }
}).collect(Collectors.joining());

String encoded = hashids.encodeHex(joinedHex);
String decoded = hashids.decodeHex(encoded);
Assertions.assertEquals(joinedHex, decoded);
```

## Benchmark

The test code is in [HashidsBenchmark.java](src/test/java/com/yatracker/benchmark/HashidsBenchmark.java)

- AMD Ryzen 9 5900X 12-Core Processor
- benchmark threads = 8
- -Xms4G, -Xmx4G
- Eclipse Temurin™ OpenJDK 1.8

Here is a summary of the test results against [yomorun/hashids-java:1.0.3](https://github.com/yomorun/hashids-java)

| Default Encode       | Mode  | Cnt   | Score | Error | Units |
|:---------------------|:------|:------|:------|:------|:------|
| yatracker/hashids    | thrpt | 10 | 6921557.957 ± 66076.560 | | ops/s |
| yomorun/hashids-java | thrpt | 10 | 4425359.269 ±  3541.210 | | ops/s |

| Default Decode       | Mode  | Cnt   | Score | Error | Units |
|:---------------------|:------|:------|:------|:------|:------|
| yatracker/hashids    | thrpt | 10 | 2726315.172 ± 64040.880 | | ops/s |
| yomorun/hashids-java | thrpt | 10 | 2282141.352 ± 39018.688 | | ops/s |

| Custom Params Encode | Mode  | Cnt   | Score | Error | Units |
|:---------------------|:------|:------|:------|:------|:------|
| yatracker/hashids    | thrpt | 10 | 4051214.915 ± 13976.185 | | ops/s |
| yomorun/hashids-java | thrpt | 10 | 3387045.682 ±  3422.060 | | ops/s |

| Custom Params Decode | Mode  | Cnt   | Score | Error | Units |
|:---------------------|:------|:------|:------|:------|:------|
| yatracker/hashids    | thrpt | 10 | 2179942.573 ± 42297.677 | | ops/s |
| yomorun/hashids-java | thrpt | 10 | 1800680.901 ± 28756.985 | | ops/s |

See [benchmark.log](benchmark/benchmark.log)