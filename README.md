
# Spring Boot basic Generic CRUD implementation

Sample project made in Spring Boot and MySQL with a Generic implementation to avoid reapeat yourself.





## Features

- Abstract base Entity
- Abstract base DTO
- Abstract base Service
- Abstract base Controller
- GET, POST, PUT, DELETE abstract operations into the Abstract Controller



## Usage/Examples

```java
//Controller
@RestController
@RequestMapping("/api/customers")
public class CustomersController extends BaseController<CustomerService, CustomerDto> {

    public CustomersController(CustomerService service) {
        this.service = service;
    }
}

//Service
@Service
public class CustomerService extends BaseService<Customer, CustomerDto, CustomersRepository> {

    @Autowired
    public CustomerService(CustomersRepository repository, ModelMapper mapper) {
        super(CustomerDto.class, Customer.class);
        this.repository = repository;
        this.mapper = mapper;
    }
}
```


## Authors

- Luis Adolfo Pimentel [[@lapc18](https://www.github.com/lapc18)]


## License

[MIT](https://choosealicense.com/licenses/mit/)

