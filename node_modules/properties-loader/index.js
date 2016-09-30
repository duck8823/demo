var loaderUtils = require("loader-utils");
module.exports = function(content) {
	var properties = {};
	var query = loaderUtils.parseQuery(this.query);
	content.replace(/\r\n|\r/g, '\n').split('\n').forEach(function(line){
		if(line && !line.match(/^#/) && (!query.regex || line.match(query.regex))){
			var prop = line.split('=', 2);
			properties[prop[0]] = prop[1];
		}
	});
	return 'module.exports = ' +  JSON.stringify(properties);
};