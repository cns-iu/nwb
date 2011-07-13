UrlNameListPair = function(name, url) {
	this.name = name;
	this.url = url;
}

Location = function (lat, lng, canonicalName, urlNameList) {
	this.lat = lat;
	this.lng = lng;
	this.canonicalName = canonicalName;
	this.urlNameList = urlNameList;
}