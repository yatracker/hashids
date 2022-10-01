[![hashids](http://hashids.org/public/img/hashids.gif 'Hashids')](http://hashids.org/)

[README](README.md) | [中文文档](README_zh.md)

这是一个新的用 Java 实现的 Hashids 库，几乎完全兼容 [niieani/hashids.js](https://github.com/niieani/hashids.js)，它包含下面这些不错的功能。

- 根据数字生成简短、唯一、非连续的 id
- 支持任意 Unicode 字符集编码，例如 emoji 字符
- 支持 Long.MAX_VALUE 以及 BigInteger 的数字

## 开始

默认初始化 `Hashids` 实例 （每个参数都有默认值，可以不传）

```java
Hashids hashids = new HashidsBuilder().build();
```

或者自定义构造参数

```java
Hashids hashids = new HashidsBuilder()
        .salt("this is my salt") // 加盐使你的 id 独一无二
        .minLength(12) // 保证生成 id 的最小长度
        .alphabet("🍭😚abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890")
        .seps("cfhistuCFHISTU") // 避免生成最常见的英语骂人话，生成的 id 永远不会有 seps 中的字母相邻
        .build();
```

## 例子

为了兼容 `BigInteger` 类型的数字，所有的参数和返回类型都使用了 `java.lang.Number`

```java
String encoded = hashids.encode(
        1, // Integer
        Long.MAX_VALUE, // Long
        BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE) // BigInteger
);

// 解码的时候，如果值小于 `Long.MAX_VALUE` 会返回 `Long` 类型，否则返回 `BigInteger`
Number[] decoded = hashids.decode(encoded);
```

同样支持 Hex 编解码，传递多大的十六进制数字是没有限制的，可以任意拼接 Hex 字符串。  
但是解码只会当成一个数字处理（参数和返回结果都是 `String` 类型）

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

## 性能

测试代码在 [HashidsBenchmark.java](src/test/java/com/yatracker/benchmark/HashidsBenchmark.java) 

- AMD Ryzen 9 5900X 12-Core Processor
- benchmark threads = 8
- -Xms4G, -Xmx4G
- Eclipse Temurin™ OpenJDK 1.8

对比 [yomorun/hashids-java](https://github.com/yomorun/hashids-java) 测试结果汇总如下

| 默认编码测试               | Mode  | Cnt   | Score | Error | Units |
|:---------------------|:------|:------|:------|:------|:------|
| yatracker/hashids    | thrpt | 10 | 6921557.957 ± 66076.560 | | ops/s |
| yomorun/hashids-java | thrpt | 10 | 4425359.269 ±  3541.210 | | ops/s |

| 默认解码测试               | Mode  | Cnt   | Score | Error | Units |
|:---------------------|:------|:------|:------|:------|:------|
| yatracker/hashids    | thrpt | 10 | 2726315.172 ± 64040.880 | | ops/s |
| yomorun/hashids-java | thrpt | 10 | 2282141.352 ± 39018.688 | | ops/s |

| 自定义参数解码测试            | Mode  | Cnt   | Score | Error | Units |
|:---------------------|:------|:------|:------|:------|:------|
| yatracker/hashids    | thrpt | 10 | 4051214.915 ± 13976.185 | | ops/s |
| yomorun/hashids-java | thrpt | 10 | 3387045.682 ±  3422.060 | | ops/s |

| 自定义参数解码测试            | Mode  | Cnt   | Score | Error | Units |
|:---------------------|:------|:------|:------|:------|:------|
| yatracker/hashids                 | thrpt | 10 | 2179942.573 ± 42297.677 | | ops/s |
| yomorun/hashids-java | thrpt | 10 | 1800680.901 ± 28756.985 | | ops/s |

完整输出见 [benchmark.log](benchmark/benchmark.log)