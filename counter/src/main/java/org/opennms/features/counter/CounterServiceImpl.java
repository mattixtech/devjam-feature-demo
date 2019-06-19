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

package org.opennms.features.counter;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.opennms.features.counter.api.CounterService;
import org.opennms.features.counter.api.ThingHandler;
import org.opennms.features.counter.api.ThingSupplierService;
import org.opennms.integration.api.v1.alarms.AlarmLifecycleListener;
import org.opennms.integration.api.v1.dao.AlarmDao;
import org.opennms.integration.api.v1.model.Alarm;

public class CounterServiceImpl implements AlarmLifecycleListener, CounterService {
    private final AtomicLong numAlarms = new AtomicLong(0);
    private final AtomicLong numThings = new AtomicLong(0);
    private final String name;

    // Obtained via integration API
    private final AlarmDao alarmDao;
    
    // How to react to our thing handler
    private final ThingHandler thingHandler = numThings::getAndIncrement;

    // These constructor parameters will be given to us via the definition in this bundle's blueprint.xml
    public CounterServiceImpl(AlarmDao alarmDao, String name) {
        this.alarmDao = Objects.requireNonNull(alarmDao);
        this.name = Objects.requireNonNull(name);
        initialize();
    }

    private void initialize() {
        numAlarms.set(alarmDao.getAlarmCount());
    }

    // OSGi will wire in any services implementing ThingSupplierService at runtime when they get registered
    public void bindThingSupplier(ThingSupplierService thingSupplierService) {
        thingSupplierService.registerThingHandler(thingHandler);
        numThings.getAndAdd(thingSupplierService.getNumThings());
    }

    public void handleNewOrUpdatedAlarm(Alarm alarm) {
        numAlarms.incrementAndGet();
    }

    public void handleDeletedAlarm(int i, String s) {
        numAlarms.decrementAndGet();
    }

    public long getNumAlarms() {
        return numAlarms.get();
    }

    public long getNumThings() {
        return numThings.get();
    }

    public String getName() {
        return name;
    }

    public void unbindThingSupplier(ThingSupplierService thingSupplierService) {
        // no-op
    }

    public void handleAlarmSnapshot(List<Alarm> list) {
        // no-op
    }
}
