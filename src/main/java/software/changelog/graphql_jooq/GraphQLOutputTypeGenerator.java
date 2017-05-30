package software.changelog.graphql_jooq;

import static software.changelog.graphql_jooq.AdditionalScalars.GraphQLDate;
import static software.changelog.graphql_jooq.AdditionalScalars.GraphQLDateTime;
import static software.changelog.graphql_jooq.AdditionalScalars.GraphQLTime;

import static org.jooq.lambda.Seq.toMap;
import static org.jooq.lambda.tuple.Tuple.tuple;

import static graphql.Scalars.GraphQLBigDecimal;
import static graphql.Scalars.GraphQLBigInteger;
import static graphql.Scalars.GraphQLBoolean;
import static graphql.Scalars.GraphQLByte;
import static graphql.Scalars.GraphQLChar;
import static graphql.Scalars.GraphQLFloat;
import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLShort;
import static graphql.Scalars.GraphQLString;

import org.immutables.value.Value;
import org.jooq.Field;
import org.jooq.lambda.Seq;

import graphql.schema.GraphQLOutputType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;

@FunctionalInterface
@Value.Enclosing
public interface GraphQLOutputTypeGenerator {

  @Nonnull
  Optional<GraphQLOutputType> generate(final Field<?> field);

  @Value.Immutable
  interface Configuration {

    @Nonnull
    @Value.Default
    default Map<Class<?>, GraphQLOutputType> typeMappings() {

      return toMap(Seq.of(tuple(Integer.class, GraphQLInt),
                          tuple(Long.class, GraphQLLong),
                          tuple(Short.class, GraphQLShort),
                          tuple(Byte.class, GraphQLByte),
                          tuple(Float.class, GraphQLFloat),
                          tuple(BigInteger.class, GraphQLBigInteger),
                          tuple(BigDecimal.class, GraphQLBigDecimal),
                          tuple(CharSequence.class, GraphQLString),
                          tuple(Boolean.class, GraphQLBoolean),
                          tuple(Character.class, GraphQLChar),
                          tuple(Date.class, GraphQLDate),
                          tuple(Time.class, GraphQLTime),
                          tuple(LocalDate.class, GraphQLDate),
                          tuple(LocalTime.class, GraphQLTime),
                          tuple(LocalDateTime.class, GraphQLDateTime)));
    }

  }

}
