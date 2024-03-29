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

package org.opennms.features.demo.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.opennms.features.demo.api.AlarmCounter;
import org.opennms.features.demo.api.KiwiCounter;
import org.opennms.features.demo.api.NamedService;

// Executed in Karaf shell via opennms-demo:count
@Command(scope = "opennms-demo", name = "count", description = "Get the count of alarms and kiwis on this system.")
@Service
public class CounterCommand implements Action {
    @Reference
    private AlarmCounter alarmCounter;

    @Reference
    private KiwiCounter kiwiCounter;

    @Reference
    private NamedService namedService;

    @Override
    public Object execute() {
        System.out.println("The configured name is: " + namedService.getName());
        System.out.println("The count for alarms is: " + alarmCounter.getNumAlarms());
        System.out.println("The count for kiwi is: " + kiwiCounter.getNumKiwi());
        return null;
    }
}
