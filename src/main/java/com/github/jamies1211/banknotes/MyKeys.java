package com.github.jamies1211.banknotes;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.TypeTokens;

public class MyKeys {
    public MyKeys() {}
    static void dummy() {} // invoke static constructor
    public static final Key<Value<Double>> BANK_NOTE_VALUE;

    static {
        BANK_NOTE_VALUE = Key.builder()
                .type(TypeTokens.DOUBLE_VALUE_TOKEN)
                .id("banknotes:banknotevalue")
                .name("Standard Value")
                .query(DataQuery.of('.', "banknote.value"))
                .build();
    }
}
