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

package org.opennms.features.demo.supplier;

import java.util.concurrent.atomic.AtomicLong;

import org.opennms.features.demo.api.KiwiHandler;
import org.opennms.features.demo.api.KiwiSupplier;

public class KiwiSupplierImpl implements KiwiSupplier {
    private KiwiHandler kiwiHandler;
    private final AtomicLong numKiwi = new AtomicLong(0);
    private final int kiwiPerSecond = 1;

    private Thread thingAddingLoop = new Thread(() -> {
        while (true) {
            try {
                for (int i = 0; i < kiwiPerSecond; i++) {
                    numKiwi.incrementAndGet();
                    if (kiwiHandler != null) {
                        kiwiHandler.handleNewKiwi();
                    }
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
        thingAddingLoop.interrupt();
    }

    public void registerKiwiHandler(KiwiHandler kiwiHandler) {
        this.kiwiHandler = kiwiHandler;
    }

    @Override
    public long getNumKiwi() {
        return numKiwi.get();
    }
}
