package com.encurtador.url.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlValidator {

    private static final String HTTPS_URL_REGEX = "^https:\\/\\/([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(\\/[\\w\\-\\.~!$&'()*+,;=:@%]*)*\\/?$";

    private static final Pattern URL_PATTERN = Pattern.compile(HTTPS_URL_REGEX);

    /**
     * Valida se a string fornecida corresponde ao padrão de URL HTTPS definido.
     *
     * @param url A string que será validada.
     * @return true se a string for uma URL HTTPS válida, false caso contrário.
     */
    public static boolean isValidHttpsUrl(String url) {
        if (url == null) {
            return false;
        }
        Matcher matcher = URL_PATTERN.matcher(url);
        return matcher.matches();
    }
}
