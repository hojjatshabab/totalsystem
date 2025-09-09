package fava.betaja.erp.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class PageResponse<T> {


    private List<T> rows;
    private int pageSize;
    private Long count;
    private int currentPage;
    private String sortBy;

    public PageResponse(List<T> rows, int pageSize, Long count, int currentPage, String sortBy) {
        this.rows = rows;
        this.pageSize = pageSize;
        this.count = count;
        this.currentPage = currentPage;
        this.sortBy = sortBy;
    }

    public PageResponse<?> PageableList(List<?> list, int currentPage, int pageSize, String sortBy) {
        int start = (currentPage - 1) * pageSize;
        int end = (currentPage * pageSize) - 1;
        long totalElements = list.size();
        if (start > totalElements)
            start = (int) totalElements;
        if (end > totalElements)
            end = (int) totalElements;
        List<?> content = list.subList(start, end);


        return new PageResponse<>(content, pageSize, totalElements, currentPage, sortBy);
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}

