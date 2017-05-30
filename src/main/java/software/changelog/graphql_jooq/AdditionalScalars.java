package software.changelog.graphql_jooq;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

import software.changelog.graphql_jooq.language.DateTimeValue;
import software.changelog.graphql_jooq.language.DateValue;
import software.changelog.graphql_jooq.language.TimeValue;

import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class AdditionalScalars {

  @Nonnull
  public static final GraphQLScalarType GraphQLDate = new GraphQLScalarType("Date", "A date without a time-zone in the ISO-8601 calendar system, such as '2007-12-03'", new Coercing<LocalDate, LocalDate>() {

    @Nullable
    @Override
    public LocalDate serialize(@Nullable final Object input) {

      if (input instanceof LocalDate) {
        return (LocalDate) input;
      }

      if (input instanceof CharSequence) {
        return LocalDate.parse((CharSequence) input, ISO_LOCAL_DATE);
      }

      return null;
    }

    @Nullable
    @Override
    public LocalDate parseValue(@Nullable final Object input) {

      return serialize(input);
    }

    @Nullable
    @Override
    public LocalDate parseLiteral(@Nullable final Object input) {

      return input instanceof DateValue
             ? ((DateValue) input).getValue()
             : null;
    }

  });

  @Nonnull
  public static final GraphQLScalarType GraphQLDateTime = new GraphQLScalarType("DateTime", "A date-time without a time-zone in the ISO-8601 calendar system, such as '2007-12-03T10:15:30'", new Coercing<LocalDateTime, LocalDateTime>() {

    @Nullable
    @Override
    public LocalDateTime serialize(@Nullable final Object input) {

      if (input instanceof LocalDateTime) {
        return (LocalDateTime) input;
      }

      if (input instanceof CharSequence) {
        return LocalDateTime.parse((CharSequence) input, ISO_LOCAL_DATE_TIME);
      }

      return null;
    }

    @Nullable
    @Override
    public LocalDateTime parseValue(@Nullable final Object input) {

      return serialize(input);
    }

    @Nullable
    @Override
    public LocalDateTime parseLiteral(@Nullable final Object input) {

      return input instanceof DateTimeValue
             ? ((DateTimeValue) input).getValue()
             : null;
    }

  });

  @Nonnull
  public static final GraphQLScalarType GraphQLTime = new GraphQLScalarType("Time", "A time without a time-zone in the ISO-8601 calendar system, such as '10:15:30'", new Coercing<LocalTime, LocalTime>() {

    @Nullable
    @Override
    public LocalTime serialize(@Nullable final Object input) {

      if (input instanceof LocalTime) {
        return (LocalTime) input;
      }

      if (input instanceof CharSequence) {
        return LocalTime.parse((CharSequence) input, ISO_LOCAL_TIME);
      }

      return null;
    }

    @Nullable
    @Override
    public LocalTime parseValue(@Nullable final Object input) {

      return serialize(input);
    }

    @Nullable
    @Override
    public LocalTime parseLiteral(@Nullable final Object input) {

      return input instanceof TimeValue
             ? ((TimeValue) input).getValue()
             : null;
    }

  });

  private AdditionalScalars() {

    // Nothing to see here
  }

}
