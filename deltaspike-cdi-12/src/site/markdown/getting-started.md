# Getting Started !



Si vous utilisez **CDI** a.k.a. **Contexts and Dependency Injection for the Java EE Platform**, peut-être utilisez-vous 
aussi **CDI** dans un contexte *Java SE**, i.e. en dehors de tout serveur d'application **Java EE**.

Le 8 avril 2014 a vu le jour d'une maintenance release pour CDI à savoir la version **1.2**.

Cette version n'est actuellement pas fournie par un serveur d'application quelconque, la spécification **Java EE 7** 
s'appuyant sur **CDI 1.1** et Java EE 8 devrait quant-à elle s'appuyer sur **CDI 2.0**.



## CDI 1.2

La documentation de cette nouvelle mouture est disponible à 
**<a target="_blank" href="http://docs.jboss.org/cdi/spec/1.2/cdi-spec.html">l'URL suivante</a>** sur le site de 
**JBoss**.

L'objectif premier de cette version est de clarifier certains éléments de la spécification comme le fonctionnement 
des événements liés au cycle de vie du conteneur. Toutefois, bien que cette version ne semble être qu'intermédiaire, 
les travaux menés peuvent parfois être déroutant pour ceux qui utilisent le scope 
**<a target="_blank" href="http://docs.oracle.com/javaee/7/api/javax/inject/Singleton.html">@javax.inject.Singleton</a>** 
à ne pas confondre avec l'annotation 
**<a target="_blank" href="http://docs.oracle.com/javaee/7/api/javax/ejb/Singleton.html">@javax.ejb.Singleton</a>** 
propre à la spécification **EJB** 3.1 et +

Et c'est là justement que les choses peuvent _déranger_ car dorénavant, la spécification **CDI 1.2**, lorsqu'elle 
adresse des questions liés à des beans **@Singleton**, il n'est question exclusivement que des beans 
**Singletons EJB** !

Ok ! Peut-être que la stratégie à long terme est de ne disposer que d'une seule annotation **@Singleton** plutôt que 
d'avoir deux annotations, la première dans le package **javax.inject** et la deuxième dans **javax.ejb**.



## Le Singleton : mal aimé du monde Java SE ?

La question est volontairement un peu provocatrice mais le problème constaté est que dorénavant, tous les beans annotés 
avec l'annotation **@javax.inject.Singleton** ne sont plus systèmatiquement chargés par **CDI** dès lors que le fichier 
**"beans.xml"** est présent et que le mode **"bean-discovery-mode"** est positionné à **"annotated"** :

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://xmlns.jcp.org/xml/ns/javaee"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/beans_1_2.xsd"
           version="1.2" bean-discovery-mode="annotated" />
           
Dans ce cas, vous avez le droit à l'exception Java suivante :

    java.lang.IllegalStateException: Could not find beans for Type=class eu.sydisnet.blog.samples.deltaspike.cdi12.boundary.SingletonHelloService and qualifiers:[]
    	at org.apache.deltaspike.core.api.provider.BeanProvider.getContextualReference(BeanProvider.java:150)
    	at org.apache.deltaspike.core.api.provider.BeanProvider.getContextualReference(BeanProvider.java:119)
    	at eu.sydisnet.blog.samples.deltaspike.cdi12.boundary.HelloServiceTest.stacktrace_when_singleton(HelloServiceTest.java:115)
    	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    	at java.lang.reflect.Method.invoke(Method.java:483)
    	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:47)
    	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
    	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:44)
    	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
    	at org.junit.internal.runners.statements.ExpectException.evaluate(ExpectException.java:19)
    	at org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
    	at org.junit.internal.runners.statements.RunAfters.evaluate(RunAfters.java:27)
    	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:271)
    	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:70)
    	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:50)
    	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
    	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
    	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
    	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
    	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
    	at org.junit.runners.ParentRunner.run(ParentRunner.java:309)
    	at org.junit.runner.JUnitCore.run(JUnitCore.java:160)
    	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:74)
    	at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:211)
    	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:67)
    	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    	at java.lang.reflect.Method.invoke(Method.java:483)
    	at com.intellij.rt.execution.application.AppMain.main(AppMain.java:134)


En revanche, pas de problème si la valeur **"all"** est positionnée à la place de **"annotated"**.



## Moins de problèmes de SCAN

Pas évident d'expliquer la raison d'être de ce (dis)fonctionnement mais avant de rentrer dans le détail, l'intérêt 
du mode **"annotated"** et que seuls les beans annotés sont scannés par **CDI** au démarrage du conteneur et cela 
permet d'éviter des problèmes évidents de _**class-loaders**_.

Cela facilite pas mal de choses quand vous utilisez le plugin 
**<a target="_blank" href="http://maven.apache.org/plugins/maven-shade-plugin/usage.html">Maven Shade</a>** qui 
consiste à créer une archive unique contenant toutes les classes du projet mais aussi toutes les classes de toutes 
les dépendances Maven associées.

J'ai eu l'occasion de travailler sur un projet utilisant **CDI 1.1** avec les drivers **JDBC Oracle**, **Bean Validation** 
(Hibernate Validator) et le pool de connexions **HikariCP**.

Avec **CDI 1.1**, il fallait configurer le fichier **META-INF/beans.xml** comme suit :

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://xmlns.jcp.org/xml/ns/javaee"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/beans_1_1.xsd"
           version="1.1" bean-discovery-mode="annotated">
    
        <!-- Configure JAR Scanning Exceptions from CDI -->
        <scan>
            <!-- HikariCP -->
            <exclude name="com.zaxxer.hikari.hibernate.**"/>-->
            <exclude name="com.zaxxer.hikari.metrics.**"/>-->
            <!-- JavAssist -->
            <exclude name="javassist.**"/>
            <!-- Oracle -->
            <exclude name="oracle.xdb.**"/>
            <exclude name="oracle.sql.**"/>
            <exclude name="oracle.jdbc.**"/>
            <!-- Hibernate Validator -->
            <exclude name="org.hibernate.validator.internal.constraintvalidators.**"/>
            <exclude name="org.hibernate.validator.parameternameprovider.**"/>
            <!-- JBoss Logging -->
            <exclude name="org.jboss.logging.**"/>
            <!-- JBoss Weld Extensions -->
            <exclude name="org.jboss.weld.**"/>
        </scan>
    
    </beans>


Avec **CDI 1.2**, plus besoin de préciser la balise SCAN !

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://xmlns.jcp.org/xml/ns/javaee"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/beans_1_2.xsd"
           version="1.2 bean-discovery-mode="annotated">
    
        <!-- No need to configure scan with HikariCP, Oracle and so on... -->
    </beans>


Et ça, c'est tant mieux !



## Une explication possible ?

Alors, une possibilité de réponse est dans la spécification. La version 1.2 précise quels sont les beans chargés 
lorsque le mode **"annotated"** est actif :

* Les beans qui sont annotés par des annotations ayant un scope de type _**"NORMAL"**_ (i.e. 
**@javax.enterprise.context.NormalScope**). Il en existe quatre spécifiquement fournis par CDI :
    * **@javax.enterprise.context.RequestScoped** : Valable le temps d'une requête HTTP ;
    * **@javax.enterprise.context.SessionScoped** : Valable le temps d'une session HTTP ;
    * **@javax.enterprise.context.ApplicationScoped** : Valable le temps d'une application ;
    * **@javax.enterprise.context.ConversationScoped** : Valable le temps d'une conversation ;
* Les beans qui sont annotés par l'annotation **javax.enterprise.context.Dependent** ;

La particularité de cette dernière annotation est qu'il s'agit là d'un scope qui est d'un type qui n'est pas le type 
_**"NORMAL"**_ car elle n'hérite pas de _**@javax.enterprise.context.NormalScope**_. 

En effet, **@Dependent** hérite d'un autre type de scope qui est **@javax.inject.Scope**. On parle dans ce cas de 
**Pseudo-Scope** ! Vous êtes perdus ?

Bref, pour résumer, les **"Pseudo-Scopes"** concernent des annotations qui héritent de **@javax.inject.Scope** et les 
**"Normal-Scopes"** sont des annotations qui héritent de **@javax.enterprise.context.NormalScope**.

Or, pour rappel, le package **javax.inject** ne provient pas de **CDI** mais de la **JSR 330**, la spécification 
qui a défini _**"@javax.inject.Inject"**_ !

L'annotation **@Dependent** est rangée dans le package **javax.enterprise.content** ce qui en fait une annotation 
définie par la spécification **CDI** construite sur la base de n'annotation **@javax.inject.Scope** rangée dans le 
package **javax.inject** définie par la **JSR 330**.

Et la raison pour laquelle ça ne marche pas pour l'annotation **@Singleton** vient du fait que cette annotation 
n'a pas été définie par **CDI** mais par la **JSR 330** puisqu'elle est rangée dans le package **javax.inject** !

Bon, tout ça, c'est pas très sympa pour tout ceux qui veulent un truc qui marche mais une solution existe...


## La solution !

La spécification **CDI 1.2** précise donc que les beans qui ne sont pas des **"Normal Scopes"**, exception faite de 
**@Dependent**, ne sont pas chargés par le conteneur CDI, et cela pour des raisons de compatibilité, je cite :

> Note that to ensure compatibility with other JSR-330 implementations, all pseudo-scope annotations except 
@Dependent are not bean defining annotations. However, a stereotype annotation including a pseudo-scope annotation 
is a bean defining annotation.

Ah ah ! Il suffit donc de créer sa propre sous-annotation an l'annotant avec **"@javax.enterprise.inject.Stereotype"** !

Ok, et comment faire un stéréotype qui soit un **Singleton** ? Et bien un faisant une annotation qui hérite de 
**@javax.inject.Singleton** pardi !
  
Voici le résultat final :

    @javax.inject.Singleton
    @javax.enterprise.inject.Stereotype
    @Retention(RetentionPolicy.RUNTIME)
    @java.lang.annotation.Target({ElementType.TYPE, ElementType.METHOD })
    public @interface Unique {
    }

Cette annotation _custom_ **"@Unique"** est à utiliser en lieu et place de l'annotation **"@javax.inject.Singleton"** 
et là, tout fonctionnera à nouveau...

Ouf !!! Mais pourquoi tant de haine me direz-vous ! Bref, ce problème m'aura valu quelques heures avant de tout 
comprendre mais j'espère que cela aidera ceux qui se retrouveront confrontés à ce problème et que tout cela sera 
éclairci d'ici **CDI 2.0** :)




