package software.changelog.graphql_jooq.language;

import static org.apache.commons.lang3.reflect.FieldUtils.readDeclaredField;
import static org.apache.commons.lang3.reflect.FieldUtils.writeDeclaredField;
import static org.assertj.core.api.Assertions.assertThat;

import static java.time.Month.DECEMBER;

import org.junit.Before;
import org.junit.Test;

import graphql.language.BooleanValue;
import graphql.language.Node;

import java.time.LocalDateTime;
import java.util.List;

public class DateTimeValueTest {

  private DateTimeValue subject;

  @Before
  public void setUp() {

    subject = new DateTimeValue();
  }

  @Test
  public void testGetChildren() {

    final List<Node> result = subject.getChildren();

    assertThat(result).isEmpty();
  }

  @Test
  public void testGetValue() throws NoSuchFieldException, IllegalAccessException {

    final LocalDateTime localDateTime = LocalDateTime.of(2007, DECEMBER, 3, 10, 15, 30);
    writeDeclaredField(subject, "value", localDateTime, true);

    final LocalDateTime result = subject.getValue();

    assertThat(result).isEqualTo(localDateTime);
  }

  @Test
  public void testIsEqualToWithDifferentType() {

    final boolean result = subject.isEqualTo(new BooleanValue(true));

    assertThat(result).isFalse();
  }

  @Test
  public void testIsEqualToWithDifferentValues() {

    final LocalDateTime localDateTime = LocalDateTime.of(2007, DECEMBER, 3, 10, 15, 30);
    subject.setValue(localDateTime);

    final boolean result = subject.isEqualTo(new DateTimeValue(LocalDateTime.now()));

    assertThat(result).isFalse();
  }

  @Test
  public void testIsEqualToWithEquivalent() {

    final LocalDateTime localDateTime = LocalDateTime.of(2007, DECEMBER, 3, 10, 15, 30);
    subject.setValue(localDateTime);

    final boolean result = subject.isEqualTo(new DateTimeValue(localDateTime));

    assertThat(result).isTrue();
  }

  @Test
  public void testIsEqualToWithNull() {

    final boolean result = subject.isEqualTo(null);

    assertThat(result).isFalse();
  }

  @Test
  public void testIsEqualToWithNullAndNonnullValues() {

    final boolean result = subject.isEqualTo(new DateTimeValue(LocalDateTime.now()));

    assertThat(result).isFalse();
  }

  @Test
  public void testIsEqualToWithSelf() {

    final boolean result = subject.isEqualTo(subject);

    assertThat(result).isTrue();
  }

  @Test
  public void testSetValue() throws NoSuchFieldException, IllegalAccessException {

    final LocalDateTime localDateTime = LocalDateTime.of(2007, DECEMBER, 3, 10, 15, 30);
    subject.setValue(localDateTime);

    final Object result = readDeclaredField(subject, "value", true);

    assertThat(result).isEqualTo(localDateTime);
  }

  @Test
  public void testToString() {

    final LocalDateTime localDateTime = LocalDateTime.of(2007, DECEMBER, 3, 10, 15, 30);
    subject.setValue(localDateTime);

    final String result = subject.toString();

    assertThat(result).isEqualTo("DateTimeValue{value=2007-12-03T10:15:30}");
  }

}
