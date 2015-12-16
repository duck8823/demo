/**
 * Created by maeda on 2015/12/13.
 */
(function($) {

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});

	var rotate = {};

	$(function(){
		$(".photo").on(
			{
				"mouseenter": function(){
					$(this).find("span.glyphicon").css("display","block");
				},
				"mouseleave": function(){
					$(this).find("span.glyphicon").css("display","none");
			}
		});

		$(".link").on("click", function(){
			$("#loader").css("display", "block");
			$("#fade").css("display", "block");
			var id = $(this).parent(".photo").data("id");
			$.ajax({
				url : app.CONTEXT_ROOT + "/rotate/" + id,
				type : "POST",
				cache : false,
			}).done(function(){
				console.log("success");
				var item$ = $(".photo[data-id='" + id + "']").find("img");
				if(rotate[id] == null){
					rotate[id] = 0;
				}
				rotate[id] += 90;
				item$.css("transform", "rotate(" + rotate[id] + "deg)");
			}).fail(function(){
				console.log("fail")
			}).always(function(){
				$("#links").justifiedGallery({
					rowHeight: 75,
					lastRow: 'nojustify',
					margins: 2
				});
				$("#loader").css("display", "none");
				$("#fade").css("display", "none");
			})
		})
	})
}) (jQuery);