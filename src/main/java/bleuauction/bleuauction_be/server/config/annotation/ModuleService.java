package bleuauction.bleuauction_be.server.config.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Service;

/** ModuleService는 단순히 CRUD만 처리하는 Service Layer로 Repository만 주입이 가능하다. */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface ModuleService {}
