package sample;

import java.util.ArrayList;
import java.util.List;

public class Observer {
    private static List<ControllerInterface> listeners = new ArrayList<ControllerInterface>();

    public static void addListener(ControllerInterface toAdd) {
        listeners.add(toAdd);
    }

    public static void btnGroupSettings() {
        for (ControllerInterface hl : listeners)
            hl.onBtnGroupSettings();
    }
}
