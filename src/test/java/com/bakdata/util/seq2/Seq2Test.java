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

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.jupiter.api.Test;

class Seq2Test {

    @Test
    void shouldCollectToList() {
        assertThat(Seq2.seq(List.of(1, 2)).toList())
                .hasSize(2)
                .containsExactly(1, 2);
    }

    @Test
    void shouldFlatMapToIterable() {
        assertThat((Stream<Integer>) Seq2.of(1, 2).flatMapToIterable(List::of))
                .hasSize(2)
                .containsExactlyInAnyOrder(1, 2);
    }

    @Test
    void shouldFlatMapToOptional() {
        assertThat((Stream<Integer>) Seq2.of(1, 2).flatMapToOptional(Optional::of))
                .hasSize(2)
                .containsExactlyInAnyOrder(1, 2);
        assertThat((Stream<Integer>) Seq2.of(1, null).flatMapToOptional(Optional::ofNullable))
                .hasSize(1)
                .containsExactlyInAnyOrder(1);
    }

    @Test
    void shouldJoinToString() {
        assertThat(Seq2.seq(List.of(1, 2)).toStringOrEmpty(" ")).hasValue("1 2");
        assertThat(Seq2.empty().toStringOrEmpty(" ")).isNotPresent();
    }

    @Test
    void shouldJoinToStringWithPrefixAndSuffix() {
        assertThat(Seq2.seq(List.of(1, 2, 3)).toStringOrEmpty(" ", "^", "$")).hasValue("^1 2 3$");
        assertThat(Seq2.empty().toStringOrEmpty(" ", "^", "$")).isNotPresent();
    }

    @Test
    void shouldSelectKey() {
        assertThat((Stream<Tuple2<Integer, Integer>>) Seq2.seq(List.of(1, 2))
                .selectKey(i -> i + 1))
                .hasSize(2)
                .containsExactlyInAnyOrder(new Tuple2<>(2, 1), new Tuple2<>(3, 2));
    }

    @Test
    void shouldFlatMapToOptionalPair() {
        assertThat((Stream<Tuple2<Integer, Integer>>) Seq2.seq(List.of(1, 2))
                .flatMapToOptionalPair(i -> i == 1 ? Optional.empty() : Optional.of(new Tuple2<>(i + 1, i))))
                .hasSize(1)
                .containsExactlyInAnyOrder(new Tuple2<>(3, 2));
    }

    @Test
    void shouldFlatMapToIterablePair() {
        assertThat((Stream<Tuple2<Integer, Integer>>) Seq2.seq(List.of(1, 2))
                .flatMapToIterablePair(i -> List.of(new Tuple2<>(i, i), new Tuple2<>(i + 1, i + 1))))
                .hasSize(4)
                .containsExactlyInAnyOrder(new Tuple2<>(1, 1), new Tuple2<>(2, 2),
                        new Tuple2<>(2, 2), new Tuple2<>(3, 3));
    }

}
