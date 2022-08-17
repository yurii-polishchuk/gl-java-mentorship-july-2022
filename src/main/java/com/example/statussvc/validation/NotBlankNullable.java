package com.example.statussvc.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Objects;

/**
 * The annotated element must contain at least one non-whitespace character. Accepts {@link CharSequence}.
 */
@Documented
@Constraint(validatedBy = NotBlankNullable.NotBlankNullableValidator.class)
@Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankNullable {

    String message() default "must contain at least one non-whitespace character";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class NotBlankNullableValidator implements ConstraintValidator<NotBlankNullable, CharSequence> {

        @Override
        public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
            return Objects.isNull(value) || StringUtils.isNotBlank(value);
        }

    }

}
