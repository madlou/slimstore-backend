package cloud.matthews.slimstore.store;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends CrudRepository<Store, Integer> {

    Store findByNumber(
        Integer number
    );
    
    Store getReferenceById(
        Integer id
    );

}
