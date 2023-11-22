/*
 * Copyright (c), 2023 bakdata GmbH
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bakdata.util.seq2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.jupiter.api.Test;

class PairSeqTest {

    @Test
    void shouldConstructFromMap() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b")))
                .hasSize(2)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"), new Tuple2<>(2, "b"));
    }

    @Test
    void shouldCollectToMap() {
        assertThat(PairSeq.seq(Map.of(1, "a", 2, "b")).toMap())
                .hasSize(2)
                .containsEntry(1, "a")
                .containsEntry(2, "b");
    }

    @Test
    void shouldCollectToList() {
        assertThat(PairSeq.seq(Map.of(1, "a", 2, "b")).toList())
                .hasSize(2)
                .contains(new Tuple2<>(1, "a"))
                .contains(new Tuple2<>(2, "b"));
    }

    @Test
    void shouldMap() {
        assertThat((Stream<String>) PairSeq.seq(Map.of(1, "a", 2, "b")).map((k, v) -> k + v))
                .hasSize(2)
                .containsExactlyInAnyOrder("1a", "2b");
    }

    @Test
    void shouldMapToLong() {
        assertThat(PairSeq.seq(Map.of(1, "a", 2, "b")).mapToLong((k, v) -> k))
                .hasSize(2)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void shouldMapToInt() {
        assertThat(PairSeq.seq(Map.of(1, "a", 2, "b")).mapToInt((k, v) -> k))
                .hasSize(2)
                .containsExactlyInAnyOrder(1, 2);
    }

    @Test
    void shouldMapToDouble() {
        assertThat(PairSeq.seq(Map.of(1, "a", 2, "b")).mapToDouble((k, v) -> k))
                .hasSize(2)
                .containsExactlyInAnyOrder(1.0, 2.0);
    }

    @Test
    void shouldFlatMap() {
        assertThat((Stream<String>) PairSeq.seq(Map.of(1, "ab", 2, "bc"))
                .flatMap((k, v) -> v.chars().mapToObj(i -> k + String.valueOf((char) i))))
                .hasSize(4)
                .containsExactlyInAnyOrder("1a", "1b", "2b", "2c");
    }

    @Test
    void shouldFlatMapToLong() {
        assertThat(PairSeq.seq(Map.of(1, "ab", 2, "bc"))
                .flatMapToLong((k, v) -> LongStream.range(0, k)))
                .hasSize(3)
                .containsExactlyInAnyOrder(0L, 0L, 1L);
    }

    @Test
    void shouldFlatMapToInt() {
        assertThat(PairSeq.seq(Map.of(1, "ab", 2, "bc"))
                .flatMapToInt((k, v) -> IntStream.range(0, k)))
                .hasSize(3)
                .containsExactlyInAnyOrder(0, 0, 1);
    }

    @Test
    void shouldFlatMapToDouble() {
        assertThat(PairSeq.seq(Map.of(1, "ab", 2, "bc"))
                .flatMapToDouble((k, v) -> DoubleStream.of(k)))
                .hasSize(2)
                .containsExactlyInAnyOrder(1.0, 2.0);
    }

    @Test
    void shouldFlatMapToPair() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapToPair((k, v) -> PairSeq.seq(Map.of(k, v, k + 1, v + v))))
                .hasSize(4)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"), new Tuple2<>(2, "aa"),
                        new Tuple2<>(2, "b"), new Tuple2<>(3, "bb"));
    }

    @Test
    void shouldFlatMapToOptionalPair() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapToOptionalPair((k, v) -> k == 1 ? Optional.empty() : Optional.of(new Tuple2<>(k + 1, v + v))))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(3, "bb"));
    }

    @Test
    void shouldFlatMapToIterablePair() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapToIterablePair((k, v) -> List.of(new Tuple2<>(k, v), new Tuple2<>(k + 1, v + v))))
                .hasSize(4)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"), new Tuple2<>(2, "aa"),
                        new Tuple2<>(2, "b"), new Tuple2<>(3, "bb"));
    }

    @Test
    void shouldFlatMapValues() {
        assertThat((Stream<Tuple2<Integer, Character>>) PairSeq.seq(Map.of(1, "ab", 2, "bc"))
                .flatMapValues(v -> v.chars().mapToObj(i -> (char) i)))
                .hasSize(4)
                .containsExactlyInAnyOrder(new Tuple2<>(1, 'a'), new Tuple2<>(1, 'b'), new Tuple2<>(2, 'b'),
                        new Tuple2<>(2, 'c'));
    }

    @Test
    void shouldFlatMapKeysToPair() {
        assertThat((Stream<Tuple2<Integer, Integer>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapKeysToPair(k -> PairSeq.seq(Map.of(k, k, k + 1, k + 1))))
                .hasSize(4)
                .containsExactlyInAnyOrder(new Tuple2<>(1, 1), new Tuple2<>(2, 2),
                        new Tuple2<>(2, 2), new Tuple2<>(3, 3));
    }

    @Test
    void shouldFlatMapKeysToOptionalPair() {
        assertThat((Stream<Tuple2<Integer, Integer>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapKeysToOptionalPair(k -> k == 1 ? Optional.empty() : Optional.of(new Tuple2<>(k + 1, k))))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(3, 2));
    }

    @Test
    void shouldFlatMapKeysToIterablePair() {
        assertThat((Stream<Tuple2<Integer, Integer>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapKeysToIterablePair(k -> List.of(new Tuple2<>(k, k), new Tuple2<>(k + 1, k + 1))))
                .hasSize(4)
                .containsExactlyInAnyOrder(new Tuple2<>(1, 1), new Tuple2<>(2, 2),
                        new Tuple2<>(2, 2), new Tuple2<>(3, 3));
    }

    @Test
    void shouldFlatMapValuesToPair() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, Map.of(3, "a"), 2, Map.of(4, "b", 5, "c")))
                .flatMapValuesToPair(PairSeq::seq))
                .hasSize(3)
                .containsExactlyInAnyOrder(new Tuple2<>(3, "a"), new Tuple2<>(4, "b"), new Tuple2<>(5, "c"));
    }

    @Test
    void shouldFlatMapValuesToOptionalPair() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(
                        Map.<Integer, Optional<Tuple2<Integer, String>>>of(1, Optional.of(new Tuple2<>(3, "a")), 2,
                                Optional.empty()))
                .flatMapValuesToOptionalPair(Function.identity()))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(3, "a"));
    }

    @Test
    void shouldFlatMapValuesToIterablePair() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(
                        Map.of(1, List.of(new Tuple2<>(3, "a")), 2, List.of(new Tuple2<>(4, "b"), new Tuple2<>(5,
                                "c"))))
                .flatMapValuesToIterablePair(Function.identity()))
                .hasSize(3)
                .containsExactlyInAnyOrder(new Tuple2<>(3, "a"), new Tuple2<>(4, "b"), new Tuple2<>(5, "c"));
    }

    @Test
    void shouldFlatMapKeys() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapKeys(k -> IntStream.range(0, k).boxed()))
                .hasSize(3)
                .containsExactlyInAnyOrder(new Tuple2<>(0, "a"), new Tuple2<>(0, "b"), new Tuple2<>(1, "b"));
    }

    @Test
    void shouldMapValues() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b")).mapValues(v -> v + v))
                .hasSize(2)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "aa"), new Tuple2<>(2, "bb"));
    }

    @Test
    void shouldMapKeys() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b")).mapKeys(v -> v + 1))
                .hasSize(2)
                .containsExactlyInAnyOrder(new Tuple2<>(2, "a"), new Tuple2<>(3, "b"));
    }

    @Test
    void shouldReturnValues() {
        assertThat((Stream<String>) PairSeq.seq(Map.of(1, "a", 2, "b")).values())
                .hasSize(2)
                .containsExactlyInAnyOrder("a", "b");
    }

    @Test
    void shouldReturnKeys() {
        assertThat((Stream<Integer>) PairSeq.seq(Map.of(1, "a", 2, "b")).keys())
                .hasSize(2)
                .containsExactlyInAnyOrder(1, 2);
    }

    @Test
    void shouldGroupByKey() {
        assertThat((Stream<Tuple2<Integer, List<String>>>) PairSeq
                .seq(Seq.of(new Tuple2<>(1, "a"), new Tuple2<>(1, "b"), new Tuple2<>(2, "c")))
                .groupByKey()
                .mapValues(Seq2::toList))
                .hasSize(2)
                .anySatisfy(t -> {
                    assertThat(t.v1()).isEqualTo(1);
                    assertThat(t.v2())
                            .hasSize(2)
                            .containsExactlyInAnyOrder("a", "b");
                })
                .anySatisfy(t -> {
                    assertThat(t.v1()).isEqualTo(2);
                    assertThat(t.v2())
                            .hasSize(1)
                            .containsExactlyInAnyOrder("c");
                });
    }

    @Test
    void shouldFilter() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .filter((k, v) -> k > 1))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(2, "b"));
    }

    @Test
    void shouldFilterNot() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .filterNot((k, v) -> k > 1))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"));
    }

    @Test
    void shouldFilterKeys() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .filterKeys(k -> k > 1))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(2, "b"));
    }

    @Test
    void shouldFilterKeysNot() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .filterKeysNot(k -> k > 1))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"));
    }

    @Test
    void shouldFilterValues() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .filterValues("a"::equals))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"));
    }

    @Test
    void shouldFilterValuesNot() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .filterValuesNot("a"::equals))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(2, "b"));
    }

    @Test
    void shouldGroupBy() {
        final Map<String, List<Tuple2<Integer, String>>> actual = PairSeq.seq(Map.of(1, "a", 2, "a"))
                .groupBy((k, v) -> v);
        assertThat(actual)
                .hasSize(1)
                .hasEntrySatisfying("a", value -> assertThat(value)
                        .containsExactlyInAnyOrder(new Tuple2<>(1, "a"), new Tuple2<>(2, "a")));
    }

    @Test
    void shouldFlatMapToIterable() {
        assertThat((Stream<Object>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapToIterable((k, v) -> List.of(k, (Object) v)))
                .hasSize(4)
                .containsExactlyInAnyOrder(1, "a", 2, "b");
    }

    @Test
    void shouldFlatMapToOptional() {
        assertThat((Stream<String>) PairSeq.seq(Map.of(1, "a", 2, "b")).flatMapToOptional((k, v) -> Optional.of(v)))
                .hasSize(2)
                .containsExactlyInAnyOrder("a", "b");
        assertThat((Stream<Integer>) PairSeq.seq(Map.of(1, "a", 2, "b")).flatMapToOptional((k, v) -> Optional.of(k)))
                .hasSize(2)
                .containsExactlyInAnyOrder(1, 2);
        final Map<Integer, String> map = new HashMap<>();
        map.put(1, "a");
        map.put(2, null);
        assertThat((Stream<String>) PairSeq.seq(map).flatMapToOptional((k, v) -> Optional.ofNullable(v)))
                .hasSize(1)
                .containsExactlyInAnyOrder("a");
    }

    @Test
    void shouldFlatMapValuesToIterable() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapValuesToIterable(v -> List.of(v, v + 1)))
                .hasSize(4)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"), new Tuple2<>(1, "a1"), new Tuple2<>(2, "b"),
                        new Tuple2<>(2, "b1"));
    }

    @Test
    void shouldFlatMapValuesToOptional() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapValuesToOptional(v -> "a".equals(v) ? Optional.of(v) : Optional.empty()))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"));
    }

    @Test
    void shouldFlatMapKeysToIterable() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapKeysToIterable(k -> List.of(k, k + 1)))
                .hasSize(4)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"), new Tuple2<>(2, "a"), new Tuple2<>(2, "b"),
                        new Tuple2<>(3, "b"));
    }

    @Test
    void shouldFlatMapKeysToOptional() {
        assertThat((Stream<Tuple2<Integer, String>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .flatMapKeysToOptional(k -> 1 == k ? Optional.of(k) : Optional.empty()))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(1, "a"));
    }

    @Test
    void shouldInnerJoinByKey() {
        final Map<Integer, Tuple2<String, Long>> actual = PairSeq.seq(Map.of(1, "a", 2, "b"))
                .innerJoinByKey(PairSeq.seq(Map.of(1, 1L, 2, 2L)))
                .toMap();
        assertThat(actual)
                .hasSize(2)
                .hasEntrySatisfying(1, value -> {
                    assertThat(value.v1()).isEqualTo("a");
                    assertThat(value.v2()).isEqualTo(1L);
                })
                .hasEntrySatisfying(2, value -> {
                    assertThat(value.v1()).isEqualTo("b");
                    assertThat(value.v2()).isEqualTo(2L);
                });
    }

    @Test
    void shouldInnerSelfJoinByKey() {
        final Map<Integer, Tuple2<String, String>> actual = PairSeq.seq(Map.of(1, "a", 2, "b"))
                .innerSelfJoinByKey()
                .toMap();
        assertThat(actual)
                .hasSize(2)
                .hasEntrySatisfying(1, value -> {
                    assertThat(value.v1()).isEqualTo("a");
                    assertThat(value.v2()).isEqualTo("a");
                })
                .hasEntrySatisfying(2, value -> {
                    assertThat(value.v1()).isEqualTo("b");
                    assertThat(value.v2()).isEqualTo("b");
                });
    }

    @Test
    void shouldLeftOuterJoinByKey() {
        final Map<Integer, Tuple2<String, Long>> actual = PairSeq.seq(Map.of(1, "a", 2, "b"))
                .leftOuterJoinByKey(PairSeq.seq(Map.of(1, 1L, 3, 2L)))
                .toMap();
        assertThat(actual)
                .hasSize(2)
                .hasEntrySatisfying(1, value -> {
                    assertThat(value.v1()).isEqualTo("a");
                    assertThat(value.v2()).isEqualTo(1L);
                })
                .hasEntrySatisfying(2, value -> {
                    assertThat(value.v1()).isEqualTo("b");
                    assertThat(value.v2()).isNull();
                });
    }

    @Test
    void shouldRightOuterJoinByKey() {
        final Map<Integer, Tuple2<String, Long>> actual = PairSeq.seq(Map.of(3, "a", 2, "b"))
                .rightOuterJoinByKey(PairSeq.seq(Map.of(1, 1L, 2, 2L)))
                .toMap();
        assertThat(actual)
                .hasSize(2)
                .hasEntrySatisfying(1, value -> {
                    assertThat(value.v1()).isNull();
                    assertThat(value.v2()).isEqualTo(1L);
                })
                .hasEntrySatisfying(2, value -> {
                    assertThat(value.v1()).isEqualTo("b");
                    assertThat(value.v2()).isEqualTo(2L);
                });
    }

    @Test
    void shouldJoinToStringOrEmpty() {
        assertThat(PairSeq.seq(List.of(new Tuple2<>(1, "a"), new Tuple2<>(2, "b"))).toStringOrEmpty(" "))
                .hasValue("(1, a) (2, b)");
        assertThat(PairSeq.empty().toStringOrEmpty(" ")).isNotPresent();
    }

    @Test
    void shouldJoinToStringOrEmptyWithPrefixAndSuffix() {
        assertThat(PairSeq.seq(List.of(new Tuple2<>(1, "a"), new Tuple2<>(2, "b"), new Tuple2<>(3, "c")))
                .toStringOrEmpty(" ", "^", "$"))
                .hasValue("^(1, a) (2, b) (3, c)$");
        assertThat(PairSeq.empty().toStringOrEmpty(" ", "^", "$")).isNotPresent();
    }

    @Test
    void shouldSelectKey() {
        assertThat((Stream<Tuple2<String, Tuple2<Integer, String>>>) PairSeq.seq(Map.of(1, "a", 2, "b"))
                .selectKey((k, v) -> k + v))
                .hasSize(2)
                .containsExactlyInAnyOrder(new Tuple2<>("1a", new Tuple2<>(1, "a")),
                        new Tuple2<>("2b", new Tuple2<>(2, "b")));
    }

}
