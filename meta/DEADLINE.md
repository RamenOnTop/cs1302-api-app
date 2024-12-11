# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice-looking HTML.

## Part 1.1: App Description

> Please provide a friendly description of your app, including
> the primary functions available to users of the app. Be sure to
> describe exactly what APIs you are using and how they are connected
> in a meaningful way.

> **Also, include the GitHub `https` URL to your repository.**

My Api either takes the Users IP and custom entered IP or a set of pre set IP
and turns that into a Geolocation using a geojs API. Which returns a set of infomation
like thec city, region, country, latitude, and longitude of the IP. Then with the information
taken from that we can use the longitude and latitude if given a actual response to
call the Google maps Static map and Air quality heat map api to display two sets of images
also calling Weather Api from 7timer to give a visualization of the weather as well, and
api weather.gov aswell to display a description of the weather at a given time of day.

https://github.com/RamenOnTop/cs1302-api-app


## Part 1.2: API

> For each RESTful JSON API that your app uses (at least two are required),
> include an example URL for a typical request made by your app. If you
> need to include additional notes (e.g., regarding API keys or rate
> limits), then you can do that below the URL/URI. Placeholders for this
> information are provided below. If your app uses more than two RESTful
> JSON APIs, then include them with similar formatting.

### API 1

```
private String url = "https://api.weather.gov./points";

public void create URI(String... params) {
	this.uri = String.format("%s%s,%s" url, param[0], param[1]);
}
```

> Replace this line with notes (if needed) or remove it (if not needed).

### API 2

```
https://get.getojs.io/v1/ip/geo/

public void createURI(String... params) {
	this.rui = "https://get.geojs.io/v1/ip/geo/" + param[0] + ".json";
}
```

> Replace this line with notes (if needed) or remove it (if not needed).

## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

Something new or exicitying is how to use api's that require keys though
a lot of them also require you too pay, but that was cool also getting
more practice with JavaFX.

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

If I could start this project from scrach I would definitly look at the project
Description more closely and look at more API. Felt like I finally got freedom in
this class on what I could script up, and in return forgot about still being limitation
having to scamble back and fix to adhere the policy of the class. Also I would constanly publish my
git changes I may have accidently deleted my APIAPII file right before submitting but we dont talk
about that.
