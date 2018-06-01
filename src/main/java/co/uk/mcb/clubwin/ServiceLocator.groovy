package co.uk.mcb.clubwin

import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

import java.lang.annotation.Annotation

@Service
@Scope("singleton")
class ServiceLocator implements ApplicationContextAware{
    private static final Logger logger = Logger.getLogger(ServiceLocator)

    private static ApplicationContext springContext

    @Override
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServiceLocator.springContext = applicationContext
    }

    static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException {
        return springContext.getBeansWithAnnotation(annotationType)
    }

    static <T> T getBean(Class<T> requiredType) {
        if (springContext == null) {
            logger.fatal("SpringContext null in ServiceLocator!")
        }
        return springContext.getBean(requiredType)
    }

    static <T> T getBean(Class<T> requiredType, Object... args) {
        return (T) springContext.getBean(StringUtils.uncapitalize(requiredType.getSimpleName()), args)
    }

    static <T> List<T> getBeans(Class<T> requiredType) {
        return new ArrayList<T>(springContext.getBeansOfType(requiredType).values())
    }

    static ApplicationContext getAppContext() {
        return springContext
    }
}
