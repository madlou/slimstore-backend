package cloud.matthews.slimstore.user;

import org.springframework.data.repository.CrudRepository;

import cloud.matthews.slimstore.store.Store;

public interface UserRepository extends CrudRepository<User, Integer> {
    
    User findByCode(
        String code
    );

    Iterable<User> findByStore(
        Store store
    );
    
    User getReferenceById(
        Integer id
    );
    
}
