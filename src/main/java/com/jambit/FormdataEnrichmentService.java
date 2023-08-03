package com.jambit;

import io.micronaut.core.annotation.Nullable;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util class to add some JSON snippet to the given string.
 */
public enum FormdataEnrichmentService {
    ;

    static final Pattern PATTERN = Pattern.compile(".*\"user_data\"[\\s\\v]*:[\\s\\v]*\\{");
    static final String TEMPLATE =
            """
                        
                    "client_ip_address" : "%s",
                    "client_user_agent" : "%s",""";

    /**
     * Takes the given data string and inserts the two attributes <b><i>client_ip_address</i></b> and
     * <b><i>client_user_agent</i></b> with the given values.
     * @param data The data string expected to be a valid json string containing an attribute
     * <b><i>user_data</i></b>.
     * @param userAgent The value to be inserted into the data string as attribute <b><i>client_user_agent</i></b>
     * @param userAddress The value to be inserted into the data string as attribute <b><i>client_ip_address</i></b>
     * @return The enriched data string, or the unmodified data string if the attribute <b><i>user_data</i></b>
     * could not be found.
     */
    @Nullable
    static String getEnrichedData(
            final String data,
            final String userAgent,
            final String userAddress
    ) {
        return Optional.ofNullable(data)
                .map(PATTERN::matcher)
                .filter(Matcher::find)
                .map(Matcher::end)
                .map(index -> data.substring(0, index)
                        + TEMPLATE.formatted(userAddress, userAgent)
                        + data.substring(index))
                .orElse(data);
    }
}