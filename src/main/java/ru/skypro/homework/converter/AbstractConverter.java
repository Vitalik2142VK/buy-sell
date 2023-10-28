package ru.skypro.homework.converter;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractConverter<IN, OUT> implements Converter<IN, OUT> {

    public abstract OUT apply(IN in);

    @Override
    public OUT convert(IN in) {
        if (in == null) {
            return null;
        }
        return apply(in);
    }

    @Override
    public List<OUT> convertAll(List<IN> list) {
        if (list == null) {
            return List.of();
        }
        return list.stream().map(item -> apply(item)).collect(Collectors.toList());
    }
}
