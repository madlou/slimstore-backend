package cloud.matthews.slimstore.register;

import org.springframework.data.repository.CrudRepository;

import cloud.matthews.slimstore.store.Store;

public interface RegisterRepository extends CrudRepository<Register, Integer> {

    Register findByStoreAndNumber(
        Store store,
        Integer number
    );
    
    Register getReferenceById(
        Integer id
    );
    
}
