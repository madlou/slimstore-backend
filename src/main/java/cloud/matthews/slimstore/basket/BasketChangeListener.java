package cloud.matthews.slimstore.basket;

/**
 * Implemented by components that need to react whenever the basket's
 * contents change (add / void / empty), without the {@code basket}
 * package needing to depend on the {@code display} package that
 * actually pushes the update to connected customer displays.
 */
public interface BasketChangeListener {

    void onBasketChanged(Basket basket);

}
