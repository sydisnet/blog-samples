package eu.sydisnet.blog.samples.deltaspike.cdi12.boundary;

import eu.sydisnet.blog.samples.deltaspike.cdi12.control.MessageFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.lang.invoke.MethodHandles;

/**
 * Component whose responsibility is to send to the client a welcome message.
 *
 * The scope dependent means there are more than one bean when retrieved from CDI.
 *
 * @author      sydisnet
 * @version     1.0.0
 */
@Dependent
public class DependentHelloService {

    /**
     * Static Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Constant used by the service when userName is not defined by the client.
     */
    private static final String UNKNOWN_USERNAME = "M. John Doe";

    /**
     * The default message template assigned when the bean is loaded.
     */
    private String defaultMessage;

    /**
     * Delegate service.
     */
    @Inject
    private MessageFormatter messageFormatter;

    /**
     * Method called by the CDI container when the bean is retrieved from
     * {@link javax.enterprise.inject.spi.BeanManager}. See the JSR 250 Callback {@link javax.annotation.PostConstruct}
     * annotation.
     */
    @PostConstruct
    void initBean() {
        defaultMessage = "Welcome %s !";

        LOG.info(String.format("##### %s stopped !", MethodHandles.lookup().lookupClass().getSimpleName()));
    }

    /**
     * Method called by the CDI container before the destruction of the bean from the
     * {@link javax.enterprise.inject.spi.BeanManager}. See the JSR 250 Callback {@link javax.annotation.PreDestroy}
     * annotation.
     *
     * This method will be never called since the bean has the scope {@link javax.enterprise.context.Dependent}.
     */
    @PreDestroy
    void tearDown() {
        LOG.info(String.format("##### %s stopped !", MethodHandles.lookup().lookupClass().getSimpleName()));
    }

    /**
     * Service called by <strong>client-tier</strong> in order to send a welcome message.
     *
     * @param userName the name of the user
     * @return the welcome message
     */
    public String sayHello(final String userName) {
        if (userName == null || userName.isEmpty()) {
            return messageFormatter.format(defaultMessage, UNKNOWN_USERNAME);
        }

        return messageFormatter.format(defaultMessage, userName);
    }
}
