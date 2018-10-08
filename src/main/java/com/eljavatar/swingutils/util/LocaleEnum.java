/*
 * Copyright 2018 Andres.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eljavatar.swingutils.util;

import java.util.Locale;



/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public enum LocaleEnum {
    
    DEFAULT("DEFAULT"),
    ENGLISH("ENGLISH"),
    FRENCH("FRENCH"),
    GERMAN("GERMAN"),
    ITALIAN("ITALIAN"),
    JAPANESE("JAPANESE"),
    KOREAN("KOREAN"),
    CHINESE("CHINESE"),
    SIMPLIFIED_CHINESE("SIMPLIFIED_CHINESE"),
    TRADITIONAL_CHINESE("TRADITIONAL_CHINESE"),
    FRANCE("FRANCE"),
    GERMANY("GERMANY"),
    ITALY("ITALY"),
    JAPAN("JAPAN"),
    KOREA("KOREA"),
    CHINA("CHINA"),
    PRC("PRC"),
    TAIWAN("TAIWAN"),
    UK("UK"),
    US("US"),
    CANADA("CANADA"),
    CANADA_FRENCH("CANADA_FRENCH"),
    ROOT("ROOT");
    
    private final Locale locale;
    
    private LocaleEnum(String name) {
        this.locale = getLocaleByName(name);
    }

    public Locale getLocale() {
        return locale;
    }
    
    private Locale getLocaleByName(String name) {
        switch (name) {
            case "DEFAULT":
                return Locale.getDefault();
            case "ENGLISH":
                return Locale.ENGLISH;
            case "FRENCH":
                return Locale.FRENCH;
            case "GERMAN":
                return Locale.GERMAN;
            case "ITALIAN":
                return Locale.ITALIAN;
            case "JAPANESE":
                return Locale.JAPANESE;
            case "KOREAN":
                return Locale.KOREAN;
            case "CHINESE":
                return Locale.CHINESE;
            case "SIMPLIFIED_CHINESE":
                return Locale.SIMPLIFIED_CHINESE;
            case "TRADITIONAL_CHINESE":
                return Locale.TRADITIONAL_CHINESE;
            case "FRANCE":
                return Locale.FRANCE;
            case "GERMANY":
                return Locale.GERMANY;
            case "ITALY":
                return Locale.ITALY;
            case "JAPAN":
                return Locale.JAPAN;
            case "KOREA":
                return Locale.KOREA;
            case "CHINA":
                return Locale.CHINA;
            case "PRC":
                return Locale.PRC;
            case "TAIWAN":
                return Locale.TAIWAN;
            case "UK":
                return Locale.UK;
            case "US":
                return Locale.US;
            case "CANADA":
                return Locale.CANADA;
            case "CANADA_FRENCH":
                return Locale.CANADA_FRENCH;
            case "ROOT":
                return Locale.ROOT;
            default:
                throw new AssertionError();
        }
    }
    
}
