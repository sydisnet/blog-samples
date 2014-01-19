package eu.sydisnet.blog.samples.camel.integration.beans;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.exec.ExecBinding;

import javax.enterprise.context.Dependent;

/**
 * This class configures camel routes which will be managed by the CamelContext created by the <br/>
 * CamelLifecycleManager. The class is annotated with @javax.enterprise.context.Dependent because we are using <br/>
 * CDI annotated beans instead of discovering all CDI beans in order to avoid injection of non-desired beans.
 * <p/>
 * Created by @sydisnet on 19/01/14.
 */
@Dependent
public class DefaultRouteBuilder extends RouteBuilder {
    /**
     * <b>Called on initialization to build the routes using the fluent builder syntax.</b>
     * <p/>
     * This is a central method for RouteBuilder implementations to implement
     * the routes using the Java fluent builder syntax.
     *
     * @throws Exception can be thrown during configuration
     */
    @Override
    public void configure() throws Exception {
        from("direct:call-java-server-version")
                .setHeader(ExecBinding.EXEC_USE_STDERR_ON_EMPTY_STDOUT, constant(true))
                .to("ext://java?args=-server -version");
    }
}
