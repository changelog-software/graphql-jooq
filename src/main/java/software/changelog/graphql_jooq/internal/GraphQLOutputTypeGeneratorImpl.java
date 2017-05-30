package software.changelog.graphql_jooq.internal;

import static org.jooq.lambda.Seq.seq;

import static graphql.schema.GraphQLList.list;
import static graphql.schema.GraphQLNonNull.nonNull;

import software.changelog.graphql_jooq.GraphQLOutputTypeGenerator;
import software.changelog.graphql_jooq.ImmutableGraphQLOutputTypeGenerator;

import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.schema.GraphQLOutputType;

import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import javax.annotation.Nonnull;

public class GraphQLOutputTypeGeneratorImpl implements GraphQLOutputTypeGenerator {

  @Nonnull
  private static final Configuration DEFAULT_CONFIGURATION = ImmutableGraphQLOutputTypeGenerator.Configuration.builder()
                                                                                                              .build();

  @Nonnull
  private static final Logger LOGGER = LoggerFactory.getLogger(GraphQLOutputTypeGeneratorImpl.class);

  @Nonnull
  private final Configuration configuration;

  public GraphQLOutputTypeGeneratorImpl() {

    this(DEFAULT_CONFIGURATION);
  }

  public GraphQLOutputTypeGeneratorImpl(final Configuration configuration) {

    super();

    this.configuration = configuration;
  }

  @Nonnull
  @Override
  public Optional<GraphQLOutputType> generate(final Field<?> field) {

    return generate(field.getDataType());
  }

  @Nonnull
  Optional<GraphQLOutputType> generate(final DataType<?> dataType) {

    final boolean isArray = dataType.isArray();

    final Class<?> javaType = isArray
                              ? dataType.getArrayType()
                              : dataType.getType();

    final UnaryOperator<GraphQLOutputType> wrapList = graphQLOutputType -> isArray
                                                                           ? list(graphQLOutputType)
                                                                           : graphQLOutputType;

    final UnaryOperator<GraphQLOutputType> wrapNonNull = graphQLOutputType -> dataType.nullable()
                                                                              ? graphQLOutputType
                                                                              : nonNull(graphQLOutputType);

    return generate(javaType).map(wrapList)
                             .map(wrapNonNull);
  }

  @Nonnull
  Optional<GraphQLOutputType> generate(final Class<?> javaType) {

    return seq(configuration.typeMappings()).filter(isAssignableAs(javaType))
                                            .reduce(findBestType(javaType))
                                            .map(Tuple2::v2);
  }

  @Nonnull
  static Predicate<Tuple2<Class<?>, ?>> isAssignableAs(final Class<?> javaType) {

    return input -> input.v1()
                         .isAssignableFrom(javaType);
  }

  @Nonnull
  static <T> BinaryOperator<Tuple2<Class<?>, T>> findBestType(final Class<?> javaType) {

    return (left, right) -> {

      final Class<?> leftType = left.v1();

      final Class<?> rightType = right.v1();

      if (leftType.equals(javaType)) {
        return left;
      }

      if (rightType.equals(javaType)) {
        return right;
      }

      if (leftType.isAssignableFrom(javaType) && !rightType.isAssignableFrom(javaType)) {
        return left;
      }

      if (rightType.isAssignableFrom(javaType) && !leftType.isAssignableFrom(javaType)) {
        return right;
      }

      if (!leftType.isInterface() && rightType.isInterface()) {
        return left;
      }

      if (leftType.isInterface() && !rightType.isInterface()) {
        return right;
      }

      if (rightType.isAssignableFrom(leftType)) {
        return left;
      }

      if (leftType.isAssignableFrom(rightType)) {
        return right;
      }

      LOGGER.warn("Unable to determine the best type for [{}] from options [{}] and [{}] - assuming [{}]", javaType, leftType, rightType, leftType);

      return left;
    };

  }

}
