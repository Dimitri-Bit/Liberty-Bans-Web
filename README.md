# Liberty Web

Simple Web Interface for LibertyBans. Made in Java using the Micronaut Framework.

## Setup
This is a quick setup guide. A more extensive guide is in the making.

1. Download the latest stable version of Liberty Web from [releases](https://github.com/Dimitri-Bit/Liberty-Bans-Web/releases)
2. Copy your LibertyBans plugin's folder and paste it in the same directory as your Liberty Web jar (The LibertyBans plugin folder contains the credentials to access the plugin's external database)
3. Start the Liberty Web jar as any other jar `java -jar Liberty_Web-VERSION.jar`
4. Upon first startup you will be asked to configure the newly created file named config.yml
5. After you are satisfied with your config start Liberty Web again. Upon the second start you will be notified that a folder named frontend has been created, here you can configure the look of your website
6. You're done, make sure your website is working correctly and enjoy.

## Requirements
- Java 17
- LibertyBans External Database

## Support
If you need help feel free to ask in the #libertybans-web channel on [Discord](https://discord.gg/qgQbJMkcQw)
