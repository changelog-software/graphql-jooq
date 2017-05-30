package software.changelog.graphql_jooq.language;

import static org.apache.commons.lang3.reflect.FieldUtils.readDeclaredField;
import static org.apache.commons.lang3.reflect.FieldUtils.writeDeclaredField;
import static org.assertj.core.api.Assertions.assertThat;

import static java.time.Month.DECEMBER;

import org.junit.Before;
import org.junit.Test;

import graphql.language.BooleanValue;
import graphql.language.Node;

import java.time.LocalDate;
import java.util.List;

public class DateValueTest {

  private DateValue subject;

  @Before
  public void setUp() {

    subject = new DateValue();
  }

  @Test
  public void testGetChildren() {

    final List<Node> result = subject.getChildren();

    assertThat(result).isEmpty();
  }

  @Test
  public void testGetValue() throws NoSuchFieldException, IllegalAccessException {

    final LocalDate localDate = LocalDate.of(2007, DECEMBER, 3);
    writeDeclaredField(subject, "value", localDate, true);

    final LocalDate result = subject.getValue();

    assertThat(result).isEqualTo(localDate);
  }

  @Test
  public void testIsEqualToWithDifferentType() {

    final boolean result = subject.isEqualTo(new BooleanValue(true));

    assertThat(result).isFalse();
  }

  @Test
  public void testIsEqualToWithDifferentValues() {

    final LocalDate localDate = LocalDate.of(2007, DECEMBER, 3);
    subject.setValue(localDate);

    final boolean result = subject.isEqualTo(new DateValue(LocalDate.now()));

    assertThat(result).isFalse();
  }

  @Test
  public void testIsEqualToWithEquivalent() {

    final LocalDate localDate = LocalDate.of(2007, DECEMBER, 3);
    subject.setValue(localDate);

    final boolean result = subject.isEqualTo(new DateValue(localDate));

    assertThat(result).isTrue();
  }

  @Test
  public void testIsEqualToWithNull() {

    final boolean result = subject.isEqualTo(null);

    assertThat(result).isFalse();
  }

  @Test
  public void testIsEqualToWithNullAndNonnullValues() {

    final boolean result = subject.isEqualTo(new DateValue(LocalDate.now()));

    assertThat(result).isFalse();
  }

  @Test
  public void testIsEqualToWithSelf() {

    final boolean result = subject.isEqualTo(subject);

    assertThat(result).isTrue();
  }

  @Test
  public void testSetValue() throws NoSuchFieldException, IllegalAccessException {

    final LocalDate localDate = LocalDate.of(2007, DECEMBER, 3);
    subject.setValue(localDate);

    final Object result = readDeclaredField(subject, "value", true);

    assertThat(result).isEqualTo(localDate);
  }

  @Test
  public void testToString() {

    final LocalDate localDate = LocalDate.of(2007, DECEMBER, 3);
    subject.setValue(localDate);

    final String result = subject.toString();

    assertThat(result).isEqualTo("DateValue{value=2007-12-03}");
  }

}
