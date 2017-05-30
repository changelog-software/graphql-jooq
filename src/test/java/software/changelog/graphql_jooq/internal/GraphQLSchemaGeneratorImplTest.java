package software.changelog.graphql_jooq.internal;

import static software.changelog.graphql_jooq.internal.GraphQLSchemaGeneratorImpl.MUTATION_TYPE_NAME;
import static software.changelog.graphql_jooq.internal.GraphQLSchemaGeneratorImpl.QUERY_TYPE_NAME;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import static graphql.schema.GraphQLObjectType.newObject;

import org.jooq.Catalog;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Schema;
import org.jooq.Table;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import graphql.schema.GraphQLObjectType;
import graphql.schema.idl.SchemaPrinter;

import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class GraphQLSchemaGeneratorImplTest {

  @Mock
  private Catalog catalog;

  @Mock
  private DataType<?> dataType;

  @Mock
  private Field<?> field;

  @Mock
  private Schema schema;

  private SchemaPrinter schemaPrinter;

  private GraphQLSchemaGeneratorImpl subject;

  @Mock
  private Table<?> table;

  @Before
  public void setUp() {

    schemaPrinter = new SchemaPrinter();

    subject = new GraphQLSchemaGeneratorImpl();
  }

  @Test
  public void testAdditionalTypesWithCatalog() {

    when(catalog.schemaStream()).thenReturn(Stream.of(schema, schema));
    when(schema.tableStream()).thenReturn(Stream.of(table))
                              .thenReturn(Stream.of(table));
    when(table.getName()).thenReturn("table_name_1")
                         .thenReturn("table_name_2");
    when(table.fieldStream()).thenReturn(Stream.of(field, field))
                             .thenReturn(Stream.of(field, field));
    when(field.getName()).thenReturn("field_name_1")
                         .thenReturn("field_name_2")
                         .thenReturn("field_name_3")
                         .thenReturn("field_name_4");
    doReturn(dataType).doReturn(dataType)
                      .doReturn(dataType)
                      .doReturn(dataType)
                      .when(field)
                      .getDataType();
    when(dataType.isArray()).thenReturn(true)
                            .thenReturn(false)
                            .thenReturn(true)
                            .thenReturn(false);
    when(dataType.nullable()).thenReturn(true)
                             .thenReturn(true)
                             .thenReturn(false)
                             .thenReturn(false);
    doReturn(String.class).doReturn(String.class)
                          .when(dataType)
                          .getType();
    doReturn(Integer.class).doReturn(Integer.class)
                           .when(dataType)
                           .getArrayType();

    final String result = subject.additionalTypes(catalog)
                                 .stream()
                                 .map(schemaPrinter::print)
                                 .reduce("", String::concat);

    assertThat(result).contains("type table_name_1 {\n" +
                                "   field_name_1 : [Int]\n" +
                                "   field_name_2 : String\n" +
                                "}\n")
                      .contains("type table_name_2 {\n" +
                                "   field_name_3 : [Int]!\n" +
                                "   field_name_4 : String!\n" +
                                "}\n");
  }

  @Test
  public void testAdditionalTypesWithSchema() {

    when(schema.tableStream()).thenReturn(Stream.of(table, table));
    when(table.getName()).thenReturn("table_name_1")
                         .thenReturn("table_name_2");
    when(table.fieldStream()).thenReturn(Stream.of(field, field))
                             .thenReturn(Stream.of(field, field));
    when(field.getName()).thenReturn("field_name_1")
                         .thenReturn("field_name_2")
                         .thenReturn("field_name_3")
                         .thenReturn("field_name_4");
    doReturn(dataType).doReturn(dataType)
                      .doReturn(dataType)
                      .doReturn(dataType)
                      .when(field)
                      .getDataType();
    when(dataType.isArray()).thenReturn(true)
                            .thenReturn(false)
                            .thenReturn(true)
                            .thenReturn(false);
    when(dataType.nullable()).thenReturn(true)
                             .thenReturn(true)
                             .thenReturn(false)
                             .thenReturn(false);
    doReturn(String.class).doReturn(String.class)
                          .when(dataType)
                          .getType();
    doReturn(Integer.class).doReturn(Integer.class)
                           .when(dataType)
                           .getArrayType();

    final String result = subject.additionalTypes(schema)
                                 .stream()
                                 .map(schemaPrinter::print)
                                 .reduce("", String::concat);

    assertThat(result).contains("type table_name_1 {\n" +
                                "   field_name_1 : [Int]\n" +
                                "   field_name_2 : String\n" +
                                "}\n")
                      .contains("type table_name_2 {\n" +
                                "   field_name_3 : [Int]!\n" +
                                "   field_name_4 : String!\n" +
                                "}\n");
  }

  @Test
  public void testGenerate() {


    when(catalog.schemaStream()).thenReturn(Stream.of(schema, schema))
                                .thenReturn(Stream.of(schema, schema))
                                .thenReturn(Stream.of(schema, schema));
    when(schema.tableStream()).thenReturn(Stream.of(table))
                              .thenReturn(Stream.of(table))
                              .thenReturn(Stream.of(table))
                              .thenReturn(Stream.of(table))
                              .thenReturn(Stream.of(table))
                              .thenReturn(Stream.of(table));
    when(table.getName()).thenReturn("table_name_1")
                         .thenReturn("table_name_2");
    when(table.fieldStream()).thenReturn(Stream.of(field, field))
                             .thenReturn(Stream.of(field, field));
    when(field.getName()).thenReturn("field_name_1")
                         .thenReturn("field_name_2")
                         .thenReturn("field_name_3")
                         .thenReturn("field_name_4");
    doReturn(dataType).doReturn(dataType)
                      .doReturn(dataType)
                      .doReturn(dataType)
                      .when(field)
                      .getDataType();
    when(dataType.isArray()).thenReturn(true)
                            .thenReturn(false)
                            .thenReturn(true)
                            .thenReturn(false);
    when(dataType.nullable()).thenReturn(true)
                             .thenReturn(true)
                             .thenReturn(false)
                             .thenReturn(false);
    doReturn(String.class).doReturn(String.class)
                          .when(dataType)
                          .getType();
    doReturn(Integer.class).doReturn(Integer.class)
                           .when(dataType)
                           .getArrayType();

    final String result = subject.generate(catalog)
                                 .map(schemaPrinter::print)
                                 .orElse("");

    assertThat(result).contains("schema {\n" +
                                "   query : " + QUERY_TYPE_NAME + "\n" +
                                "   mutation : " + MUTATION_TYPE_NAME + "\n" +
                                "}\n")
                      .contains("type " + MUTATION_TYPE_NAME + " {\n" +
                                "}\n")
                      .contains("type " + QUERY_TYPE_NAME + " {\n" +
                                "}\n")
                      .contains("type table_name_1 {\n" +
                                "   field_name_1 : [Int]\n" +
                                "   field_name_2 : String\n" +
                                "}\n")
                      .contains("type table_name_2 {\n" +
                                "   field_name_3 : [Int]!\n" +
                                "   field_name_4 : String!\n" +
                                "}\n");

  }

  @Test
  public void testMutationWithBuilderAndSchema() {

    when(schema.tableStream()).thenReturn(Stream.empty());
    final GraphQLObjectType.Builder builder = newObject().name(MUTATION_TYPE_NAME);

    final GraphQLObjectType.Builder result = subject.mutation(builder, schema);

    assertThat(result).isSameAs(builder);

    final GraphQLObjectType graphQLObjectType = builder.build();

    assertThat(schemaPrinter.print(graphQLObjectType)).isEqualToIgnoringWhitespace("type " + MUTATION_TYPE_NAME + " {\n" +
                                                                                   "}");
  }

  @Test
  public void testMutationWithBuilderAndTable() {

    final GraphQLObjectType.Builder builder = newObject().name(MUTATION_TYPE_NAME);

    final GraphQLObjectType.Builder result = subject.mutation(builder, table);

    assertThat(result).isSameAs(builder);

    final GraphQLObjectType graphQLObjectType = builder.build();

    assertThat(schemaPrinter.print(graphQLObjectType)).isEqualToIgnoringWhitespace("type " + MUTATION_TYPE_NAME + " {\n" +
                                                                                   "}");
  }

  @Test
  public void testMutationWithCatalog() {

    when(catalog.schemaStream()).thenReturn(Stream.of(schema, schema));
    when(schema.tableStream()).thenReturn(Stream.empty())
                              .thenReturn(Stream.empty());

    final GraphQLObjectType result = subject.mutation(catalog);

    assertThat(schemaPrinter.print(result)).isEqualToIgnoringWhitespace("type " + MUTATION_TYPE_NAME + " {\n" +
                                                                        "}");
  }

  @Test
  public void testQueryWithBuilderAndSchema() {

    when(schema.tableStream()).thenReturn(Stream.empty());
    final GraphQLObjectType.Builder builder = newObject().name(QUERY_TYPE_NAME);

    final GraphQLObjectType.Builder result = subject.query(builder, schema);

    assertThat(result).isSameAs(builder);

    final GraphQLObjectType graphQLObjectType = builder.build();

    assertThat(schemaPrinter.print(graphQLObjectType)).isEqualToIgnoringWhitespace("type " + QUERY_TYPE_NAME + " {\n" +
                                                                                   "}");
  }

  @Test
  public void testQueryWithBuilderAndTable() {

    final GraphQLObjectType.Builder builder = newObject().name(QUERY_TYPE_NAME);

    final GraphQLObjectType.Builder result = subject.query(builder, table);

    assertThat(result).isSameAs(builder);

    final GraphQLObjectType graphQLObjectType = builder.build();

    assertThat(schemaPrinter.print(graphQLObjectType)).isEqualToIgnoringWhitespace("type " + QUERY_TYPE_NAME + " {\n" +
                                                                                   "}");
  }

  @Test
  public void testQueryWithCatalog() {

    when(catalog.schemaStream()).thenReturn(Stream.of(schema, schema));
    when(schema.tableStream()).thenReturn(Stream.empty())
                              .thenReturn(Stream.empty());

    final GraphQLObjectType result = subject.query(catalog);

    assertThat(schemaPrinter.print(result)).isEqualToIgnoringWhitespace("type " + QUERY_TYPE_NAME + " {\n" +
                                                                        "}");
  }

}
