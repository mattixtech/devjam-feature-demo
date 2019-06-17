Useful commands used in this demo:
===

* Add a feature repository from maven: `feature:repo-add mvn:<group id>/<artifact id>/<version>/xml`
* List features: `feature:list`
* List bundles: `bundle:list`
* List services: `service:list`
* Install a feature: `feature:install <feature>`
* Watch the log: `log:tail`
* Hot-reload bundles if they change on disk: `bundle:watch *`
* Update a property:
~~~~
config:edit <persistent id>
config:property-set <propert name> <property value>
config:update
~~~~
