package software.changelog.graphql_jooq.internal;

import static software.changelog.graphql_jooq.internal.GraphQLFieldDefinitionGeneratorImpl.NO_OP_DATA_FETCHER;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;

import org.jooq.DataType;
import org.jooq.Field;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import graphql.schema.GraphQLFieldDefinition;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class GraphQLFieldDefinitionGeneratorImplTest {

  @Mock
  private DataType<?> dataType;

  @Mock
  private Field<?> field;

  private GraphQLFieldDefinitionGeneratorImpl subject;

  @Before
  public void setUp() {

    subject = new GraphQLFieldDefinitionGeneratorImpl();
  }

  @Test
  public void testGenerateWithFieldAndNameGeneratorAndType() {

    when(field.getName()).thenReturn("name");
    when(field.getComment()).thenReturn("comment");
    final GraphQLFieldDefinition expected = newFieldDefinition().name("name")
                                                                .description("comment")
                                                                .type(GraphQLString)
                                                                .build();

    final GraphQLFieldDefinition result = subject.generate(field, GraphQLString);

    assertThat(result).isEqualToComparingFieldByFieldRecursively(expected);
  }

  @Test
  public void testGenerateWithFieldWhenTypeIsKnown() {

    when(field.getName()).thenReturn("name");
    when(field.getComment()).thenReturn("comment");
    doReturn(dataType).when(field)
                      .getDataType();
    when(dataType.nullable()).thenReturn(true);
    doReturn(String.class).when(dataType)
                          .getType();

    final GraphQLFieldDefinition expected = newFieldDefinition().name("name")
                                                                .description("comment")
                                                                .type(GraphQLString)
                                                                .dataFetcher(NO_OP_DATA_FETCHER)
                                                                .build();

    final Optional<GraphQLFieldDefinition> result = subject.generate(field);

    assertThat(result).usingFieldByFieldValueComparator()
                      .contains(expected);
  }

  @Test
  public void testGenerateWithFieldWhenTypeIsUnknown() {

    doReturn(dataType).when(field)
                      .getDataType();
    doReturn(Object.class).when(dataType)
                          .getType();

    final Optional<GraphQLFieldDefinition> result = subject.generate(field);

    assertThat(result).isEmpty();
  }

}
