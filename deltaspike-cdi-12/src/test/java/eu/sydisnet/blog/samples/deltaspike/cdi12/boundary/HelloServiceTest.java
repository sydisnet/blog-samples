package eu.sydisnet.blog.samples.deltaspike.cdi12.boundary;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

import static org.hamcrest.Matchers.*;

/**
 * Tests explaining CDI 1.2 and the pseudo-support for {@link javax.inject.Singleton} annotation.
 *
 * @author sydisnet
 * @since 1.0.0
 */
public class HelloServiceTest {

    static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * The CDI Container.
     */
    CdiContainer cdiContainer;

    /**
     * The context controller associated with this instance of {@link org.apache.deltaspike.cdise.api.CdiContainer}.
     */
    ContextControl contextControl;


    @Before
    public void setUp() throws Exception {
        LOG.info("****************************** HelloServiceTest::setUp() ******************************");

        // Starts the CDI Container
        cdiContainer = CdiContainerLoader.getCdiContainer();
        Assert.assertThat("The CDI Container should be correctly initialized !", cdiContainer, notNullValue());
        cdiContainer.boot();

        // Gets the Context Controller, then starts all contexts
        contextControl = cdiContainer.getContextControl();
        Assert.assertThat(
            "The DeltaSpike Contrext Controller should be correctly retrieved !",
            contextControl, notNullValue()
        );
        contextControl.startContexts();
    }

    @After
    public void tearDown() throws Exception {
        LOG.info("****************************** HelloServiceTest::setUp() ******************************");

        // Stops all contexts
        if (contextControl != null) {
            contextControl.stopContexts();
        }

        // Stops the CDI Container
        if (cdiContainer != null) {
            cdiContainer.shutdown();
        }
    }

    @Test
    public void succeed_with_dependent() throws Exception {
        LOG.info("****************************** HelloServiceTest::succeed_with_dependent() ******************************");

        // Given
        String userName = "SYDISNET";
        DependentHelloService dependent = BeanProvider.getContextualReference(DependentHelloService.class, true);

        // When
        String actualMessage = dependent.sayHello(userName);

        // Expects
        Assert.assertThat("The actualMessage cannot be null ! ", actualMessage, notNullValue());
        Assert.assertThat(
            String.format("The actualMessage should contain %s !", userName),
            actualMessage, containsString(userName));
    }


    @Test(expected = NullPointerException.class)
    public void fail_with_singleton() throws Exception {
        LOG.info("****************************** HelloServiceTest::fail_with_singleton() ******************************");

        // Given
        String userName = "SYDISNET";
        SingletonHelloService singleton = BeanProvider.getContextualReference(SingletonHelloService.class, true);

        // When
        // The call here fails because the singleton is null !!
        String actualMessage = singleton.sayHello(userName);

        // Expects
        Assert.assertThat("The actualMessage cannot be null ! ", actualMessage, notNullValue());
        Assert.assertThat(
            String.format("The actualMessage should contain %s !", userName),
            actualMessage, containsString(userName));
    }

    @Test(expected = IllegalStateException.class)
    public void stacktrace_when_singleton() throws Exception {
        LOG.info("****************************** HelloServiceTest::stacktrace_when_singleton() ******************************");

        try {
            SingletonHelloService singleton = BeanProvider.getContextualReference(SingletonHelloService.class, false);
        }
        catch (IllegalStateException e) {
            LOG.error("CDI cannot retrieve the @Singleton bean because \"bean-discovery-mode=\"annotated\"\" ! So, there are no available @Qualifier for this bean !", e);
            throw e;
        }
    }

    @Test
    public void solution_when_dealing_with_singleton() throws Exception {
        LOG.info("****************************** HelloServiceTest::solution_when_dealing_with_singleton() ******************************");

        // Given
        String userName = "SYDISNET";
        SingletonHelloServiceV2 singleton = BeanProvider.getContextualReference(SingletonHelloServiceV2.class, true);

        // When
        String actualMessage = singleton.sayHello(userName);

        // Expects
        Assert.assertThat("The actualMessage cannot be null ! ", actualMessage, notNullValue());
        Assert.assertThat(
            String.format("The actualMessage should contain %s !", userName),
            actualMessage, containsString(userName));
    }
}