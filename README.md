[![Build Status](https://dev.azure.com/bakdata/public/_apis/build/status/bakdata.seq2?branchName=master)](https://dev.azure.com/bakdata/public/_build/latest?definitionId=21&branchName=master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.bakdata.seq2%3Aseq2&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.bakdata.seq2%3Aseq2)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.bakdata.seq2%3Aseq2&metric=coverage)](https://sonarcloud.io/dashboard?id=com.bakdata.seq2%3Aseq2)
[![Maven](https://img.shields.io/maven-central/v/com.bakdata.seq2/seq2.svg)](https://search.maven.org/search?q=g:com.bakdata.seq2%20AND%20a:seq2&core=gav)

# seq2

This library is an extension to the great [jOOλ library](https://github.com/jOOQ/jOOL).
It mainly provides native handling of pair streams.

## Usage

```java
final Seq2<String> words = Seq2.of("foo", "bar", "foo");
final Map<String, Integer> wordCounts = words.mapToPair(Function.identity(), s -> 1)
        .reduceByKey(Integer::sum)
        .toMap();
System.out.println(wordCounts);
// {bar=1, foo=2}
```

## Getting Started

You can add seq2 via Maven Central.

#### Gradle
```gradle
compile group: 'com.bakdata.seq2', name: 'seq2', version: '1.0.0'
```

#### Maven
```xml
<dependency>
    <groupId>com.bakdata.seq2</groupId>
    <artifactId>seq2</artifactId>
    <version>1.0.0</version>
</dependency>
```


For other build tools or versions, refer to the [latest version in MvnRepository](https://mvnrepository.com/artifact/com.bakdata.seq2/seq2/latest).

## Development

If you want to contribute to this project, you can simply clone the repository and build it via Gradle.
All dependencies should be included in the Gradle files, there are no external prerequisites.

```bash
> git clone git@github.com:bakdata/seq2.git
> cd seq2 && ./gradlew build
```

Please note, that we have [code styles](https://github.com/bakdata/bakdata-code-styles) for Java.
They are basically the Google style guide, with some small modifications.

## Contributing

We are happy if you want to contribute to this project.
If you find any bugs or have suggestions for improvements, please open an issue.
We are also happy to accept your PRs.
Just open an issue beforehand and let us know what you want to do and why.

## License
This project is licensed under the MIT license.
Have a look at the [LICENSE](https://github.com/bakdata/seq2/blob/master/LICENSE) for more details.
