package ru.spbstu.vadim01er.bst;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Bst<Key extends Comparable<Key>, Value> {

    default boolean containsKey(@NotNull Key key) {
        return get(key) != null;
    }

    void put(@NotNull Key key, @NotNull Value value);

    @Nullable
    Value get(@NotNull Key key);

    @Nullable
    Value remove(@NotNull Key key);

    @Nullable
    Key min();

    @Nullable
    Value minValue();

    @Nullable
    Key max();

    @Nullable
    Value maxValue();

    @Nullable
    Key floor(@NotNull Key key);

    @Nullable
    Key ceil(@NotNull Key key);

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

}
