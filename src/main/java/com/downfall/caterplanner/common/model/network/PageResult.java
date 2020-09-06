package com.downfall.caterplanner.common.model.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class PageResult<T> {
    private boolean isFinal;
    private T elements;

    public static <T> PageResult<T> of(Page page, int currentPage, T elements){
        return new PageResult<>(currentPage == page.getTotalPages() - 1, elements);
    }

    public static <T> PageResult<T> of(boolean isFinal, T elements){
        return new PageResult<>(isFinal, elements);
    }
}
