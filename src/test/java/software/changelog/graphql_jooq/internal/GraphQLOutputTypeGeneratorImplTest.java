package software.changelog.graphql_jooq.internal;

import static software.changelog.graphql_jooq.AdditionalScalars.GraphQLDate;
import static software.changelog.graphql_jooq.AdditionalScalars.GraphQLDateTime;
import static software.changelog.graphql_jooq.AdditionalScalars.GraphQLTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.lambda.tuple.Tuple.tuple;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

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
import static graphql.schema.GraphQLList.list;
import static graphql.schema.GraphQLNonNull.nonNull;

import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;
import org.jooq.lambda.tuple.Tuple4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import graphql.schema.GraphQLOutputType;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class GraphQLOutputTypeGeneratorImplTest {

  @RunWith(Parameterized.class)
  public static class FindBestTypeTest {

    private final Class<?> javaType;

    private final Class<?> leftTargetType;

    private final Class<?> rightTargetType;

    private final Result side;

    private BinaryOperator<Tuple2<Class<?>, Object>> subject;

    public FindBestTypeTest(final Tuple4<Class<?>, Class<?>, Class<?>, Result> tuple4) {

      javaType = tuple4.v1();
      leftTargetType = tuple4.v2();
      rightTargetType = tuple4.v3();
      side = tuple4.v4();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Seq<Tuple4<Class<?>, Class<?>, Class<?>, Result>> data() {

      return Seq.of(tuple(Object.class, Object.class, Object.class, Result.LEFT), // All the same -> default (left)
                    tuple(String.class, Object.class, Object.class, Result.LEFT), // Same super-class -> default (left)
                    tuple(String.class, String.class, Object.class, Result.LEFT), // Exact match on left -> left
                    tuple(String.class, Object.class, String.class, Result.RIGHT), // Exact match on right -> right
                    tuple(Integer.class, Number.class, Object.class, Result.LEFT), // Closer super-class on left -> left
                    tuple(Integer.class, Object.class, Number.class, Result.RIGHT), // Closer super-class on right -> right
                    tuple(String.class, CharSequence.class, Number.class, Result.LEFT), // Implemented interface on left, unrelated on right -> left
                    tuple(String.class, Number.class, CharSequence.class, Result.RIGHT), // Implemented interface on right, unrelated on left -> right
                    tuple(Integer.class, Number.class, Object.class, Result.LEFT), // Closer ancestor on left -> left
                    tuple(Integer.class, Object.class, Number.class, Result.RIGHT), // Closer ancestor on right -> right
                    tuple(Integer.class, Number.class, String.class, Result.LEFT), // Ancestor on left, unrelated on right -> left
                    tuple(Integer.class, String.class, Number.class, Result.RIGHT), // Ancestor on right, unrelated on left -> right
                    tuple(Integer.class, Number.class, Comparable.class, Result.LEFT), // Class on left, interface on right -> left
                    tuple(Integer.class, Comparable.class, Number.class, Result.RIGHT), // Interface on left, class on right -> right
                    tuple(String.class, Comparable.class, Serializable.class, Result.LEFT), // Both options valid -> default (left)
                    tuple(String.class, Object.class, Object.class, Result.LEFT), // Both options valid -> default (left)
                    tuple(String.class, Number.class, Stream.class, Result.LEFT)); // Neither are valid (shouldn't happen if filtering is correct) -> default (left)
    }

    @Before
    public void setUp() {

      subject = GraphQLOutputTypeGeneratorImpl.findBestType(javaType);
    }

    @Test
    public void testFindBestType() {

      final Tuple2<Class<?>, Object> left = tuple(leftTargetType, null);
      final Tuple2<Class<?>, Object> right = tuple(rightTargetType, null);
      final Tuple2<Class<?>, Object> expected = side == Result.LEFT
                                                ? left
                                                : right;

      final Tuple2<Class<?>, Object> result = subject.apply(left, right);


      assertThat((Object) result).isSameAs(expected);
    }

    enum Result {
      LEFT,
      RIGHT,
    }

  }

  @RunWith(Parameterized.class)
  public static class GraphQLTypeGeneratorImplGenerateOutputTypeTest {

    private final GraphQLOutputType graphQLOutputType;

    private final Class<?> javaType;

    private DataType<?> dataType;

    private Field<?> field;

    private GraphQLOutputTypeGeneratorImpl subject;

    public GraphQLTypeGeneratorImplGenerateOutputTypeTest(final Tuple2<Class<?>, GraphQLOutputType> tuple2) {

      this.javaType = tuple2.v1();
      this.graphQLOutputType = tuple2.v2();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Seq<Tuple2<Class<?>, GraphQLOutputType>> data() {

      return Seq.of(tuple(Integer.class, GraphQLInt),
                    tuple(Long.class, GraphQLLong),
                    tuple(Short.class, GraphQLShort),
                    tuple(Byte.class, GraphQLByte),
                    tuple(Float.class, GraphQLFloat),
                    tuple(BigInteger.class, GraphQLBigInteger),
                    tuple(BigDecimal.class, GraphQLBigDecimal),
                    tuple(CharSequence.class, GraphQLString),
                    tuple(String.class, GraphQLString),
                    tuple(Boolean.class, GraphQLBoolean),
                    tuple(Character.class, GraphQLChar),

                    tuple(java.util.Date.class, GraphQLDate),
                    tuple(java.sql.Date.class, GraphQLDate),
                    tuple(Time.class, GraphQLTime),
                    tuple(LocalDate.class, GraphQLDate),
                    tuple(LocalTime.class, GraphQLTime),
                    tuple(LocalDateTime.class, GraphQLDateTime),

                    tuple(Object.class, null));
    }

    private static Class<?> arrayOf(final Class<?> type) {

      return Array.newInstance(type, 0)
                  .getClass();
    }

    @Before
    public void setUp() {

      dataType = Mockito.mock(DataType.class);
      field = Mockito.mock(Field.class);

      subject = new GraphQLOutputTypeGeneratorImpl();
    }

    @Test
    public void testGenerate() {

      final Optional<GraphQLOutputType> result = subject.generate(javaType);

      if (graphQLOutputType == null) {
        assertThat(result).isEmpty();
      }
      else {
        assertThat(result).usingFieldByFieldValueComparator()
                          .contains(graphQLOutputType);
      }
    }

    @Test
    public void testGenerateWithNonNullArrayDataType() {

      when(dataType.isArray()).thenReturn(true);
      doReturn(javaType).when(dataType)
                        .getArrayType();
      when(dataType.nullable()).thenReturn(false);

      final Optional<GraphQLOutputType> result = subject.generate(dataType);

      if (graphQLOutputType == null) {
        assertThat(result).isEmpty();
      }
      else {
        assertThat(result).usingFieldByFieldValueComparator()
                          .contains(nonNull(list(graphQLOutputType)));
      }
    }

    @Test
    public void testGenerateWithNonNullArrayField() {

      doReturn(dataType).when(field)
                        .getDataType();
      when(dataType.isArray()).thenReturn(true);
      doReturn(javaType).when(dataType)
                        .getArrayType();
      when(dataType.nullable()).thenReturn(false);

      final Optional<GraphQLOutputType> result = subject.generate(field);

      if (graphQLOutputType == null) {
        assertThat(result).isEmpty();
      }
      else {
        assertThat(result).usingFieldByFieldValueComparator()
                          .contains(nonNull(list(graphQLOutputType)));
      }
    }

    @Test
    public void testGenerateWithNonNullDataType() {

      doReturn(javaType).when(dataType)
                        .getType();
      when(dataType.nullable()).thenReturn(false);

      final Optional<GraphQLOutputType> result = subject.generate(dataType);

      if (graphQLOutputType == null) {
        assertThat(result).isEmpty();
      }
      else {
        assertThat(result).usingFieldByFieldValueComparator()
                          .contains(nonNull(graphQLOutputType));
      }
    }

    @Test
    public void testGenerateWithNonNullField() {

      doReturn(dataType).when(field)
                        .getDataType();
      doReturn(javaType).when(dataType)
                        .getType();
      when(dataType.nullable()).thenReturn(false);

      final Optional<GraphQLOutputType> result = subject.generate(field);

      if (graphQLOutputType == null) {
        assertThat(result).isEmpty();
      }
      else {
        assertThat(result).usingFieldByFieldValueComparator()
                          .contains(nonNull(graphQLOutputType));
      }
    }

    @Test
    public void testGenerateWithNullableArrayDataType() {

      when(dataType.isArray()).thenReturn(true);
      doReturn(javaType).when(dataType)
                        .getArrayType();
      when(dataType.nullable()).thenReturn(true);

      final Optional<GraphQLOutputType> result = subject.generate(dataType);

      if (graphQLOutputType == null) {
        assertThat(result).isEmpty();
      }
      else {
        assertThat(result).usingFieldByFieldValueComparator()
                          .contains(list(graphQLOutputType));
      }
    }

    @Test
    public void testGenerateWithNullableArrayField() {

      when(dataType.isArray()).thenReturn(true);
      doReturn(dataType).when(field)
                        .getDataType();
      doReturn(javaType).when(dataType)
                        .getArrayType();
      when(dataType.nullable()).thenReturn(true);

      final Optional<GraphQLOutputType> result = subject.generate(field);

      if (graphQLOutputType == null) {
        assertThat(result).isEmpty();
      }
      else {
        assertThat(result).usingFieldByFieldValueComparator()
                          .contains(list(graphQLOutputType));
      }
    }

    @Test
    public void testGenerateWithNullableDataType() {

      doReturn(javaType).when(dataType)
                        .getType();
      when(dataType.nullable()).thenReturn(true);

      final Optional<GraphQLOutputType> result = subject.generate(dataType);

      if (graphQLOutputType == null) {
        assertThat(result).isEmpty();
      }
      else {
        assertThat(result).usingFieldByFieldValueComparator()
                          .contains(graphQLOutputType);
      }
    }

    @Test
    public void testGenerateWithNullableField() {

      doReturn(dataType).when(field)
                        .getDataType();
      doReturn(javaType).when(dataType)
                        .getType();
      when(dataType.nullable()).thenReturn(true);

      final Optional<GraphQLOutputType> result = subject.generate(field);

      if (graphQLOutputType == null) {
        assertThat(result).isEmpty();
      }
      else {
        assertThat(result).usingFieldByFieldValueComparator()
                          .contains(graphQLOutputType);
      }
    }

  }

  @RunWith(Parameterized.class)
  public static class IsAssignableFromTest {

    private final boolean expected;

    private final Class<?> javaType;

    private final Class<?> targetType;

    private Predicate<Tuple2<Class<?>, ?>> subject;

    public IsAssignableFromTest(final Tuple3<Class<?>, Class<?>, Boolean> tuple3) {

      javaType = tuple3.v1();
      targetType = tuple3.v2();
      expected = tuple3.v3();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Seq<Tuple3<Class<?>, Class<?>, Boolean>> data() {

      return Seq.of(tuple(String.class, Object.class, true), // Object is assignable from String
                    tuple(String.class, String.class, true), // String is assignable from String
                    tuple(Object.class, String.class, false)); // String is not assignable from Object
    }

    @Before
    public void setUp() {

      subject = GraphQLOutputTypeGeneratorImpl.isAssignableAs(javaType);
    }

    @Test
    public void testIsAssignableAs() {

      final boolean result = subject.test(tuple(targetType, null));

      assertThat(result).isEqualTo(expected);
    }

  }

}
