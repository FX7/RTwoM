# RTwoM

## About

This is a Spigot/Paper plugin, which will offer access to the Bukkit API via REST calls.

For example you can get the last location of player *steve* via the simple curl call:

*curl -H "App-Name: testApp" -H "App-Key: 1234-abcd" -X GET "http://localhost:8080/rest/v1/player/steve/getLastLocation"*

## Features

I didnt find a plugin which offers the ability to make REST calls against the Bukkit API, but even if there is such plugin out there, RTwoM has two very interesting unique features:

* access control : Not only the app itself must have a valid access token, it is also possible to define which world the app can access and which player. In the future it may be possible to differ between read (eg getLocation) and write (eg setLocation) access and maybe also define coordinates for world access.
* atomic access and scripting : Each single REST call will try to be as small in its function as possible. Single Bukkit API methods should be represented in a single REST call. That said you may think this will lead to a heavy traffic while applying complex actions. But therefore this plugin offers the possibility to define scripts. With a script you can define a bunch of single REST calls, which will be then be accessable as one new REST call.

## Why

A friend of mine had the idea to provide a Andorid app, which would interact with a Spigot/Paper server, while he is moving around in the reald world. The use case he had in mind was, to motivate his childs for a walk, because at the end of the way, there will be some rare items as reward.

## Status quo

The plugin is in very early pre alpha state ;-) So there is a basic access control, few atomic REST calls and on example script.

## Way ahead

So many things todo, but the main effort will go to:

* improve the documentation ;-)
* create more atomic REST methods
* command handling for giving / removing access of an app
* REST possibility for gaining access 
* more / better access control (read/write access, coordinates ...)

## Examples

### http/rest Server config

The port and the host of the http/rest server can be defined in the *config.yml* in the *restServerConfig* block. Take a look at the config.yml in the "yaml config examples" folder.

### access control

Access tokens can also be defined in the *config.yml*. In the *appAccess* block you have to put the token and what is allowed for that token. Take a look at the config.yml in the "yaml config examples" folder.

### scripting

There is also one example script in the *scripts.yml*. The sript is called "rewardChest". It will get the last location of a player create a chest at that location and put some items in it.

### curl calls

curl -v -H "App-Name: testApp" -H "App-Key: 1234-abcd" -X GET "http://localhost:8080/rest/v1/player/steve/getLastLocation"

curl -v -H "App-Name: testApp" -H "App-Key: 1234-abcd" -H "Entity-Key: xyz-987" -X POST "http://localhost:8080/rest/v1/world/world/setMaterial?x=-130&y=72&z=202" --data "{'material':'CHEST'}"

curl -v -H "App-Name: testApp" -H "App-Key: 1234-abcd" -X POST "http://localhost:8080/rest/v1/world/world/addToContainer?x=-130&y=72&z=202" --data "{'itemStack':{'material':'DIAMOND','amount':'64'}}"

curl -v -H "App-Name: testApp" -H "App-Key: 1234-abcd" -X POST "http://localhost:8080/rest/v1/world/world/removeFromContainer?x=-130&y=72&z=202" --data "{'itemStack':{'material':'DIAMOND','amount':'63'}}"

curl -v -H "App-Name: testApp" -H "App-Key: 1234-abcd" -X GET "http://localhost:8080/rest/v1/world/world/getInventory?x=-130&y=72&z=202"

curl -v -H "App-Name: testApp" -H "App-Key: 1234-abcd" -X GET "http://localhost:8080/rest/v1/player/steve/getLastPlayed"

curl -v -H "App-Name: testApp" -H "App-Key: 1234-abcd" -X POST "http://localhost:8080/rest/v1/script/rewardChest/execute" --data "{'player':'steve','material':'CHEST','itemStack':{'material':'DIAMOND','amount':'64'}}"
