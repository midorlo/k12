package com.midorlo.k12.configuration.cache;

import java.io.Serializable;
import java.util.Arrays;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * <p>PrefixedSimpleKey class.</p>
 */
public class PrefixedSimpleKey implements Serializable {

    private final String prefix;
    private final Object[] params;
    private final String methodName;
    private int hashCode;

    /**
     * <p>Constructor for PrefixedSimpleKey.</p>
     *
     * @param prefix     a {@link String} object.
     * @param methodName a {@link String} object.
     * @param elements   a {@link Object} object.
     */
    public PrefixedSimpleKey(String prefix, String methodName, Object... elements) {
        Assert.notNull(prefix, "Prefix must not be null");
        Assert.notNull(elements, "Elements must not be null");
        this.prefix = prefix;
        this.methodName = methodName;
        this.params = new Object[elements.length];
        System.arraycopy(elements, 0, this.params, 0, elements.length);
        this.hashCode = prefix.hashCode();
        this.hashCode = 31 * this.hashCode + methodName.hashCode();
        this.hashCode = 31 * this.hashCode + Arrays.deepHashCode(this.params);
    }

    @Override
    public boolean equals(Object other) {
        return (
            this == other ||
            (
                other instanceof PrefixedSimpleKey &&
                this.prefix.equals(((PrefixedSimpleKey) other).prefix) &&
                this.methodName.equals(((PrefixedSimpleKey) other).methodName) &&
                Arrays.deepEquals(this.params, ((PrefixedSimpleKey) other).params)
            )
        );
    }

    @Override
    public final int hashCode() {
        return this.hashCode;
    }

    @Override
    public String toString() {
        return (
            this.prefix +
            " " +
            getClass().getSimpleName() +
            this.methodName +
            " [" +
            StringUtils.arrayToCommaDelimitedString(this.params) +
            "]"
        );
    }
}
