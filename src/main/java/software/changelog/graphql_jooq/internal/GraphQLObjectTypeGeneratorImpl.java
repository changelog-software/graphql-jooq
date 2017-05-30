package software.changelog.graphql_jooq.internal;

import static graphql.schema.GraphQLObjectType.newObject;

import static java.util.stream.Collectors.toList;

import software.changelog.graphql_jooq.GraphQLFieldDefinitionGenerator;
import software.changelog.graphql_jooq.GraphQLObjectTypeGenerator;
import software.changelog.graphql_jooq.ImmutableGraphQLObjectTypeGenerator;

import org.jooq.Table;

import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

public class GraphQLObjectTypeGeneratorImpl implements GraphQLObjectTypeGenerator {

  @Nonnull
  private static final Configuration DEFAULT_CONFIGURATION = ImmutableGraphQLObjectTypeGenerator.Configuration.builder()
                                                                                                              .build();

  @Nonnull
  private final Configuration configuration;

  @Nonnull
  private final GraphQLFieldDefinitionGenerator graphQLFieldDefinitionGenerator;

  public GraphQLObjectTypeGeneratorImpl() {

    this(DEFAULT_CONFIGURATION, new GraphQLFieldDefinitionGeneratorImpl());
  }

  public GraphQLObjectTypeGeneratorImpl(final Configuration configuration,
                                        final GraphQLFieldDefinitionGenerator graphQLFieldDefinitionGenerator) {

    super();

    this.configuration = configuration;
    this.graphQLFieldDefinitionGenerator = graphQLFieldDefinitionGenerator;
  }

  @Nonnull
  @Override
  public Optional<GraphQLObjectType> generate(final Table<?> table) {

    if (!include(configuration, table)) {
      return Optional.empty();
    }

    // TODO Primary Key -> GraphQLID
    // TODO Foreign Key -> GraphQLObjectType(??)

    final List<GraphQLFieldDefinition> graphQLFieldDefinitions = table.fieldStream()
                                                                      .map(graphQLFieldDefinitionGenerator::generate)
                                                                      .filter(Optional::isPresent)
                                                                      .map(Optional::get)
                                                                      .collect(toList());

    if (graphQLFieldDefinitions.isEmpty()) {
      return Optional.empty();
    }

    final String name = configuration.nameGenerator()
                                     .generate(table);

    final String description = table.getComment();

    final GraphQLObjectType graphQLObjectType = newObject().name(name)
                                                           .description(description)
                                                           .fields(graphQLFieldDefinitions)
                                                           .build();

    return Optional.of(graphQLObjectType);
  }

}
