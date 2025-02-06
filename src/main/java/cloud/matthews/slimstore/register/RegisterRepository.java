package cloud.matthews.slimstore.register;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cloud.matthews.slimstore.store.Store;

@Repository
public interface RegisterRepository extends CrudRepository<Register, Integer> {

    Register findByStoreAndNumber(
        Store store,
        Integer number
    );
    
    Register getReferenceById(
        Integer id
    );
    
}
