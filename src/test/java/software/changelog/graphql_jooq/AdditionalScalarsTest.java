package software.changelog.graphql_jooq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import static java.time.Month.DECEMBER;

import software.changelog.graphql_jooq.language.DateTimeValue;
import software.changelog.graphql_jooq.language.DateValue;
import software.changelog.graphql_jooq.language.TimeValue;

import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;

import graphql.schema.Coercing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AdditionalScalarsTest {

  public static class GraphQLDateTest {

    private Coercing subject;

    @Before
    public void setUp() {

      subject = AdditionalScalars.GraphQLDate.getCoercing();
    }

    @Test
    public void testParseLiteralWithDateValue() {

      final LocalDate localDate = LocalDate.of(2007, DECEMBER, 3);

      final DateValue dateValue = new DateValue();
      dateValue.setValue(localDate);

      final Object result = subject.parseLiteral(dateValue);

      assertThat(result).isEqualTo(localDate);
    }

    @Test
    public void testParseLiteralWithOtherType() {

      final Object result = subject.parseLiteral("value");

      assertThat(result).isNull();
    }

    @Test
    public void testParseValueWithInvalidString() {

      final ThrowableAssert.ThrowingCallable callable = () -> subject.parseValue("invalid");

      assertThatExceptionOfType(DateTimeParseException.class).isThrownBy(callable);
    }

    @Test
    public void testParseValueWithLocalDate() {

      final LocalDate localDate = LocalDate.of(2007, DECEMBER, 3);

      final Object result = subject.parseValue(localDate);

      assertThat(result).isEqualTo(localDate);
    }

    @Test
    public void testParseValueWithOtherType() {

      final Object result = subject.parseValue(new Object());

      assertThat(result).isNull();
    }

    @Test
    public void testParseValueWithString() {

      final LocalDate localDate = LocalDate.of(2007, DECEMBER, 3);

      final Object result = subject.parseValue("2007-12-03");

      assertThat(result).isEqualTo(localDate);
    }

    @Test
    public void testSerializeWithInvalidString() {

      final ThrowableAssert.ThrowingCallable callable = () -> subject.serialize("invalid");

      assertThatExceptionOfType(DateTimeParseException.class).isThrownBy(callable);
    }

    @Test
    public void testSerializeWithLocalDate() {

      final LocalDate localDate = LocalDate.of(2007, DECEMBER, 3);

      final Object result = subject.serialize(localDate);

      assertThat(result).isEqualTo(localDate);
    }

    @Test
    public void testSerializeWithOtherType() {

      final Object result = subject.serialize(new Object());

      assertThat(result).isNull();
    }

    @Test
    public void testSerializeWithValidString() {

      final LocalDate localDate = LocalDate.of(2007, DECEMBER, 3);

      final Object result = subject.serialize("2007-12-03");

      assertThat(result).isEqualTo(localDate);
    }

  }

  public static class GraphQLDateTimeTest {

    private Coercing subject;

    @Before
    public void setUp() {

      subject = AdditionalScalars.GraphQLDateTime.getCoercing();
    }

    @Test
    public void testParseLiteralWithDateTimeValue() {

      final LocalDateTime localDateTime = LocalDateTime.of(2007, DECEMBER, 3, 10, 15, 30);

      final DateTimeValue dateTimeValue = new DateTimeValue();
      dateTimeValue.setValue(localDateTime);

      final Object result = subject.parseLiteral(dateTimeValue);

      assertThat(result).isEqualTo(localDateTime);
    }

    @Test
    public void testParseLiteralWithOtherType() {

      final Object result = subject.parseLiteral("value");

      assertThat(result).isNull();
    }

    @Test
    public void testParseValueWithInvalidString() {

      final ThrowableAssert.ThrowingCallable callable = () -> subject.parseValue("invalid");

      assertThatExceptionOfType(DateTimeParseException.class).isThrownBy(callable);
    }

    @Test
    public void testParseValueWithLocalDateTime() {

      final LocalDateTime localDateTime = LocalDateTime.of(2007, DECEMBER, 3, 10, 15, 30);

      final Object result = subject.parseValue(localDateTime);

      assertThat(result).isEqualTo(localDateTime);
    }

    @Test
    public void testParseValueWithOtherType() {

      final Object result = subject.parseValue(new Object());

      assertThat(result).isNull();
    }

    @Test
    public void testParseValueWithString() {

      final LocalDateTime localDateTime = LocalDateTime.of(2007, DECEMBER, 3, 10, 15, 30);

      final Object result = subject.parseValue("2007-12-03T10:15:30");

      assertThat(result).isEqualTo(localDateTime);
    }

    @Test
    public void testSerializeWithInvalidString() {

      final ThrowableAssert.ThrowingCallable callable = () -> subject.serialize("invalid");

      assertThatExceptionOfType(DateTimeParseException.class).isThrownBy(callable);
    }

    @Test
    public void testSerializeWithLocalDateTime() {

      final LocalDateTime localDateTime = LocalDateTime.of(2007, DECEMBER, 3, 10, 15, 30);

      final Object result = subject.serialize(localDateTime);

      assertThat(result).isEqualTo(localDateTime);
    }

    @Test
    public void testSerializeWithOtherType() {

      final Object result = subject.serialize(new Object());

      assertThat(result).isNull();
    }

    @Test
    public void testSerializeWithValidString() {

      final LocalDateTime localDateTime = LocalDateTime.of(2007, DECEMBER, 3, 10, 15, 30);

      final Object result = subject.serialize("2007-12-03T10:15:30");

      assertThat(result).isEqualTo(localDateTime);
    }

  }

  public static class GraphQLTimeTest {

    private Coercing subject;

    @Before
    public void setUp() {

      subject = AdditionalScalars.GraphQLTime.getCoercing();
    }

    @Test
    public void testParseLiteralWithOtherType() {

      final Object result = subject.parseLiteral("value");

      assertThat(result).isNull();
    }

    @Test
    public void testParseLiteralWithTimeValue() {

      final LocalTime localTime = LocalTime.of(10, 15, 30);

      final TimeValue timeValue = new TimeValue();
      timeValue.setValue(localTime);

      final Object result = subject.parseLiteral(timeValue);

      assertThat(result).isEqualTo(localTime);
    }

    @Test
    public void testParseValueWithInvalidString() {

      final ThrowableAssert.ThrowingCallable callable = () -> subject.parseValue("invalid");

      assertThatExceptionOfType(DateTimeParseException.class).isThrownBy(callable);
    }

    @Test
    public void testParseValueWithLocalTime() {

      final LocalTime localTime = LocalTime.of(10, 15, 30);

      final Object result = subject.parseValue(localTime);

      assertThat(result).isEqualTo(localTime);
    }

    @Test
    public void testParseValueWithOtherType() {


      final Object result = subject.parseValue(new Object());

      assertThat(result).isNull();
    }

    @Test
    public void testParseValueWithString() {

      final LocalTime localTime = LocalTime.of(10, 15, 30);
      final Object result = subject.parseValue("10:15:30");

      assertThat(result).isEqualTo(localTime);
    }

    @Test
    public void testSerializeWithInvalidString() {

      final ThrowableAssert.ThrowingCallable callable = () -> subject.serialize("invalid");

      assertThatExceptionOfType(DateTimeParseException.class).isThrownBy(callable);
    }

    @Test
    public void testSerializeWithLocalTime() {

      final LocalTime localTime = LocalTime.of(10, 15, 30);

      final Object result = subject.serialize(localTime);

      assertThat(result).isEqualTo(localTime);
    }

    @Test
    public void testSerializeWithOtherType() {

      final Object result = subject.serialize(new Object());

      assertThat(result).isNull();
    }

    @Test
    public void testSerializeWithValidString() {

      final LocalTime localTime = LocalTime.of(10, 15, 30);

      final Object result = subject.serialize("10:15:30");

      assertThat(result).isEqualTo(localTime);
    }

  }

}
