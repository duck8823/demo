(function($) {

	$(window).on("load", function(){

		$("#loader").css("display", "block");
		$("#fade").css("display", "block");

		var max = 0;
		var words = [];
		$.ajax({
			type: "GET",
			url: app.CONTEXT_ROOT + "/" + sns + "/wordCount",
			async: false,
			dataType: "text",
			success: function(data){
				words = $.parseJSON(data);
				for(var i=0; i<words.length; i++){
					if(max < words[i].count){
						max = words[i].count;
					}
				}
			}
		});
		if(words.length <= 0){
			words = [{ "word" : "No Words!", "count" : 1 }];
			max = 1;
		}

		var width = $("div.main").width();
		var height = $(window).width() / 3;
		if(height > 400){
			height = 400;
		}
		var fill = d3.scale.category20();
		d3.layout.cloud()
			.size([width, height])
			.words(
				words.map(function(d) {
					return {
						text: d.word, size: d.count / max * (height / 400 * 60)
					};
				})
			)
			.padding(5)
			.rotate(function() { return ~~(Math.random() * 30) * 3; })
			.font("Impact")
			.fontSize(function(d) { return d.size; })
			.on("end", draw)
			.start();

		function draw(words) {
			d3.select("#cloud")
				.append("svg")
				.attr("width", width)
				.attr("height", height)
				.append("g")
				.attr("transform", "translate(" + (width / 2) + "," + (height / 2) + ")")
				.selectAll("text")
				.data(words)
				.enter()
				.append("text")
				//.on("click", function(d) { location.href = contextPath + "/weblog?keyword=" + d.text })
				.style("cursor", "pointer")
				.style("font-size", function(d) { return d.size + "px"; })
				.style("font-family", "Impact")
				.style("fill", function(d, i) { return fill(i); })
				.attr("text-anchor", "middle")
				.attr("transform", function(d) {
					return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
				})
				.text(function(d) { return d.text; });
		}

		$("#loader").css("display", "none");
		$("#fade").css("display", "none");
	})
}) (jQuery);