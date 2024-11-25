package com.xalpol12.auctionportal.utils;

import java.util.UUID;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * Converts from a String to a {@link java.util.UUID}.
 *
 * @author Dawid Stawiszyński
 * @since 3.2
 * @see UUID#fromString
 */
public class StringToUUIDConverter implements Converter<String, UUID> {

    @Override
    @Nullable
    public UUID convert(String source) {
        return (StringUtils.hasText(source) ? UUID.fromString(source.trim()) : null);
    }

}