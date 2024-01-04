
# Liberty Web Docs
Welcome to the LibertyBans Web Interface Documentation.

## Installation Guide
- Download the latest version of Liberty Web [here](https://github.com/Dimitri-Bit/Liberty-Bans-Web/releases)
- Create a new folder in which you will run your Liberty Web server (If you are using a shared hosting panel such as Pterodactyl simply upload the jar to your server)
- Copy your LibertyBans plugin folder and place it in the same directory as your Liberty Web jar (the plugin folder should be configured with the external database connection credentials)
![Directory Example Image](img/image_1.png)

- Start the Liberty Web jar `java -jar Liberty_Web-VERSION.jar`, when you start Liberty Web for the first time a config.yml file will be created which you can then configure. The config looks something like this:

```yaml
server:
  # On what port the server will run
  port: 8080
  host: "0.0.0.0"

# Cache configuration
caffeine:
  # Some usernames aren't saved in the database, so we must retrieve them using Mojang's API
  # The provided configuration should be suitable for most users
  mojang:
    # Maximum amount of usernames to cache
    maxSize: 150
    # After what time cache record will expire
    expiration: "10m"
```

- After you're done editing your config.yml start Liberty Web once again, on the second startup a new folder called frontend will be created. In this folder you can edit or completely change the style of your website to suit your needs. If you wish to create a completely new frontend you should learn some more about the backend on [this guide](endpoints.md).
- You are done, you can now view your punishments online. Please note that this is meant to be a public facing website to allow all your players to publicly view your server's punishments. Some features meant for administration use of the server should be added in future updates so stay tuned.

***NOTE: It is highly recommended to create a new user with read-only access to your punishments database.***

## Reverse Proxy

After you setup Libertybans Web, you will probably want it to be available to https, on a domain. A reverse proxy allows you to run infinite web servers on the same port by proxying the requests.

The following are the requirements for doing so:

1. A VPS/Dedicated Server.
2. Basic knowledge with linux.
3. A (sub)domain.
4. You are not able to do that on any hosting with a simple panel like pterodactyl. 
5. Run LibertyWeb on a port that is not 443 or 80, as they will be used by the reverse proxy.
6. Running LibertyWeb on the same machine (very recommended, but not required).

### Installing NGINX

Note: You do not need to install it if you are running pterodactyl for example.

The following is how to install Nginx on Debian-Based linux distributions. 

```sh
sudo apt update
sudo apt-get install nginx
sudo systemctl enable --now nginx
sudo systemctl start nginx
```

Run the following to check if Nginx was installed & running properly:

```sh
curl 127.0.0.1:80
```

If it prints an HTML, then it's working properly.
### Installing certbot
The following is how to install certbot on Debian-Based linux distributions.

```sh
sudo apt update
sudo apt install -y certbot
sudo apt install -y python3-certbot-nginx
```

### Creating a Certificate
For HTTPS to be secure, a certificate is required. You need to do the following before proceeding.

1. Set the domain you want to use to A record, and set the IP to your VPS/Dedicated Server IP. If you're using CloudFlare, disable proxy mode (the cloud icon) unless you know what you're doing.
2. Make sure the port 443 and 80 are allowed through your firewall (443 is required for the web server to stay working.)

Run the following command to create the certificate:

```sh
certbot certonly --nginx -d <DOMAIN>
```
- Replace <DOMAIN> with your domain or subdomain.

### Creating the NGINX configuration file.
SFTP into your machine. Go to this directory `/etc/nginx/sites-available` and create a new file and call it `libertyweb.conf`

Open the file and set the file content to this, replace <DOMAIN> with your domain, and <PORT> with the port running LibertyWeb.

```nginxconf
server {
    listen 443 ssl;
    server_name <DOMAIN>;

    ssl_certificate /etc/letsencrypt/live/<DOMAIN>/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/<DOMAIN>/privkey.pem;

    location / {
        proxy_pass http://<HOST>:<PORT>;
        proxy_set_header Host $host;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

- If LibertyWeb is not on the same machine, replace `127.0.0.1` with the IP required to reach it.

#### Enabling Configuration
The final step is to enable your NGINX configuration and restart it.

```sh
sudo ln -s /etc/nginx/sites-available/libertyweb.conf /etc/nginx/sites-enabled/libertyweb.conf
sudo systemctl restart nginx
```

### Done!
It should be accessible from your domain now, with https!
