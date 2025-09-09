package fava.betaja.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest<T> {
    private int pageSize;
    private int currentPage;
    private String sortBy;
    @Nullable
    private T value;
    @Nullable
    private UUID extended;

    @Nullable
    public T getValue() {
        return value;
    }

    public void setValue(@Nullable T value) {
        this.value = value;
    }

    @Nullable
    public UUID getExtended() {
        return extended;
    }

    public void setExtended(@Nullable UUID extended) {
        this.extended = extended;
    }
}

