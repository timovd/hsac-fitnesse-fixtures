package nl.hsac.fitnesse.fixture.util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper to remove wiki formatting from strings.
 */
public class HtmlCleaner {
    private static final Pattern PATTERN = Pattern.compile("<a href=\"(.*?)\">(.*?)</a>(.*)", Pattern.CASE_INSENSITIVE);
    private static final Pattern PRE_FORMATTED_PATTERN = Pattern.compile("<pre>\\s*(.*?)\\s*</pre>", Pattern.DOTALL);

    /**
     * Gets a URL from a wiki page value.
     * @param htmlLink link as present on wiki page.
     * @return address the link points to (if it is an 'a'), the original link otherwise.
     */
    public String getUrl(String htmlLink) {
        String result = htmlLink;
        Matcher matcher = PATTERN.matcher(htmlLink);
        if (matcher.matches()) {
            result = matcher.group(1) + matcher.group(3);
        }
        return result;
    }

    /**
     * Removes result of wiki formatting (for e.g. email addresses) if needed.
     * @param rawValue value as received from Fitnesse.
     * @return rawValue if it was just text, cleaned version if it was not.
     */
    public String cleanupValue(String rawValue) {
        String result = null;
        if (rawValue != null) {
            Matcher matcher = PATTERN.matcher(rawValue);
            if (matcher.matches()) {
                result = matcher.group(2) + matcher.group(3);
            } else {
                result = cleanupPreFormatted(rawValue);
            }
        }
        return result;
    }

    /**
     * Removes HTML preformatting (if any).
     * @param value value (possibly pre-formatted)
     * @return value without HTML preformatting.
     */
    public String cleanupPreFormatted(String value) {
        String result = value;
        if (value != null) {
            Matcher matcher = PRE_FORMATTED_PATTERN.matcher(value);
            if (matcher.matches()) {
                String escapedBody = matcher.group(1);
                result = StringEscapeUtils.unescapeHtml4(escapedBody);
            }
        }
        return result;
    }
}