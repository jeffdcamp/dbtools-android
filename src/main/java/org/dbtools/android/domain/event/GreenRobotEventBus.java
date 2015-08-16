package org.dbtools.android.domain.event;

import de.greenrobot.event.EventBus;
import org.dbtools.android.domain.DBToolsEventBus;

public class GreenRobotEventBus implements DBToolsEventBus {
    private EventBus bus;

    public GreenRobotEventBus(EventBus bus) {
        this.bus = bus;
    }

    public void post(Object event) {
        bus.post(event);
    }
}
