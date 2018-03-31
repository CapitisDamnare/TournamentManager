package tapsi.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer pattern together with the {@link ControllerInterface}.
 */
public class Observer {
    private static List<ControllerInterface> listeners = new ArrayList<ControllerInterface>();

    /**
     * Adds listener to the controller.
     *
     * @param toAdd {@link ControllerInterface}
     */
    public static void addListener(ControllerInterface toAdd) {
        listeners.add(toAdd);
    }

    /**
     * Group settings button is pressed.
     */
    public static void btnGroupSettings() {
        for (ControllerInterface hl : listeners)
            hl.onBtnGroupSettings();
    }
}
