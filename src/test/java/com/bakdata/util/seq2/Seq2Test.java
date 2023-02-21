/*
 * MIT License
 *
 * Copyright (c) 2023 bakdata
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bakdata.util.seq2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
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

}
