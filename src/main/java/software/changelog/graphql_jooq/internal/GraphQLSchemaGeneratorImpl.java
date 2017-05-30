package software.changelog.graphql_jooq.internal;

import static org.jooq.lambda.Seq.seq;

import static graphql.schema.GraphQLObjectType.newObject;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import software.changelog.graphql_jooq.GraphQLObjectTypeGenerator;
import software.changelog.graphql_jooq.GraphQLSchemaGenerator;
import software.changelog.graphql_jooq.ImmutableGraphQLSchemaGenerator;

import org.jooq.Catalog;
import org.jooq.Schema;
import org.jooq.Table;

import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;

public class GraphQLSchemaGeneratorImpl implements GraphQLSchemaGenerator {

  @Nonnull
  static final String MUTATION_TYPE_NAME = "mutationType";

  @Nonnull
  static final String QUERY_TYPE_NAME = "queryType";

  @Nonnull
  private static final Configuration DEFAULT_CONFIGURATION = ImmutableGraphQLSchemaGenerator.Configuration.builder()
                                                                                                          .build();

  @Nonnull
  private final Configuration configuration;

  @Nonnull
  private final GraphQLObjectTypeGenerator graphQLObjectTypeGenerator;

  public GraphQLSchemaGeneratorImpl() {

    this(DEFAULT_CONFIGURATION, new GraphQLObjectTypeGeneratorImpl());
  }

  public GraphQLSchemaGeneratorImpl(final Configuration configuration,
                                    final GraphQLObjectTypeGeneratorImpl graphQLObjectTypeGenerator) {

    super();

    this.graphQLObjectTypeGenerator = graphQLObjectTypeGenerator;
    this.configuration = configuration;
  }

  @Nonnull
  @Override
  public Optional<GraphQLSchema> generate(final Catalog catalog) {

    final boolean include = include(configuration, catalog);

    if (!include) {
      return Optional.empty();
    }

    final GraphQLSchema graphQLSchema = GraphQLSchema.newSchema()
                                                     .mutation(mutation(catalog))
                                                     .query(query(catalog))
                                                     .build(additionalTypes(catalog));

    return Optional.ofNullable(graphQLSchema);
  }

  @Nonnull
  GraphQLObjectType mutation(final Catalog catalog) {

    return seq(catalog.schemaStream()).foldLeft(newObject().name(MUTATION_TYPE_NAME), this::mutation)
                                      .build();
  }

  @Nonnull
  GraphQLObjectType query(final Catalog catalog) {

    return seq(catalog.schemaStream()).foldLeft(newObject().name(QUERY_TYPE_NAME), this::query)
                                      .build();
  }

  @Nonnull
  Set<GraphQLType> additionalTypes(final Catalog catalog) {

    return include(configuration, catalog)
           ? catalog.schemaStream()
                    .map(this::additionalTypes)
                    .flatMap(Collection::stream)
                    .collect(toSet())
           : emptySet();
  }

  @Nonnull
  Set<GraphQLType> additionalTypes(final Schema schema) {

    return include(configuration, schema)
           ? schema.tableStream()
                   .map(graphQLObjectTypeGenerator::generate)
                   .filter(Optional::isPresent)
                   .map(Optional::get)
                   .collect(toSet())
           : emptySet();
  }

  @Nonnull
  GraphQLObjectType.Builder mutation(final GraphQLObjectType.Builder builder,
                                     final Schema schema) {

    return include(configuration, schema)
           ? seq(schema.tableStream()).foldLeft(builder, this::mutation)
           : builder;
  }

  @Nonnull
  GraphQLObjectType.Builder mutation(final GraphQLObjectType.Builder builder,
                                     final Table<?> table) {

    // TODO Configurable ignore

    return builder;
  }

  @Nonnull
  GraphQLObjectType.Builder query(final GraphQLObjectType.Builder builder,
                                  final Schema schema) {

    return include(configuration, schema)
           ? seq(schema.tableStream()).foldRight(builder, (table, builder1) -> query(builder1, table))
           : builder;
  }

  @Nonnull
  GraphQLObjectType.Builder query(final GraphQLObjectType.Builder builder,
                                  final Table<?> table) {

    // TODO Configurable ignore

    return builder;
  }

}
