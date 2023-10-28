package ru.skypro.homework.converter;

import java.util.List;

public interface Converter<IN,OUT> {

    OUT convert (IN in);

    List<OUT> convertAll(List<IN> list);
}
