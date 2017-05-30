package software.changelog.graphql_jooq.language;

import graphql.language.AbstractNode;
import graphql.language.Node;
import graphql.language.Value;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TimeValue extends AbstractNode implements Value {

  @Nullable
  private LocalTime value;

  public TimeValue(final LocalTime value) {

    this();
    this.value = value;
  }

  public TimeValue() {

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

    final TimeValue that = (TimeValue) node;

    return value == null
           ? that.value == null
           : value.equals(that.value);
  }

  @Nullable
  public LocalTime getValue() {

    return value;
  }

  public void setValue(final LocalTime value) {

    this.value = value;
  }

  @Nonnull
  @Override
  public String toString() {

    return "TimeValue{value=" + value + '}';
  }

}
