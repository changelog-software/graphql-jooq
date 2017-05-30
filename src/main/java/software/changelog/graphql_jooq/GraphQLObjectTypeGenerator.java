package software.changelog.graphql_jooq;

import static java.util.Collections.emptySet;

import org.immutables.value.Value;
import org.jooq.Table;

import graphql.schema.GraphQLObjectType;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nonnull;

@FunctionalInterface
@Value.Enclosing
public interface GraphQLObjectTypeGenerator {

  @Nonnull
  Optional<GraphQLObjectType> generate(final Table<?> table);

  // TODO Test this
  default boolean include(final Configuration configuration,
                          final Table<?> table) {

    final Set<Predicate<Table>> tables = configuration.tables();

    return tables.isEmpty()
           || tables.stream()
                    .anyMatch(predicate -> predicate.test(table));
  }

  @Value.Immutable
  interface Configuration {

    @Nonnull
    @Value.Default
    default NameGenerator nameGenerator() {

      return Table::getName;
    }

    @Nonnull
    @Value.Default
    default Set<Predicate<Table>> tables() {

      return emptySet();
    }

    @FunctionalInterface
    interface NameGenerator {

      String generate(Table<?> table);

    }

  }

}
