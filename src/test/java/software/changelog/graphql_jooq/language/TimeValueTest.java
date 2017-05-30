package software.changelog.graphql_jooq.language;

import static org.apache.commons.lang3.reflect.FieldUtils.readDeclaredField;
import static org.apache.commons.lang3.reflect.FieldUtils.writeDeclaredField;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import graphql.language.BooleanValue;
import graphql.language.Node;

import java.time.LocalTime;
import java.util.List;

public class TimeValueTest {

  private TimeValue subject;

  @Before
  public void setUp() {

    subject = new TimeValue();
  }

  @Test
  public void testGetChildren() {

    final List<Node> result = subject.getChildren();

    assertThat(result).isEmpty();
  }

  @Test
  public void testGetValue() throws NoSuchFieldException, IllegalAccessException {

    final LocalTime localTime = LocalTime.of(10, 15, 30);
    writeDeclaredField(subject, "value", localTime, true);

    final LocalTime result = subject.getValue();

    assertThat(result).isEqualTo(localTime);
  }

  @Test
  public void testIsEqualToWithDifferentType() {

    final boolean result = subject.isEqualTo(new BooleanValue(true));

    assertThat(result).isFalse();
  }

  @Test
  public void testIsEqualToWithDifferentValues() {

    final LocalTime localTime = LocalTime.of(10, 15, 30);
    subject.setValue(localTime);

    final boolean result = subject.isEqualTo(new TimeValue(LocalTime.now()));

    assertThat(result).isFalse();
  }

  @Test
  public void testIsEqualToWithEquivalent() {

    final LocalTime localTime = LocalTime.of(10, 15, 30);
    subject.setValue(localTime);

    final boolean result = subject.isEqualTo(new TimeValue(localTime));

    assertThat(result).isTrue();
  }

  @Test
  public void testIsEqualToWithNull() {

    final boolean result = subject.isEqualTo(null);

    assertThat(result).isFalse();
  }

  @Test
  public void testIsEqualToWithNullAndNonnullValues() {

    final boolean result = subject.isEqualTo(new TimeValue(LocalTime.now()));

    assertThat(result).isFalse();
  }

  @Test
  public void testIsEqualToWithSelf() {

    final boolean result = subject.isEqualTo(subject);

    assertThat(result).isTrue();
  }

  @Test
  public void testSetValue() throws NoSuchFieldException, IllegalAccessException {

    final LocalTime localTime = LocalTime.of(10, 15, 30);
    subject.setValue(localTime);

    final Object result = readDeclaredField(subject, "value", true);

    assertThat(result).isEqualTo(localTime);
  }

  @Test
  public void testToString() {

    final LocalTime localTime = LocalTime.of(10, 15, 30);
    subject.setValue(localTime);

    final String result = subject.toString();

    assertThat(result).isEqualTo("TimeValue{value=10:15:30}");
  }

}
