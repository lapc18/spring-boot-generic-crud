package io.github.lapc18.springbootgenericcrud;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Base abstract RestController which needs IService and BaseDto to start working.
 * It provides a basic CRUD operation.
 *
 * @see IService
 * @see BaseDto
 * @see <a href="https://github.com/lapc18/spring-boot-generic-crud">Repository</a>
 * @author @lapc18
 * */
public abstract class BaseController<T extends IService, E extends BaseDto> {

    /**
     * Generic service instance to be set on child constructor.
     * @see IService
     */
    public T service;


    /**
     * @param body E extends BaseDto
     * @return E extends BaseDto
     * @see BaseDto
     * @throws Exception
     */
    @PostMapping(value = "")
    public ResponseEntity<?> create(
            @RequestBody(required = true) E body
    ) throws Exception {
        try {
            return ResponseEntity.ok(this.service.create(body));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    /**
     * @param body E extends BaseDto
     * @return E extends BaseDto
     * @see BaseDto
     * @throws Exception
     */
    @PutMapping(value = "")
    public ResponseEntity<?> update(
            @RequestBody(required = true) E body
    ) throws Exception {
        try {
            return ResponseEntity.ok(this.service.update(body));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    /**
     * @param page int
     * @param size int
     * @return PageResponse<T>
     * @see PageResponse
     * @throws Exception
     */
    @GetMapping(value = "")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")  int size
    ) throws Exception {
        try {
            return ResponseEntity.ok(this.service.getAll(PageRequest.of(page, size)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    /**
     * @param id int
     * @return T | null
     * @throws Exception
     */
    @GetMapping(value = "")
    public ResponseEntity<?> getById(
            @RequestParam(required = true) int id
    ) throws Exception {
        try {
            return ResponseEntity.ok(this.service.getById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    /**
     * @param softDelete boolean
     * @param id int
     * @return HashMap<String, String>
     * @throws Exception
     */
    @DeleteMapping(value = "")
    public ResponseEntity<?> delete(
            @RequestParam(defaultValue = "true") boolean softDelete,
            @RequestParam() int id
    ) throws Exception {
        try {
            if(softDelete)
                this.service.softDelete(id);
            else
                this.service.hardDelete(id);

            var res = new HashMap<String, String>();
            res.put("status", "deleted");
            res.put("deleted", String.valueOf(id));

            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }


}
