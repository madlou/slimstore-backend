package cloud.matthews.slimstore.transaction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLineRepository extends CrudRepository<TransactionLine, Integer> {
    
}
