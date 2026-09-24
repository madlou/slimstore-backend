package cloud.matthews.slimstore.view;

/**
 * Abstraction implemented by the translation module so that {@code view}
 * can invoke translation behavior without depending on the {@code translation}
 * package, breaking the view &lt;-&gt; translation package cycle.
 */
public interface ViewTranslator {

    View translateView(View view);

}
