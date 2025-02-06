package cloud.matthews.slimstore.transaction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionTenderRepository extends CrudRepository<TransactionTender, Integer> {
    
}
