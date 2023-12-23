package bleuauction.bleuauction_be.server.config.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Service;

/** ComponentService는 비즈니스를 처리해야 하며, 또는 타 서비스 Layer를 참조해야 할때 선언한다. */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface ComponentService {}
