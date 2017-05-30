package software.changelog.graphql_jooq.language;

import graphql.language.AbstractNode;
import graphql.language.Node;
import graphql.language.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DateTimeValue extends AbstractNode implements Value {

  @Nullable
  private LocalDateTime value;

  public DateTimeValue(final LocalDateTime value) {

    this();
    this.value = value;
  }

  public DateTimeValue() {

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

    final DateTimeValue that = (DateTimeValue) node;

    return value == null
           ? that.value == null
           : value.equals(that.value);
  }

  @Nullable
  public LocalDateTime getValue() {

    return value;
  }

  public void setValue(final LocalDateTime value) {

    this.value = value;
  }

  @Nonnull
  @Override
  public String toString() {

    return "DateTimeValue{value=" + value + '}';
  }

}
