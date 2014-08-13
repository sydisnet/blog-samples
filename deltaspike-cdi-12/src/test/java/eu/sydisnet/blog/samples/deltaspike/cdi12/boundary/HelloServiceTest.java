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
 * Integration Tests for the {@link eu.sydisnet.blog.samples.deltaspike.cdi12.boundary.HelloService} class.
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

    /**
     * The Service under Test.
     */
    HelloService sut;

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

        // Gets the Service Under Test :)
        sut = BeanProvider.getContextualReference(HelloService.class);
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
    public void should_be_able_to_say_hello() throws Exception {
        LOG.info("****************************** HelloServiceTest::should_be_able_to_say_hello() ******************************");

        // Given
        String userName = "SYDISNET";

        // When
        String actualMessage = sut.sayHello(userName);

        // Expects
        Assert.assertThat("The actualMessage cannot be null ! ", actualMessage, notNullValue());
        Assert.assertThat(
            String.format("The actualMessage should contain %s !", userName),
            actualMessage, containsString(userName));
    }
}