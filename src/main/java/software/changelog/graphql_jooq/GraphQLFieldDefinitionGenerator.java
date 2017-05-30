package software.changelog.graphql_jooq;

import static java.util.Collections.emptySet;

import org.immutables.value.Value;
import org.jooq.Field;

import graphql.schema.GraphQLFieldDefinition;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nonnull;

@FunctionalInterface
@Value.Enclosing
public interface GraphQLFieldDefinitionGenerator {

  @Nonnull
  Optional<GraphQLFieldDefinition> generate(Field<?> field);

  // TODO Test this
  default boolean include(final Configuration configuration,
                          final Field<?> field) {

    final Set<Predicate<Field<?>>> fields = configuration.fields();

    return fields.isEmpty()
           || fields.stream()
                    .anyMatch(predicate -> predicate.test(field));
  }

  @Value.Immutable
  interface Configuration {

    @Nonnull
    @Value.Default
    default Set<Predicate<Field<?>>> fields() {

      return emptySet();
    }

    @Nonnull
    @Value.Default
    default NameGenerator nameGenerator() {

      return Field::getName;
    }

    @FunctionalInterface
    interface NameGenerator {

      String generate(Field<?> field);

    }

  }

}
