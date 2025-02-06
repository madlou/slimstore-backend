package cloud.matthews.slimstore.product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPriceRepository extends CrudRepository<ProductPrice, Integer> {
    
}
