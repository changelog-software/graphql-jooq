package software.changelog.graphql_jooq;

import static java.util.Collections.emptySet;

import org.immutables.value.Value;
import org.jooq.Catalog;
import org.jooq.Schema;

import graphql.schema.GraphQLSchema;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nonnull;

@FunctionalInterface
@Value.Enclosing
public interface GraphQLSchemaGenerator {

  @Nonnull
  Optional<GraphQLSchema> generate(Catalog catalog);

  // TODO Test this
  default boolean include(final Configuration configuration,
                          final Schema schema) {

    final Set<Predicate<Schema>> schemas = configuration.schemas();

    return schemas.isEmpty()
           ||
           schemas.stream()
                  .anyMatch(predicate -> predicate.test(schema));
  }

  // TODO Test this
  default boolean include(final Configuration configuration,
                          final Catalog catalog) {

    final Set<Predicate<Catalog>> catalogs = configuration.catalogs();

    return catalogs.isEmpty()
           || catalogs.stream()
                      .anyMatch(predicate -> predicate.test(catalog));
  }

  @Value.Immutable
  interface Configuration {

    @Nonnull
    @Value.Default
    default Set<Predicate<Catalog>> catalogs() {

      return emptySet();
    }

    @Nonnull
    @Value.Default
    default Set<Predicate<Schema>> schemas() {

      return emptySet();
    }

  }

}
