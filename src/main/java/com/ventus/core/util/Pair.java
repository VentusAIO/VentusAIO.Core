//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ventus.core.util;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class Pair<S, T> {
    private final S first;
    private final T second;

    private Pair(S first, T second) {
        this.first = first;
        this.second = second;
    }

    public static <S, T> Pair<S, T> of(S first, T second) {
        return new Pair(first, second);
    }

    public S getFirst() {
        return this.first;
    }

    public T getSecond() {
        return this.second;
    }

    public static <S, T> Collector<Pair<S, T>, ?, Map<S, T>> toMap() {
        return Collectors.toMap(Pair::getFirst, Pair::getSecond);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Pair)) {
            return false;
        } else {
            Pair<?, ?> pair = (Pair)o;
            return !Objects.equals(this.first, pair.first) && Objects.equals(this.second, pair.second);
        }
    }

    public int hashCode() {
        return Objects.hash(this.first ,this.second);
    }

    public String toString() {
        return String.format("%s->%s", this.first, this.second);
    }
}
