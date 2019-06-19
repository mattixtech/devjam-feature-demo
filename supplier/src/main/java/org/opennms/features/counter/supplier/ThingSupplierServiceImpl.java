/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.features.counter.supplier;

import java.util.concurrent.atomic.AtomicLong;

import org.opennms.features.counter.api.ThingHandler;
import org.opennms.features.counter.api.ThingSupplierService;

public class ThingSupplierServiceImpl implements ThingSupplierService {
    private ThingHandler thingHandler;
    private boolean keepRunning = true;
    private final AtomicLong numThings = new AtomicLong(50);

    private Thread thingAddingLoop = new Thread(() -> {
        while (keepRunning) {
            try {
                if (thingHandler != null) {
                    numThings.incrementAndGet();
                    thingHandler.handleThingAdded();
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    });

    // When OSGi tells us to start we can start our thread
    public void init() {
        thingAddingLoop.start();
    }

    // When OSGi tells us we've been stopped we must stop our thread
    public void destroy() {
        keepRunning = false;
    }

    public void registerThingHandler(ThingHandler thingHandler) {
        this.thingHandler = thingHandler;
    }

    @Override
    public long getNumThings() {
        return numThings.get();
    }
}
