package com.harhay.poodle.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

@UtilityClass
public class FileUtils {

    @SneakyThrows
    public static String fileToString(String fileName, Class<?> clazz) {
        InputStream inputStream = clazz.getResourceAsStream(fileName);
        return IOUtils.toString(requireNonNull(inputStream), UTF_8);
    }
}
