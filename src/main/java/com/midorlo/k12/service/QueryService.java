package com.midorlo.k12.service;

import com.midorlo.k12.service.filter.Filter;
import com.midorlo.k12.service.filter.RangeFilter;
import com.midorlo.k12.service.filter.StringFilter;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base service for constructing and executing complex queries.
 *
 * @param <ENTITY> the type of the entity which is queried.
 */
@Transactional(readOnly = true)
public abstract class QueryService<ENTITY> {

    /**
     * Helper function to return a specification for filtering on a single field, where equality, and null/non-null
     * conditions are supported.
     *
     * @param filter the individual attribute filter coming from the frontend.
     * @param field  the JPA static metamodel representing the field.
     * @param <X>    The type of the attribute which is filtered.
     * @return a Specification
     */
    protected <X> Specification<ENTITY> buildSpecification(Filter<X> filter, SingularAttribute<? super ENTITY, X> field) {
        return buildSpecification(filter, root -> root.get(field));
    }

    /**
     * Helper function to return a specification for filtering on a single field, where equality, and null/non-null
     * conditions are supported.
     *
     * @param filter            the individual attribute filter coming from the frontend.
     * @param metaclassFunction the function, which navigates from the current entity to a column, for which the
     *                          filter applies.
     * @param <X>               The type of the attribute which is filtered.
     * @return a Specification
     */
    protected <X> Specification<ENTITY> buildSpecification(Filter<X> filter, Function<Root<ENTITY>, Expression<X>> metaclassFunction) {
        if (filter.getEquals() != null) {
            return equalsSpecification(metaclassFunction, filter.getEquals());
        } else if (filter.getIn() != null) {
            return valueIn(metaclassFunction, filter.getIn());
        } else if (filter.getNotIn() != null) {
            return valueNotIn(metaclassFunction, filter.getNotIn());
        } else if (filter.getNotEquals() != null) {
            return notEqualsSpecification(metaclassFunction, filter.getNotEquals());
        } else if (filter.getSpecified() != null) {
            return byFieldSpecified(metaclassFunction, filter.getSpecified());
        }
        return null;
    }

    /**
     * Helper function to return a specification for filtering on a {@link String} field, where equality, containment,
     * and null/non-null conditions are supported.
     *
     * @param filter the individual attribute filter coming from the frontend.
     * @param field  the JPA static metamodel representing the field.
     * @return a Specification
     */
    protected Specification<ENTITY> buildStringSpecification(StringFilter filter, SingularAttribute<? super ENTITY, String> field) {
        return buildSpecification(filter, root -> root.get(field));
    }

    /**
     * Helper function to return a specification for filtering on a {@link String} field, where equality, containment,
     * and null/non-null conditions are supported.
     *
     * @param filter            the individual attribute filter coming from the frontend.
     * @param metaclassFunction lambda, which based on a Root&lt;ENTITY&gt; returns Expression - basically picks a
     *                          column
     * @return a Specification
     */
    protected Specification<ENTITY> buildSpecification(StringFilter filter, Function<Root<ENTITY>, Expression<String>> metaclassFunction) {
        if (filter.getEquals() != null) {
            return equalsSpecification(metaclassFunction, filter.getEquals());
        } else if (filter.getIn() != null) {
            return valueIn(metaclassFunction, filter.getIn());
        } else if (filter.getNotIn() != null) {
            return valueNotIn(metaclassFunction, filter.getNotIn());
        } else if (filter.getContains() != null) {
            return likeUpperSpecification(metaclassFunction, filter.getContains());
        } else if (filter.getDoesNotContain() != null) {
            return doesNotContainSpecification(metaclassFunction, filter.getDoesNotContain());
        } else if (filter.getNotEquals() != null) {
            return notEqualsSpecification(metaclassFunction, filter.getNotEquals());
        } else if (filter.getSpecified() != null) {
            return byFieldSpecified(metaclassFunction, filter.getSpecified());
        }
        return null;
    }

    /**
     * Helper function to return a specification for filtering on a single {@link Comparable}, where equality, less
     * than, greater than and less-than-or-equal-to and greater-than-or-equal-to and null/non-null conditions are
     * supported.
     *
     * @param <X>    The type of the attribute which is filtered.
     * @param filter the individual attribute filter coming from the frontend.
     * @param field  the JPA static metamodel representing the field.
     * @return a Specification
     */
    protected <X extends Comparable<? super X>> Specification<ENTITY> buildRangeSpecification(
        RangeFilter<X> filter,
        SingularAttribute<? super ENTITY, X> field
    ) {
        return buildSpecification(filter, root -> root.get(field));
    }

    /**
     * Helper function to return a specification for filtering on a single {@link Comparable}, where equality, less
     * than, greater than and less-than-or-equal-to and greater-than-or-equal-to and null/non-null conditions are
     * supported.
     *
     * @param <X>               The type of the attribute which is filtered.
     * @param filter            the individual attribute filter coming from the frontend.
     * @param metaclassFunction lambda, which based on a Root&lt;ENTITY&gt; returns Expression - basically picks a
     *                          column
     * @return a Specification
     */
    protected <X extends Comparable<? super X>> Specification<ENTITY> buildSpecification(
        RangeFilter<X> filter,
        Function<Root<ENTITY>, Expression<X>> metaclassFunction
    ) {
        if (filter.getEquals() != null) {
            return equalsSpecification(metaclassFunction, filter.getEquals());
        } else if (filter.getIn() != null) {
            return valueIn(metaclassFunction, filter.getIn());
        }

        Specification<ENTITY> result = Specification.where(null);
        if (filter.getSpecified() != null) {
            result = result.and(byFieldSpecified(metaclassFunction, filter.getSpecified()));
        }
        return getEntitySpecification(
            result,
            filter.getNotEquals() != null,
            notEqualsSpecification(metaclassFunction, filter.getNotEquals()),
            filter.getNotIn() != null,
            valueNotIn(metaclassFunction, filter.getNotIn()),
            filter.getGreaterThan() != null,
            greaterThan(metaclassFunction, filter.getGreaterThan()),
            filter.getGreaterThanOrEqual() != null,
            greaterThanOrEqualTo(metaclassFunction, filter.getGreaterThanOrEqual()),
            filter.getLessThan() != null,
            lessThan(metaclassFunction, filter.getLessThan()),
            filter.getLessThanOrEqual() != null,
            lessThanOrEqualTo(metaclassFunction, filter.getLessThanOrEqual()),
            filter
        );
    }

    private <X extends Comparable<? super X>> Specification<ENTITY> getEntitySpecification(
        Specification<ENTITY> result,
        boolean b,
        Specification<ENTITY> entitySpecification,
        boolean b2,
        Specification<ENTITY> entitySpecification2,
        boolean b3,
        Specification<ENTITY> entitySpecification3,
        boolean b4,
        Specification<ENTITY> entitySpecification4,
        boolean b5,
        Specification<ENTITY> entitySpecification5,
        boolean b6,
        Specification<ENTITY> entitySpecification6,
        RangeFilter<X> filter
    ) {
        if (b) {
            result = result.and(entitySpecification);
        }
        if (b2) {
            result = result.and(entitySpecification2);
        }
        if (b3) {
            result = result.and(entitySpecification3);
        }
        if (b4) {
            result = result.and(entitySpecification4);
        }
        if (b5) {
            result = result.and(entitySpecification5);
        }
        if (b6) {
            result = result.and(entitySpecification6);
        }
        return result;
    }

    /**
     * Helper function to return a specification for filtering on one-to-one or many-to-one reference. Usage:
     * <pre>
     *   Specification&lt;Employee&gt; specByProjectId = buildReferringEntitySpecification(criteria.getProjectId(),
     * Employee_.project, Project_.id);
     *   Specification&lt;Employee&gt; specByProjectName = buildReferringEntitySpecification(criteria.getProjectName(),
     * Employee_.project, Project_.name);
     * </pre>
     *
     * @param filter     the filter object which contains a value, which needs to match or a flag if null is checked.
     * @param reference  the attribute of the static metamodel for the referring entity.
     * @param valueField the attribute of the static metamodel of the referred entity, where the equality should be
     *                   checked.
     * @param <OTHER>    The type of the referenced entity.
     * @param <X>        The type of the attribute which is filtered.
     * @return a Specification
     */
    protected <OTHER, X> Specification<ENTITY> buildReferringEntitySpecification(
        Filter<X> filter,
        SingularAttribute<? super ENTITY, OTHER> reference,
        SingularAttribute<? super OTHER, X> valueField
    ) {
        return buildSpecification(filter, root -> root.get(reference).get(valueField));
    }

    /**
     * Helper function to return a specification for filtering on one-to-many or many-to-many reference. Usage:
     * <pre>
     *   Specification&lt;Employee&gt; specByEmployeeId = buildReferringEntitySpecification(criteria.getEmployeeId(),
     * Project_.employees, Employee_.id);
     *   Specification&lt;Employee&gt; specByEmployeeName = buildReferringEntitySpecification(criteria.getEmployeeName(),
     * Project_.project, Project_.name);
     * </pre>
     *
     * @param filter     the filter object which contains a value, which needs to match or a flag if emptiness is
     *                   checked.
     * @param reference  the attribute of the static metamodel for the referring entity.
     * @param valueField the attribute of the static metamodel of the referred entity, where the equality should be
     *                   checked.
     * @param <OTHER>    The type of the referenced entity.
     * @param <X>        The type of the attribute which is filtered.
     * @return a Specification
     */
    protected <OTHER, X> Specification<ENTITY> buildReferringEntitySpecification(
        Filter<X> filter,
        SetAttribute<ENTITY, OTHER> reference,
        SingularAttribute<OTHER, X> valueField
    ) {
        return buildReferringEntitySpecification(filter, root -> root.join(reference), entity -> entity.get(valueField));
    }

    /**
     * Helper function to return a specification for filtering on one-to-many or many-to-many reference.Usage:<pre>
     *   Specification&lt;Employee&gt; specByEmployeeId = buildReferringEntitySpecification(
     *          criteria.getEmployeeId(),
     *          root -&gt; root.get(Project_.company).join(Company_.employees),
     *          entity -&gt; entity.get(Employee_.id));
     *   Specification&lt;Employee&gt; specByProjectName = buildReferringEntitySpecification(
     *          criteria.getProjectName(),
     *          root -&gt; root.get(Project_.project)
     *          entity -&gt; entity.get(Project_.name));
     * </pre>
     *
     * @param filter           the filter object which contains a value, which needs to match or a flag if emptiness is
     *                         checked.
     * @param functionToEntity the function, which joins he is current entity to the entity set, on which the
     *                         filtering is applied.
     * @param entityToColumn   the function, which of the static metamodel of the referred entity, where the equality
     *                         should be
     *                         checked.
     * @param <OTHER>          The type of the referenced entity.
     * @param <MISC>           The type of the entity which is the last before the OTHER in the chain.
     * @param <X>              The type of the attribute which is filtered.
     * @return a Specification
     */
    protected <OTHER, MISC, X> Specification<ENTITY> buildReferringEntitySpecification(
        Filter<X> filter,
        Function<Root<ENTITY>, SetJoin<MISC, OTHER>> functionToEntity,
        Function<SetJoin<MISC, OTHER>, Expression<X>> entityToColumn
    ) {
        if (filter.getEquals() != null) {
            return equalsSpecification(functionToEntity.andThen(entityToColumn), filter.getEquals());
        } else if (filter.getSpecified() != null) {
            // Interestingly, 'functionToEntity' doesn't work, we need the longer lambda formula
            return byFieldSpecified(functionToEntity::apply, filter.getSpecified());
        }
        return null;
    }

    /**
     * Helper function to return a specification for filtering on one-to-many or many-to-many reference.Where
     * equality, less
     * than, greater than and less-than-or-equal-to and greater-than-or-equal-to and null/non-null conditions are
     * supported. Usage:
     * <pre>
     *   Specification&lt;Employee&gt; specByEmployeeId = buildReferringEntitySpecification(criteria.getEmployeeId(),
     * Project_.employees, Employee_.id);
     *   Specification&lt;Employee&gt; specByEmployeeName = buildReferringEntitySpecification(criteria.getEmployeeName(),
     * Project_.project, Project_.name);
     * </pre>
     *
     * @param <X>        The type of the attribute which is filtered.
     * @param filter     the filter object which contains a value, which needs to match or a flag if emptiness is
     *                   checked.
     * @param reference  the attribute of the static metamodel for the referring entity.
     * @param valueField the attribute of the static metamodel of the referred entity, where the equality should be
     *                   checked.
     * @param <OTHER>    The type of the referenced entity.
     * @return a Specification
     */
    protected <OTHER, X extends Comparable<? super X>> Specification<ENTITY> buildReferringEntitySpecification(
        final RangeFilter<X> filter,
        final SetAttribute<ENTITY, OTHER> reference,
        final SingularAttribute<OTHER, X> valueField
    ) {
        return buildReferringEntitySpecification(filter, root -> root.join(reference), entity -> entity.get(valueField));
    }

    /**
     * Helper function to return a specification for filtering on one-to-many or many-to-many reference.Where
     * equality, less
     * than, greater than and less-than-or-equal-to and greater-than-or-equal-to and null/non-null conditions are
     * supported. Usage:
     * <pre><code>
     *   Specification&lt;Employee&gt; specByEmployeeId = buildReferringEntitySpecification(
     *          criteria.getEmployeeId(),
     *          root -&gt; root.get(Project_.company).join(Company_.employees),
     *          entity -&gt; entity.get(Employee_.id));
     *   Specification&lt;Employee&gt; specByProjectName = buildReferringEntitySpecification(
     *          criteria.getProjectName(),
     *          root -&gt; root.get(Project_.project)
     *          entity -&gt; entity.get(Project_.name));
     * </code>
     * </pre>
     *
     * @param <X>              The type of the attribute which is filtered.
     * @param filter           the filter object which contains a value, which needs to match or a flag if emptiness is
     *                         checked.
     * @param functionToEntity the function, which joins he is current entity to the entity set, on which the
     *                         filtering is applied.
     * @param entityToColumn   the function, which of the static metamodel of the referred entity, where the equality
     *                         should be
     *                         checked.
     * @param <OTHER>          The type of the referenced entity.
     * @param <MISC>           The type of the entity which is the last before the OTHER in the chain.
     * @return a Specification
     */
    protected <OTHER, MISC, X extends Comparable<? super X>> Specification<ENTITY> buildReferringEntitySpecification(
        final RangeFilter<X> filter,
        Function<Root<ENTITY>, SetJoin<MISC, OTHER>> functionToEntity,
        Function<SetJoin<MISC, OTHER>, Expression<X>> entityToColumn
    ) {
        Function<Root<ENTITY>, Expression<X>> fused = functionToEntity.andThen(entityToColumn);
        if (filter.getEquals() != null) {
            return equalsSpecification(fused, filter.getEquals());
        } else if (filter.getIn() != null) {
            return valueIn(fused, filter.getIn());
        }
        Specification<ENTITY> result = Specification.where(null);
        if (filter.getSpecified() != null) {
            // Interestingly, 'functionToEntity' doesn't work, we need the longer lambda formula
            result = result.and(byFieldSpecified(functionToEntity::apply, filter.getSpecified()));
        }
        return getEntitySpecification(
            result,
            filter.getNotEquals() != null,
            notEqualsSpecification(fused, filter.getNotEquals()),
            filter.getNotIn() != null,
            valueNotIn(fused, filter.getNotIn()),
            filter.getGreaterThan() != null,
            greaterThan(fused, filter.getGreaterThan()),
            filter.getGreaterThanOrEqual() != null,
            greaterThanOrEqualTo(fused, filter.getGreaterThanOrEqual()),
            filter.getLessThan() != null,
            lessThan(fused, filter.getLessThan()),
            filter.getLessThanOrEqual() != null,
            lessThanOrEqualTo(fused, filter.getLessThanOrEqual()),
            filter
        );
    }

    /**
     * Generic method, which based on a Root&lt;ENTITY&gt; returns an Expression which type is the same as the given
     * 'value' type.
     *
     * @param metaclassFunction function which returns the column which is used for filtering.
     * @param value             the actual value to filter for.
     * @param <X>               The type of the attribute which is filtered.
     * @return a Specification.
     */
    protected <X> Specification<ENTITY> equalsSpecification(Function<Root<ENTITY>, Expression<X>> metaclassFunction, final X value) {
        return (root, query, builder) -> builder.equal(metaclassFunction.apply(root), value);
    }

    /**
     * Generic method, which based on a Root&lt;ENTITY&gt; returns an Expression which type is the same as the given
     * 'value' type.
     *
     * @param metaclassFunction function which returns the column which is used for filtering.
     * @param value             the actual value to exclude for.
     * @param <X>               The type of the attribute which is filtered.
     * @return a Specification.
     */
    protected <X> Specification<ENTITY> notEqualsSpecification(Function<Root<ENTITY>, Expression<X>> metaclassFunction, final X value) {
        return (root, query, builder) -> builder.not(builder.equal(metaclassFunction.apply(root), value));
    }

    /**
     * <p>likeUpperSpecification.</p>
     *
     * @param metaclassFunction a {@link Function} object.
     * @param value             a {@link String} object.
     * @return a {@link Specification} object.
     */
    protected Specification<ENTITY> likeUpperSpecification(
        Function<Root<ENTITY>, Expression<String>> metaclassFunction,
        final String value
    ) {
        return (root, query, builder) -> builder.like(builder.upper(metaclassFunction.apply(root)), wrapLikeQuery(value));
    }

    /**
     * <p>doesNotContainSpecification.</p>
     *
     * @param metaclassFunction a {@link Function} object.
     * @param value             a {@link String} object.
     * @return a {@link Specification} object.
     */
    protected Specification<ENTITY> doesNotContainSpecification(
        Function<Root<ENTITY>, Expression<String>> metaclassFunction,
        final String value
    ) {
        return (root, query, builder) -> builder.not(builder.like(builder.upper(metaclassFunction.apply(root)), wrapLikeQuery(value)));
    }

    /**
     * <p>byFieldSpecified.</p>
     *
     * @param metaclassFunction a {@link Function} object.
     * @param specified         a boolean.
     * @param <X>               a X object.
     * @return a {@link Specification} object.
     */
    protected <X> Specification<ENTITY> byFieldSpecified(Function<Root<ENTITY>, Expression<X>> metaclassFunction, final boolean specified) {
        return specified
            ? (root, query, builder) -> builder.isNotNull(metaclassFunction.apply(root))
            : (root, query, builder) -> builder.isNull(metaclassFunction.apply(root));
    }

    /**
     * <p>byFieldEmptiness.</p>
     *
     * @param metaclassFunction a {@link Function} object.
     * @param specified         a boolean.
     * @param <X>               a X object.
     * @return a {@link Specification} object.
     */
    protected <X> Specification<ENTITY> byFieldEmptiness(
        Function<Root<ENTITY>, Expression<Set<X>>> metaclassFunction,
        final boolean specified
    ) {
        return specified
            ? (root, query, builder) -> builder.isNotEmpty(metaclassFunction.apply(root))
            : (root, query, builder) -> builder.isEmpty(metaclassFunction.apply(root));
    }

    /**
     * <p>valueIn.</p>
     *
     * @param metaclassFunction a {@link Function} object.
     * @param values            a {@link Collection} object.
     * @param <X>               a X object.
     * @return a {@link Specification} object.
     */
    protected <X> Specification<ENTITY> valueIn(Function<Root<ENTITY>, Expression<X>> metaclassFunction, final Collection<X> values) {
        return (root, query, builder) -> {
            In<X> in = builder.in(metaclassFunction.apply(root));
            for (X value : values) {
                in = in.value(value);
            }
            return in;
        };
    }

    /**
     * <p>valueNotIn.</p>
     *
     * @param metaclassFunction a {@link Function} object.
     * @param values            a {@link Collection} object.
     * @param <X>               a X object.
     * @return a {@link Specification} object.
     */
    protected <X> Specification<ENTITY> valueNotIn(Function<Root<ENTITY>, Expression<X>> metaclassFunction, final Collection<X> values) {
        return (root, query, builder) -> {
            In<X> in = builder.in(metaclassFunction.apply(root));
            for (X value : values) {
                in = in.value(value);
            }
            return builder.not(in);
        };
    }

    /**
     * <p>greaterThanOrEqualTo.</p>
     *
     * @param metaclassFunction a {@link Function} object.
     * @param value             a X object.
     * @param <X>               a X object.
     * @return a {@link Specification} object.
     */
    protected <X extends Comparable<? super X>> Specification<ENTITY> greaterThanOrEqualTo(
        Function<Root<ENTITY>, Expression<X>> metaclassFunction,
        final X value
    ) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(metaclassFunction.apply(root), value);
    }

    /**
     * <p>greaterThan.</p>
     *
     * @param metaclassFunction a {@link Function} object.
     * @param value             a X object.
     * @param <X>               a X object.
     * @return a {@link Specification} object.
     */
    protected <X extends Comparable<? super X>> Specification<ENTITY> greaterThan(
        Function<Root<ENTITY>, Expression<X>> metaclassFunction,
        final X value
    ) {
        return (root, query, builder) -> builder.greaterThan(metaclassFunction.apply(root), value);
    }

    /**
     * <p>lessThanOrEqualTo.</p>
     *
     * @param metaclassFunction a {@link Function} object.
     * @param value             a X object.
     * @param <X>               a X object.
     * @return a {@link Specification} object.
     */
    protected <X extends Comparable<? super X>> Specification<ENTITY> lessThanOrEqualTo(
        Function<Root<ENTITY>, Expression<X>> metaclassFunction,
        final X value
    ) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(metaclassFunction.apply(root), value);
    }

    /**
     * <p>lessThan.</p>
     *
     * @param metaclassFunction a {@link Function} object.
     * @param value             a X object.
     * @param <X>               a X object.
     * @return a {@link Specification} object.
     */
    protected <X extends Comparable<? super X>> Specification<ENTITY> lessThan(
        Function<Root<ENTITY>, Expression<X>> metaclassFunction,
        final X value
    ) {
        return (root, query, builder) -> builder.lessThan(metaclassFunction.apply(root), value);
    }

    /**
     * <p>wrapLikeQuery.</p>
     *
     * @param txt a {@link String} object.
     * @return a {@link String} object.
     */
    protected String wrapLikeQuery(String txt) {
        return "%" + txt.toUpperCase() + '%';
    }

    /**
     * <p>distinct.</p>
     *
     * @param distinct a boolean.
     * @return a {@link Specification} object.
     */
    protected Specification<ENTITY> distinct(boolean distinct) {
        return (root, query, cb) -> {
            query.distinct(distinct);
            return null;
        };
    }
}
