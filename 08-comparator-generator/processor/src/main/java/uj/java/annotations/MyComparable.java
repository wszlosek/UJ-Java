package uj.java.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation indicating class, for which fields comparator should be generated.
 * Private fields are not being compared. Only simple types are considered; no objects and no arrays included.
 * For ordering fields, use {@link ComparePriority} annotation.
 */
@Target(ElementType.TYPE)
public @interface MyComparable {
}
