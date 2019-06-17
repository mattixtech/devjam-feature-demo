Demo Steps
===
1. Give overview of the project structure, code and blueprints
1. Comment out the service entry in the Counter module blueprint
1. Install the demo project to the local maven repository
    ~~~
    cd /Users/opennms/vmshared/devjam-feature-demo
    mvn install
    ~~~
1. With OpenNMS running, connect to the Karaf shell
    ~~~
    ssh -p 8101 admin@localhost
    ~~~
1. Demonstrate that the features from our project are currently unavailable
    ~~~
    feature:list | grep -i demo
    ~~~
1. Add the feature repository for our project from our local Maven repository
    ~~~
    feature:repo-add mvn:org.opennms.features/org.opennms.features.demo-features/1.0.0-SNAPSHOT/xml
    ~~~
1. Demonstrate the features for our project are now available
    ~~~
    feature:list | grep -i demo
    ~~~
1. Install the feature for the counter (this also installs the API as a dependency)
    ~~~
    feature:install opennms-feature-demo-counter
    ~~~
1. The counter and api features should both now be started
    ~~~
    feature:list | grep -i demo
    ~~~
1. We can also check the bundle directly
    ~~~
    bundle:list | grep -i demo
    ~~~
1. The logs will show that our counter service was registered with the API layer
    ~~~
    log:tail
    ~~~
    Should look like this:
    > 12:11:23.341 DEBUG [features-3-thread-1] ServiceEvent REGISTERED - [org.opennms.integration.api.v1.alarms.AlarmLifecycleListener] - org.opennms.features.counter
      12:11:23.342 DEBUG [features-3-thread-1] bind called with org.opennms.features.counter.CounterImpl@287a7ef8: {service.id=954, osgi.service.blueprint.compname=counter, objectClass=[Ljava.lang.String;@70af4d4d, service.scope=bundle, service.bundleid=353}
1. Install our command feature
    ~~~
    feature:install opennms-feature-demo-command
    ~~~
1. We can see the command is waiting for the counter service to be available
    ~~~
    log:tail
    ~~~
1. We can see there is no counter service currently registered
    ~~~
    service:list | grep org.opennms.features.counter.api.Counter -A 10
    ~~~
1. Turn on bundle watch and watch the logs
    ~~~
    bundle:watch *
    log:tail
    ~~~
1. Fix the counter blueprint and re-install to Maven
    ~~~
    mvn install
    ~~~
1. Should see the command registered
    >  12:16:09.875 DEBUG [Thread-2257] ServiceEvent REGISTERED - [org.opennms.features.counter.api.Counter] - org.opennms.features.counter
    12:16:10.921 INFO [Thread-2257] Registering commands for bundle org.opennms.features.command/1.0.0.SNAPSHOT
1. Our service should now be present
    ~~~
    service:list | grep org.opennms.features.counter.api.Counter -A 10
    ~~~
1. Run the command to see the state of the CounterService
    ~~~
    opennms-demo:count
    ~~~
1. Change the name of the service
    ~~~
    config:edit org.opennms.features.counter.settings
    config:property-set name "Hello World"
    config:update
    ~~~
1. Show that the name has changed via the command
    ~~~
    opennms-demo:count
    ~~~
1. Install the supplier feature
    ~~~
    feature:install opennms-feature-demo-supplier
    ~~~
1. Show the supplier service
    ~~~
    service:list | grep org.opennms.features.counter.api.ThingSupplier -A 10
    ~~~
1. Watch the command to see the count increasing
    ~~~
    watch opennms-demo:count
    ~~~
1. Stop the supplier bundle
    ~~~
    bundle:list | grep -i demo
    bundle:stop NNN
    ~~~
1. The count should no longer be increasing
    ~~~
    watch opennms-demo:count
    ~~~
1. Start the bundle again and see the count is increasing again
    ~~~
    bundle:start NNN
    watch opennms-demo:count
    ~~~
1. Show that we are able to see alarms counted in real time
    ~~~
    ~/devjam/trigger_alarms.sh
    ~~~