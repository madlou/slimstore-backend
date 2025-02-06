package cloud.matthews.slimstore.product.barcode;

import lombok.Data;

@Data
public class BarcodeSpecification {

    String region;
    String type;
    String banner;
    Integer length;
    Integer division;
    Integer department;
    Integer category;
    Integer style;
    Integer price;
    Integer week;

}
