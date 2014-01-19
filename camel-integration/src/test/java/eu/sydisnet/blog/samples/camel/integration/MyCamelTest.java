package eu.sydisnet.blog.samples.camel.integration;

import eu.sydisnet.blog.samples.camel.integration.beans.DefaultRouteBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

/**
 * This class test a Camel Context whose lifecycle is managed by a Singleton-EJB
 * <p/>
 * Created by @sydisnet on 19/01/14.
 */
@RunWith(Arquillian.class)
public class MyCamelTest {

    /**
     * We are using a logging system
     */
    static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().toString());

    /**
     * We have to inject CamelLifeCycleManager in order to bootstrap our camel container
     */
    @Inject
    CamelLifecycleManager camelStarter;

    /**
     * We have to create a deployable archive in order to test our Camel Context
     *
     * @return a deployable archive
     */
    @Deployment
    public static Archive<?> createDeployment() {
        // Web Archive
        WebArchive archiveUnderTest = ShrinkWrap.create(WebArchive.class, "MyCamelTest.war");

        // CDI Activation
        // We are using annotated beans so we have to specify it in beans.xml
        archiveUnderTest.addAsWebInfResource("WEB-INF/beans.xml", ArchivePaths.create("beans.xml"));

        // Adding classes
        archiveUnderTest.addClasses(CamelLifecycleManager.class, DefaultRouteBuilder.class, MyCamelTest.class);

        // Adding modules
        PomEquippedResolveStage mavenResolver = Maven.resolver().loadPomFromFile("pom.xml");

        // Download Libs
        File[] deps = mavenResolver.resolve("org.apache.camel:camel-core:2.12.2", "org.apache.camel:camel-exec:2.12.2").withTransitivity().asFile();
        archiveUnderTest.addAsLibraries(deps);

        // Enterprise Archive for deployement
        EnterpriseArchive deployable = ShrinkWrap.create(EnterpriseArchive.class, "MyCamelTest.ear");
        deployable.addAsModules(archiveUnderTest);

        // Listing Application Content
        LOG.info("Content of MyCamelTest.ear: " + deployable.toString(true));
        LOG.info("Content of MyCamelTest.war: " + archiveUnderTest.toString(true));

        return deployable;
    }

    @Test
    public void should_be_able_to_start_camel_embedded_container() {
        // Given CamelStarter

        // When
        String response = camelStarter.callMe();

        // Expect
        assertTrue("Should not be empty", !response.isEmpty());
        LOG.info("Camel Status is " + response);
    }

}
