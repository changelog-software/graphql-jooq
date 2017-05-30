package software.changelog.graphql_jooq.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Table;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import graphql.schema.GraphQLObjectType;
import graphql.schema.idl.SchemaPrinter;

import java.util.Optional;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class GraphQLObjectTypeGeneratorImplTest {

  @Mock
  private DataType<?> dataType;

  @Mock
  private Field<?> field;

  private SchemaPrinter schemaPrinter;

  private GraphQLObjectTypeGeneratorImpl subject;

  @Mock
  private Table<?> table;

  @Before
  public void setUp() {

    schemaPrinter = new SchemaPrinter();

    subject = new GraphQLObjectTypeGeneratorImpl();
  }

  @Test
  public void testGenerateWithArrayField() {

    when(table.getName()).thenReturn("table_name");
    when(table.fieldStream()).thenReturn(Stream.of(field, field));
    when(field.getName()).thenReturn("field_name_1")
                         .thenReturn("field_name_2");
    doReturn(dataType).when(field)
                      .getDataType();
    when(dataType.isArray()).thenReturn(true)
                            .thenReturn(true);
    doReturn(String.class).doReturn(Integer.class)
                          .when(dataType)
                          .getArrayType();
    when(dataType.nullable()).thenReturn(true)
                             .thenReturn(true);

    final String result = subject.generate(table)
                                 .map(schemaPrinter::print)
                                 .orElse("");

    assertThat(result).isEqualToIgnoringWhitespace("type table_name {\n" +
                                                   "   field_name_1 : [String]\n" +
                                                   "   field_name_2 : [Int]\n" +
                                                   "}");
  }

  @Test
  public void testGenerateWithNoFields() {

    when(table.fieldStream()).thenReturn(Stream.empty());

    final Optional<GraphQLObjectType> result = subject.generate(table);

    assertThat(result).isEmpty();
  }

  @Test
  public void testGenerateWithNonNullArrayField() {

    when(table.getName()).thenReturn("table_name");
    when(table.fieldStream()).thenReturn(Stream.of(field, field));
    when(field.getName()).thenReturn("field_name_1")
                         .thenReturn("field_name_2");
    doReturn(dataType).doReturn(dataType)
                      .when(field)
                      .getDataType();
    when(dataType.isArray()).thenReturn(true)
                            .thenReturn(true);
    doReturn(String.class).doReturn(Integer.class)
                          .when(dataType)
                          .getArrayType();
    when(dataType.nullable()).thenReturn(false)
                             .thenReturn(false);

    final String result = subject.generate(table)
                                 .map(schemaPrinter::print)
                                 .orElse("");

    assertThat(result).isEqualToIgnoringWhitespace("type table_name {\n" +
                                                   "   field_name_1 : [String]!\n" +
                                                   "   field_name_2 : [Int]!\n" +
                                                   "}");
  }

  @Test
  public void testGenerateWithNonNullFild() {

    when(table.getName()).thenReturn("table_name");
    when(table.fieldStream()).thenReturn(Stream.of(field, field));
    when(field.getName()).thenReturn("field_name_1")
                         .thenReturn("field_name_2");
    doReturn(dataType).doReturn(dataType)
                      .when(field)
                      .getDataType();
    when(dataType.isArray()).thenReturn(false)
                            .thenReturn(false);
    doReturn(String.class).doReturn(Integer.class)
                          .when(dataType)
                          .getType();
    when(dataType.nullable()).thenReturn(false)
                             .thenReturn(false);

    final String result = subject.generate(table)
                                 .map(schemaPrinter::print)
                                 .orElse("");

    assertThat(result).isEqualToIgnoringWhitespace("type table_name {\n" +
                                                   "   field_name_1 : String!\n" +
                                                   "   field_name_2 : Int!\n" +
                                                   "}");
  }

  @Test
  public void testGenerateWithNullableField() {

    when(table.getName()).thenReturn("table_name");
    when(table.fieldStream()).thenReturn(Stream.of(field, field));
    when(field.getName()).thenReturn("field_name_1")
                         .thenReturn("field_name_2");
    doReturn(dataType).doReturn(dataType)
                      .when(field)
                      .getDataType();
    doReturn(String.class).doReturn(Integer.class)
                          .when(dataType)
                          .getType();
    when(dataType.nullable()).thenReturn(true)
                             .thenReturn(true);

    final String result = subject.generate(table)
                                 .map(schemaPrinter::print)
                                 .orElse("");

    assertThat(result).isEqualToIgnoringWhitespace("type table_name {\n" +
                                                   "   field_name_1 : String\n" +
                                                   "   field_name_2 : Int\n" +
                                                   "}");
  }

}
