@ParametersAreNonnullByDefault
@Value.Style(deepImmutablesDetection = true,
             get = {"get*",
                    "is*"},
             jdkOnly = true,
             passAnnotations = {Nonnull.class,
                                Nullable.class})
package software.changelog.graphql_jooq;

import org.immutables.value.Value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
