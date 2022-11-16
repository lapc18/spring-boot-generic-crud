package io.github.lapc18.springbootgenericcrud;

import org.springframework.data.domain.Pageable;

public interface IService<T> {
    T create(T dto) throws Exception;
    T update(T dto) throws Exception;
    void softDelete(int id) throws Exception;
    void hardDelete(int id) throws Exception;
    PageResponse<T> getAll(Pageable pageable) throws Exception;
    T getById(int id) throws Exception;
}
