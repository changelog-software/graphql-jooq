package software.changelog.graphql_jooq.language;

import graphql.language.AbstractNode;
import graphql.language.Node;
import graphql.language.Value;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DateValue extends AbstractNode implements Value {

  @Nullable
  private LocalDate value;

  public DateValue(final LocalDate value) {

    this();
    this.value = value;
  }

  public DateValue() {

    super();
  }

  @Nonnull
  @Override
  public List<Node> getChildren() {

    return new ArrayList<>();
  }

  @Override
  public boolean isEqualTo(final Node node) {

    if (this == node) {
      return true;
    }

    if (node == null || getClass() != node.getClass()) {
      return false;
    }

    final DateValue that = (DateValue) node;

    return value == null
           ? that.value == null
           : value.equals(that.value);
  }

  @Nullable
  public LocalDate getValue() {

    return value;
  }

  public void setValue(final LocalDate value) {

    this.value = value;
  }

  @Nonnull
  @Override
  public String toString() {

    return "DateValue{value=" + value + '}';
  }

}
