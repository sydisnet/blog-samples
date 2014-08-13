package eu.sydisnet.blog.samples.deltaspike.cdi12.boundary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Dans CDI 1.2, il y a eu certaines clarifications concernant les scopes.
 *
 * L'annotation {@link javax.inject.Singleton} est un pseudo-scope. Cela signifie qu'il ne s'agit pas
 * d'un<strong>Built-In</strong> scope. La notion de <strong>Singleton</strong> n'est valable que pour les EJB
 * singleton sessions beans.
 *
 * Les <strong>Built-In</strong> scopes sont de cinq types dont les quatre premiers sont des
 * {@link javax.enterprise.context.NormalScope} i.e. susceptibles d'être passivés, le dernier étant quant
 * à lui un {@link javax.inject.Scope}, i.e. un <strong>Pseudo-Scope</strong> :
 * <ul>
 * <li>{@link javax.enterprise.context.RequestScoped}: Valable le temps d'une requête HTTP ;</li>
 * <li>{@link javax.enterprise.context.SessionScoped}: Valable le temps d'une session HTTP ;</li>
 * <li>{@link javax.enterprise.context.ApplicationScoped}: Valable le temps d'une application ;</li>
 * <li>{@link javax.enterprise.context.ConversationScoped}: cf. le cycle de vie d'une Conversation ;</li>
 * <li>{@link javax.enterprise.context.Dependent}: équivalent à un bean Spring de type <i>prototype</i> ;</li>
 * </ul>
 *
 * Le mode de découverte de beans par CDI est par défaut <strong>annoté</strong>, ce qui signifie qu'un bean
 * ne peut être considéré par CDI que s'il est annoté par des annotations bien précises à savoir :
 * <ul>
 * <li>{@link javax.enterprise.context.ApplicationScoped} ;</li>
 * <li>{@link javax.enterprise.context.SessionScoped} ;</li>
 * <li>{@link javax.enterprise.context.ConversationScoped} ;</li>
 * <li>{@link javax.enterprise.context.RequestScoped} ;</li>
 * <li>Tout autre scope de type <strong>{@link javax.enterprise.context.NormalScope}</strong> ;</li>
 * <li>{@link javax.interceptor.Interceptor} ou  {@link javax.decorator.Decorator} ;</li>
 * <li>Toute annotation de type <strong>{@link javax.enterprise.inject.Stereotype} ;</strong></li>
 * <li>Le <strong>Pseudo-Scope {@link javax.enterprise.context.Dependent}</strong> et
 * uniquement de dernier ;</li>
 * </ul>
 * Le <strong>Pseudo-Scope {@link javax.inject.Singleton}</strong> étant défini par la JSR 330 (@Inject) et
 * non pas par la JSR 290 (CDI), ce dernier n'est plus considéré par le mode de découverte de beans par CDI.
 *
 * @author sebastien.hebert@efs.sante.fr
 * @version 1.0.0
 */
@javax.inject.Singleton
@javax.enterprise.inject.Stereotype
@Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ElementType.TYPE, ElementType.METHOD })
public @interface Unique {
}
