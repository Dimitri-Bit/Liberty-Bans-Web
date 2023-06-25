# Liberty Bans Web Interface

A simple web interface for the LibertyBans Minecraft Java Edition punishments plugin built in Java & Micronaut.

**Please Note:** This application relies on your LibertyBans plugin using a MySQL database for storage. More database variety support will be added in the future.

## Requirements

- Java 11
- LibertyBans plugin instance using a MySQL database

## Setup

1. Download the latest JAR file from [here](https://github.com/Dimitri-Fustic/Liberty-Bans-Web/releases).
2. Create a new folder for your application.
3. Run the JAR file.
4. Upon the first run, a `config.yml` file will be created to configure your database credentials.

The `config.yml` file will look something like this:

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

Configure the `config.yml` file as follows:

- Change the `database.url` to your database's host and port, and `db_name` to your database name.
- Replace `username` and `password` with your valid credentials.

### HikariCP

You can adjust the size of your database connection pool as well as their lifetime. The default configuration should be good for most people;

### Caffeine

Since LibertyBans doesn't always store a punished player's username, we manually look it up using Mojang's API and the player's UUID. This process takes some time, so we cache it using Caffeine for future responses. You can configure the number of username entries to save and the expiration time for each entry.

### Startup

After running your application for the first time, you will be prompted to start it with the following command in the future:
```
java -jar -Dmicronaut.config.files="config.yml" -jar application.jar
```
If you do not an error will occur so please make sure to remember this.

### Frontend

After running the application for the second time, a folder for the frontend files will appear. Here you can customize the look of your application or make a complete redesign. The frontend is built using the following technologies:

- Bootstrap
- jQuery
- HTML, CSS, & JS

After this short setup you're good to go!

## Endpoints

If you want to access the backend endpoints directly for any reason, the current endpoints are:

### The Punishments Endpoint

Endpoint: `/history/$type/$page`

- `$type` can be one of the following: ban, kick, mute, or warn.
- `$page` represents the desired page number. A maximum of 6 punishment items will appear per page, along with the total number of available pages.

A typical response from the endpoint can look like this:
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

## Images
![alt text](https://i.imgur.com/tQawmrN.png)
![alt text](https://i.imgur.com/Isk7xHt.png)
![alt text](https://i.imgur.com/v0vLGfo.png)

## Live Preview

Live Demo is coming soon.

## Support

If you need any support regarding this application, please open an issues request. A Discord support server is coming soon.

*Please Note: The official LibertyBans Discord server probably won't provide you support for this application since it is not an official web interface.*
