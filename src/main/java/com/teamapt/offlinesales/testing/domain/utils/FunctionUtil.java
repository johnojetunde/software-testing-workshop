package com.teamapt.offlinesales.testing.domain.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class FunctionUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public FunctionUtil() {
    }

    public static <T> Stream<T> emptyIfNullStream(Collection<T> list) {
        return list == null ? Stream.empty() : list.stream();
    }

    public static <T> Collection emptyIfNull(Collection<T> list) {
        return list == null ? List.of() : list;
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    static {
        MAPPER.registerModule(new JavaTimeModule());
    }
}
