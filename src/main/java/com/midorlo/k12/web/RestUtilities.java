package com.midorlo.k12.web;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Utility class for all things tedious when creating rest apis.
 *
 * <p>
 * Pagination uses the same principles as the <a href="https://developer.github.com/v3/#pagination">GitHub API</a>,
 * and follow <a href="http://tools.ietf.org/html/rfc5988">RFC 5988 (Link header)</a>.
 */
@SuppressWarnings("UastIncorrectHttpHeaderInspection")
@Slf4j
@UtilityClass
public final class RestUtilities {

    private static final String HEADER_X_TOTAL_COUNT = "X-Total-Count";
    private static final String HEADER_LINK_FORMAT = "<{0}>; rel=\"{1}\"";

    /**
     * Generate pagination headers for a Spring Data {@link Page} object.
     *
     * @param uriBuilder The URI builder.
     * @param page       The page.
     * @param <T>        The type of object.
     * @return http header.
     */
    public static <T> HttpHeaders generatePaginationHttpHeaders(UriComponentsBuilder uriBuilder, Page<T> page) {
        int pageNumber = page.getNumber();
        int pageSize = page.getSize();
        StringBuilder link = new StringBuilder();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_X_TOTAL_COUNT, Long.toString(page.getTotalElements()));

        if (pageNumber < page.getTotalPages() - 1) {
            link.append(prepareLink(uriBuilder, pageNumber + 1, pageSize, "next")).append(",");
        }
        if (pageNumber > 0) {
            link.append(prepareLink(uriBuilder, pageNumber - 1, pageSize, "prev")).append(",");
        }
        link
            .append(prepareLink(uriBuilder, page.getTotalPages() - 1, pageSize, "last"))
            .append(",")
            .append(prepareLink(uriBuilder, 0, pageSize, "first"));
        headers.add(HttpHeaders.LINK, link.toString());
        return headers;
    }

    private static String prepareLink(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize, String relType) {
        return MessageFormat.format(HEADER_LINK_FORMAT, preparePageUri(uriBuilder, pageNumber, pageSize), relType);
    }

    private static String preparePageUri(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize) {
        return uriBuilder
            .replaceQueryParam("page", Integer.toString(pageNumber))
            .replaceQueryParam("size", Integer.toString(pageSize))
            .toUriString()
            .replace(",", "%2C")
            .replace(";", "%3B");
    }

    /**
     * Create a {@link Page} from a {@link List} of objects
     *
     * @param list     list of objects
     * @param pageable pagination information.
     * @param <T>      type of object
     * @return page containing objects, and attributes set according to pageable
     * @throws IllegalArgumentException - if list is null
     */
    public static <T> Page<T> createPageFromList(List<T> list, Pageable pageable) {
        if (list == null) {
            throw new IllegalArgumentException("To create a Page, the list mustn't be null!");
        }

        int startOfPage = pageable.getPageNumber() * pageable.getPageSize();
        if (startOfPage > list.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        int endOfPage = Math.min(startOfPage + pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(startOfPage, endOfPage), pageable, list.size());
    }

    /**
     * <p>createAlert.</p>
     *
     * @param appName a {@link String} object.
     * @param message a {@link String} object.
     * @param param   a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createAlert(String appName, String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + appName + "-alert", message);
        headers.add("X-" + appName + "-params", URLEncoder.encode(param, StandardCharsets.UTF_8));
        return headers;
    }

    /**
     * <p>createEntityCreationAlert.</p>
     *
     * @param appName    a {@link String} object.
     * @param entityName a {@link String} object.
     * @param param      a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createEntityCreationAlert(String appName, String entityName, String param) {
        return createEntityCreationAlert(appName, false, entityName, param);
    }

    /**
     * <p>createEntityCreationAlert.</p>
     *
     * @param appName    a {@link String} object.
     * @param entityName a {@link String} object.
     * @param param      a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createLocalizedEntityCreationAlert(String appName, String entityName, String param) {
        return createEntityCreationAlert(appName, true, entityName, param);
    }

    private static HttpHeaders createEntityCreationAlert(String appName, boolean enableTranslation, String entityName, String param) {
        String message = enableTranslation
            ? appName + "." + entityName + ".created"
            : "A new " + entityName + " is created with identifier " + param;
        return createAlert(appName, message, param);
    }

    public static HttpHeaders createEntityUpdateAlert(String appName, String entityName, String param) {
        return createEntityUpdateAlert(appName, false, entityName, param);
    }

    public static HttpHeaders createLocalizedEntityUpdateAlert(String appName, String entityName, String param) {
        return createEntityUpdateAlert(appName, true, entityName, param);
    }

    private static HttpHeaders createEntityUpdateAlert(String appName, boolean enableTranslation, String entityName, String param) {
        String message = enableTranslation
            ? appName + "." + entityName + ".updated"
            : "A " + entityName + " is updated with identifier " + param;
        return createAlert(appName, message, param);
    }

    public static HttpHeaders createEntityDeletionAlert(String appName, String entityName, String param) {
        return createEntityDeletionAlert(appName, false, entityName, param);
    }

    public static HttpHeaders createLocalizedEntityDeletionAlert(String appName, String entityName, String param) {
        return createEntityDeletionAlert(appName, true, entityName, param);
    }

    private static HttpHeaders createEntityDeletionAlert(String appName, boolean enableTranslation, String entityName, String param) {
        String message = enableTranslation
            ? appName + "." + entityName + ".deleted"
            : "A " + entityName + " is deleted with identifier " + param;
        return createAlert(appName, message, param);
    }

    public static HttpHeaders createFailureAlert(String appName, String entityName, String errorKey, String defaultMessage) {
        return createFailureAlert(appName, false, entityName, errorKey, defaultMessage);
    }

    public static HttpHeaders createLocalizedFailureAlert(String appName, String entityName, String errorKey, String defaultMessage) {
        return createFailureAlert(appName, true, entityName, errorKey, defaultMessage);
    }

    private static HttpHeaders createFailureAlert(
        String appName,
        boolean enableTranslation,
        String entityName,
        String errorKey,
        String defaultMessage
    ) {
        log.error("Entity processing failed, {}", defaultMessage);

        String message = enableTranslation ? "error." + errorKey : defaultMessage;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + appName + "-error", message);
        headers.add("X-" + appName + "-params", entityName);
        return headers;
    }

    /**
     * Wrap the optional into a {@link ResponseEntity} with an {@link HttpStatus#OK} status, or if it's empty, it
     * returns a {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND}.
     *
     * @param <X>      type of the response
     * @param response response to return if present
     * @return response containing {@code maybeResponse} if present or {@link HttpStatus#NOT_FOUND}
     */
    public static <X> ResponseEntity<X> wrapOrNotFound(X response) {
        return wrapOrNotFound(response, null);
    }

    /**
     * Wrap the optional into a {@link ResponseEntity} with an {@link HttpStatus#OK} status with the headers, or if it's
     * empty, throws a {@link ResponseStatusException} with status {@link HttpStatus#NOT_FOUND}.
     *
     * @param <X>      type of the response
     * @param response response to return if present
     * @param header   headers to be added to the response
     * @return response containing {@code maybeResponse} if present
     */
    public static <X> ResponseEntity<X> wrapOrNotFound(@NonNull X response, HttpHeaders header) {
        return ResponseEntity.ok().headers(header).body(response);
    }
}
