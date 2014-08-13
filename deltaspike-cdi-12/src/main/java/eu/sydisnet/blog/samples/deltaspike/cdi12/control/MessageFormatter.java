package eu.sydisnet.blog.samples.deltaspike.cdi12.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import java.lang.invoke.MethodHandles;

/**
 * Component in charge of formatting a message.
 *
 * @author sydisnet
 * @version 1.0.0
 */
@Dependent
public class MessageFormatter {

    /**
     * Static Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Method called by the CDI container when the bean is retrieved from
     * {@link javax.enterprise.inject.spi.BeanManager}. See the JSR 250 Callback {@link javax.annotation.PostConstruct}
     * annotation.
     */
    @PostConstruct
    void initBean() {
        LOG.info(String.format("##### %s started !", MethodHandles.lookup().lookupClass().getSimpleName()));
    }

    /**
     * Method called by the CDI container before the destruction of the bean from the
     * {@link javax.enterprise.inject.spi.BeanManager}. See the JSR 250 Callback {@link javax.annotation.PreDestroy}
     * annotation.
     */
    @PreDestroy
    void tearDown() {
        LOG.info(String.format("##### %s stopped !", MethodHandles.lookup().lookupClass().getSimpleName()));
    }

    /**
     * Service in charge of formatting a template message.
     *
     * @param template the message template to process
     * @param param the element to add to the message template
     * @return the processed message
     */
    public String format(final String template, final String param) {
        String result = String.format(template, param);

        return result;
    }
}
