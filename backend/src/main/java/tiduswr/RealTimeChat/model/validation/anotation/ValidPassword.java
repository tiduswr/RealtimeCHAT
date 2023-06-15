package tiduswr.RealTimeChat.model.validation.anotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import tiduswr.RealTimeChat.model.validation.validator.PasswordConstraintValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE, RECORD_COMPONENT })
@Retention(RUNTIME)
@SuppressWarnings("unused")
public @interface ValidPassword {

    String message() default "Invalid Password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}