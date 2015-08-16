package org.dbtools.android.domain.event;

import com.squareup.otto.Bus;
import org.dbtools.android.domain.DBToolsEventBus;

public class OttoEventBus implements DBToolsEventBus {
    private Bus bus;

    public OttoEventBus(Bus bus) {
        this.bus = bus;
    }

    public void post(Object event) {
        bus.post(event);
    }
}
