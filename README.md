# Liberty Bans Web Interface
A simple web interface for the LibertyBans Minecraft Java Edition punishments plugin built in Java & Micronaut. <br>

**Please Note: this application relies on your LibertyBans plugin using a MySQL database for storage, more database variety support will be added in the future.**<br>

## Requirements
 - Java 11
 - LibertyBans plugin instance using a MySQL database

## Setup
Download the latest jar file from [here](https://github.com/Dimitri-Fustic/Liberty-Bans-Web/releases), create a new folder for your application
and run the jar. When you run the application for the first time a config.yml file will be created so you can configure your database credentials.

The config.yml file will look something like this:

```yml
server:
  port: 8080

database:
  url: "jdbc:mysql://host:port/db_name"
  username: "username"
  password: "password"

  hikaricp:
    maxPoolSize: 6
    maxLifetime: 1800000
    idleTimeout: 600000
    connectionTimeout: 30000

caffeine:
  mojang:
    maxSize: 150
    expiration: "10m"
```

Simply change the database.url host:port to the host and port your database is running on and db_name to the database name. Also change the username & password with your valid credentials. There is additional configuration for hikaricp and caffeine which is explained below.

### HikariCP
You can change the size of your database connection pool as well as their lifetime, the current configuration should be good enough for most people.

### Caffeine:
Since LibertyBans doesn't always store a punished player's username we have to look it up manually using Mojang's API & the player's UUID. This does take some time so when we do look it up
we also cahce it using caffeine. You can configure how many username entiries you would like to save as well as after how long each entry will expire

### Startup
After running your application for the first time you will be prompted to start it with the following command in the future:
```
java -jar -Dmicronaut.config.files="config.yml" -jar application.jar
```
If you do not an error will occur so please make sure to remember this.

### Frontend
After running the application for second time the a folder for the frontend files will appear. Here you can configure the look of your website or change it outright.
The frontend is built using the following technologies:
- Bootstrap
- JQuery
- HTML, CSS & JS

After this short setup you're good to go!

## Endpoints
If you want to access the backend endpoints directly for whatever reason the current endpoints are:

### The punishments endpoint:<br>
**/history/$type/$page**

$type can be one of the following:
 - ban
 - kick
 - mute
 - warn

And $page is just the page number you want. A maximum of 6 punishment items will appear per page as well as a number indicating the maximum amount of pages
avaliable with each query.

A typical response can look like this:
```json
{
   "pageCount":3,
   "punishments":[
      {
         "victimUuid":"3904e547421343e394044add31e2202f",
         "victimUsername":"Joe",
         "operatorUuid":"f78a4d8dd51b4b3998a3230f2de0c670",
         "operatorUsername":"Console",
         "reason":"testing",
         "label":"Active",
         "active":true,
         "start":1684951627,
         "end":1685038027
      },
      {
         "victimUuid":"e68dd39a59514e329666c177e444df3c",
         "victimUsername":"Local",
         "operatorUuid":"f78a4d8dd51b4b3998a3230f2de0c670",
         "operatorUsername":"Console",
         "reason":"No reason stated.",
         "label":"Permanent",
         "active":true,
         "start":1684951137,
         "end":0
      },
   ]
}
```
## Live Preview:
You can find a live preview of the website [here](https://github.com/Dimitri-Fustic/Liberty-Bans-Web/).

## Support
If you need any support regarding this application please open a issues request, a Discord support server is coming soon.

*Please Note: The official LibertyBans Discord server probably won't provide you any support with this application since it is not a official web interface*
