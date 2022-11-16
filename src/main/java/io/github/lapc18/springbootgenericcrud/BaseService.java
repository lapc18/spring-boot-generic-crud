package io.github.lapc18.springbootgenericcrud;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Base abstract Service which needs BaseEntity, BaseDto and JpaRepository to start working.
 * It wraps a basic CRUD operation using the JpaRepository.
 *
 * @see IService
 * @see BaseEntity
 * @see BaseDto
 * @see JpaRepository
 * @see <a href="https://github.com/lapc18/spring-boot-generic-crud">Repository</a>
 * @author @lapc18
 * */
public class BaseService<E extends BaseEntity, T extends BaseDto, R extends JpaRepository> implements IService<T> {

    /**
     * Generic repository instance to be set on child constructor.
     * @see JpaRepository
     */
    public R repository;

    /**
     * ModelMapper instance to be set on child constructor.
     * @see ModelMapper
     */
    public ModelMapper mapper;

    /**
     * Class<T> type to be set on child constructor.
     * @see Class<T>
     */
    public Class<T> tClass;

    /**
     * Class<E> type to be set on child constructor.
     * @see Class<E>
     */
    public Class<E> eClass;


    /**
     * @param tClass Class<T>
     * @param eClass Class<E>
     */
    public BaseService(Class<T> tClass, Class<E> eClass) {
        this.tClass = tClass;
        this.eClass = eClass;
    }

    /**
     * This operation create a new entity into the database.
     * @param dto T
     * @return T
     * @throws Exception
     */
    @Override
    public T create(T dto) throws Exception {
        if(dto == null) throw new Exception("Entity can not be empty!");

        try {
            var entity = this.mapper.map(dto, this.eClass);
            return this.mapper.map(this.repository.save(entity), this.tClass);
        } catch (Exception e) {
            throw new Exception("An exception has occurred saving your entity: ", e);
        }
    }

    /**
     * This operation updates the entity if exits.
     * @param dto T
     * @return T
     * @throws Exception
     */
    @Override
    public T update(T dto) throws Exception {
        if(dto == null) throw new Exception("Your entity can not be empty!");
        if(this.repository.findById(dto.getId()).isEmpty()) throw new Exception("No entity found with provided ID!");

        try {
            var entity = this.mapper.map(dto, this.eClass);
            return this.mapper.map(this.repository.save(entity), this.tClass);
        } catch (Exception e) {
            throw new Exception("An exception has occurred saving Product: ", e);
        }
    }

    /**
     * This operation return a single entity if exits.
     * @param id int
     * @return T
     * @throws Exception
     */
    @Override
    public T getById(int id) throws Exception {
        if(id == 0) throw new Exception("Entity ID can not be empty!");

        try {
            Optional<T> product = this.repository.findById(id);
            if(product.isEmpty()) return null;

            return this.mapper.map(product.get(), this.tClass);
        } catch (Exception e) {
            throw new Exception("An exception has occurred finding your Entity by code: ", e);
        }
    }

    /**
     * This operation retrieve all entities using a pageable parameter to paginate result.
     * The result will return all entities that is not softly deleted.
     * @param pageable Pageable
     * @return PageResponse<T>
     * @throws Exception
     */
    @Override
    public PageResponse<T> getAll(Pageable pageable) throws Exception {
        if(pageable == null) throw new Exception("Pageable can not be empty!");

        try {
            Page<E> data = this.repository.findAll(pageable);
            List<T> list = data.getContent()
                    .stream()
                    .filter((entity -> !entity.isDeleted()))
                    .map((entity -> this.mapper.map(entity, this.tClass)))
                    .collect(Collectors.toList());
            return new PageResponse<T>(list, data.getNumber(), data.getTotalElements(), data.getTotalPages());
        } catch (Exception e) {
            throw new Exception("An exception has occurred finding your entities: ", e);
        }
    }

    /**
     * This operation set the deleted property to TRUE of the entity to remove from retrieve queries.
     * @param id int
     * @return void
     * @throws Exception
     */
    @Override
    public void softDelete(int id) throws Exception {
        Optional<E> entity = this.repository.findById(id);
        if(entity.isEmpty()) throw new Exception("No entity found with provided ID!");

        var timestamp = Calendar.getInstance().get(Calendar.DATE);
        E entityFound = entity.get();
        entityFound.setDeleted(true);
        entityFound.setDeletedAt(timestamp);
        this.repository.save(entityFound);
    }

    /**
     * This operation remove the entity permanently from the DataBase.
     * @param id int
     * @return void
     * @throws Exception
     */
    @Override
    public void hardDelete(int id) throws Exception {
        Optional<T> p = this.repository.findById(id);
        if(p.isEmpty()) throw new Exception("Not entity found with provided ID!");

        this.repository.delete(p.get());
    }
}
