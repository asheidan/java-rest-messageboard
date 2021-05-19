package se.ojoj.restnotes.pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonInclude(Include.NON_NULL)
public class PaginationWrapper<T> {

  @JsonProperty(value = "items")
  public List<T> page;

  @Schema(example = "1")
  public long total;
  public int offset;
  @Schema(example = "25")
  public int limit;

  public PaginationWrapper(List<T> page, int offset, int limit, long total) {
    this.page = page;
    this.offset = offset;
    this.limit = limit;
    this.total = total;
  }

  /**
   * <p>Implemented in order to user assertEquals in tests.</p>
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (getClass() != o.getClass()) {
      return false;
    }

    PaginationWrapper wrapper = (PaginationWrapper) o;

    return offset == wrapper.offset
        && limit == wrapper.limit
        && total == wrapper.total
        && page.equals(wrapper.page);
  }
}