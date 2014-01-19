package eu.sydisnet.blog.samples.camel.integration;

import eu.sydisnet.blog.samples.camel.integration.beans.DefaultRouteBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.component.exec.ExecComponent;
import org.apache.camel.component.exec.ExecResult;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.spi.Registry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

/**
 * This class manages the lifecycle of the Camel Context. Camel Context is started by JSR-250 common annotations, <br/>
 * for example @javax.annotation.PostConstruct and stopped by @javax.annotation.PreDestroy callback annotated methods.
 * <p/>
 * Created by @sydisnet on 19/01/14.
 */
@Singleton
@Startup
public class CamelLifecycleManager {

    /**
     * In order to log som infos.
     */
    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().toString());

    /**
     * The CamelContext binded to CamelLifecycleManager.
     */
    private CamelContext camelContext;

    /**
     * Our route builder
     */
    @Inject
    private DefaultRouteBuilder routeBuilder;

    /**
     * Invoking some external program by Camel Exec Component
     *
     * @return the output of the program
     */
    public String callMe() {
        // In order to interact with the CamelContext, we have to create e ProducerTemplate,
        // then we contact the endpoint with an empty (so null) body.
        ExecResult execResult = camelContext.createProducerTemplate().requestBody("direct:call-java-server-version", null, ExecResult.class);

        LOG.info("ExitValue: " + execResult.getExitValue());
        LOG.info("Stdout: " + execResult.getStdout());
        LOG.info("Stderr: " + execResult.getStderr());

        String output;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(execResult.getStderr()))) {
            String line;
            StringBuilder sb = new StringBuilder("\n");
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            output = sb.toString();
        } catch (IOException ex) {
            LOG.severe("Unable to read STDERR... Command should not be succeeded");
            throw new EJBException(ex);
        }

        LOG.info("Output: " + output);

        return output;
    }

    @PostConstruct
    private void start() {
        // 1. Getting the JNDI server context
        Context jeeContext;
        try {
            jeeContext = new InitialContext();
            LOG.info("JavaEE Context Created !");
        } catch (NamingException e) {
            throw new EJBException(e);
        }

        // 2. Registering the JNDI server context in a new JNDI Registry
        // so we can directly perform lookup from Camel
        Registry registry = new JndiRegistry(jeeContext);
        LOG.info("Registry Created !");

        // 3. Creating a new CamelContext
        camelContext = new DefaultCamelContext(registry);
        LOG.info("Camel Context created !");

        // 4. Creating an exec component and configure an associated endpoint to "ext://..." URI
        ExecComponent exec = new ExecComponent();
        LOG.info("EXEC Component Created as \"ext://...\" endpoint !");

        camelContext.addComponent("ext", exec);
        LOG.info("\"ext://...\" endpoint to Camel Context !");

        // 5. Adding routes
        try {
            camelContext.addRoutes(routeBuilder);
            LOG.info("Routes added to Camel Context !");
        } catch (Exception e) {
            throw new EJBException(e);
        }

        // 6. Finally, starting camel
        try {
            camelContext.start();
        } catch (Exception e) {
            throw new EJBException(e);
        }
        LOG.info("Camel Context started !");
    }

    @PreDestroy
    private void stop() {
        try {
            camelContext.stop();
            LOG.info("Camel Context stopped !");
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }
}
