camel-integration
=================

First, you have to download WildFly 8.0.0.CR1 from [WildFly website](http://www.wildfly.org/).

If your OS is Windows and you are using NVidia Geforce Experience (in my case), you should have a service named
"NvNetworkService.exe" which use the port 9990. So you may encounter some difficulties, there is a workaround for this
kind of troubles.

First, copy the <WILDFLY_AS_HOME>/standalone/configuration.xml in the same directory and call it sydisnet.xml.

Then, you have to uncomment in arquillian.xml, the following lines :

```xml
(...)
<container qualifier="wildfly-remote-8" default="true">
    <configuration>
        <property name="jbossHome">C:/opt/jboss/wildfly/wildfly-8.0.0.CR1</property>
        <property name="javaVmArguments">-Xmx512m -XX:MaxPermSize=128m</property>
        <!-- This property must be overridden is you don't want to use standalone.xml
        <property name="serverConfig">sydisnet.xml</property> -->
        <property name="managementAddress">127.0.0.1</property>
        <!-- This property must to be redefined if standard port is overridden...
        <property name="managementPort">19990</property> -->
        (...)
    </configuration>
</container>
```

In sydisnet.xml, search for port-offset="${jboss.socket.binding.port-offset:0}" and change it to

```xml
    port-offset="${jboss.socket.binding.port-offset:10000}"
```

So, ports used by WildFly will be 18080 for HTTP, 19999 for native management and 19990 for web console management.
That is this last port which is used by Arquillian.
