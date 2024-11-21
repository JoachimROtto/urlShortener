## About
The URLShortener is just a small exercise. For security reasons, no one should use something like this.
It exposes 4 endpoints:

### POST
http://**localURL**/shorten
to post something like the following JSON
{"text" : "https://example.com"}

### GET
http://**localURL**/**id**
to get a redirect as html

http://**localURL**/getID/**id**
to get the original URL as plain text
  
http://**localURL**/getURL/**id**
to get the original URL as JSON

and a web interface (http://**localURL**/ui/shortenui). Data are persisted in an H2 database.

## Installation
- Start it from the IDE
- maven: mvn exec:java -Dexec.mainClass="de.gfed.urlshortener.UrlshortenerApplication"
