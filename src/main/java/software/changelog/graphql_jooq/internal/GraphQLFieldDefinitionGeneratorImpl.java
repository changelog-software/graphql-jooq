package software.changelog.graphql_jooq.internal;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;

import software.changelog.graphql_jooq.GraphQLFieldDefinitionGenerator;
import software.changelog.graphql_jooq.GraphQLOutputTypeGenerator;
import software.changelog.graphql_jooq.ImmutableGraphQLFieldDefinitionGenerator;

import org.jooq.Field;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLOutputType;

import java.util.Optional;
import javax.annotation.Nonnull;

public class GraphQLFieldDefinitionGeneratorImpl implements GraphQLFieldDefinitionGenerator {

  @Nonnull
  static final DataFetcher<?> NO_OP_DATA_FETCHER = environment -> null;

  @Nonnull
  private static final Configuration DEFAULT_CONFIGURATION = ImmutableGraphQLFieldDefinitionGenerator.Configuration.builder()
                                                                                                                   .build();

  @Nonnull
  private final Configuration configuration;

  @Nonnull
  private final GraphQLOutputTypeGenerator graphQLOutputTypeGenerator;

  public GraphQLFieldDefinitionGeneratorImpl() {

    this(DEFAULT_CONFIGURATION, new GraphQLOutputTypeGeneratorImpl());
  }

  public GraphQLFieldDefinitionGeneratorImpl(final Configuration configuration,
                                             final GraphQLOutputTypeGenerator graphQLOutputTypeGenerator) {

    super();

    this.configuration = configuration;
    this.graphQLOutputTypeGenerator = graphQLOutputTypeGenerator;
  }

  @Nonnull
  @Override
  public Optional<GraphQLFieldDefinition> generate(final Field<?> field) {

    return include(configuration, field)
           ? graphQLOutputTypeGenerator.generate(field)
                                       .map(graphQLOutputType -> generate(field, graphQLOutputType))
           : Optional.empty();
  }

  @Nonnull
  GraphQLFieldDefinition generate(final Field<?> field,
                                  final GraphQLOutputType graphQLOutputType) {

    final String name = configuration.nameGenerator()
                                     .generate(field);

    final String description = field.getComment();

    return newFieldDefinition().name(name)
                               .description(description)
                               .type(graphQLOutputType)
                               .dataFetcher(NO_OP_DATA_FETCHER) // TODO Add a jOOQ based DataFetcher
                               .build();
  }

}
