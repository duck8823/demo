/**
 * Created by maeda on 2015/12/13.
 */
(function($) {

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
				url : app.CONTEXT_ROOT + "/manage/photo/delete/" + id,
				type : "DELETE",
				cache : false,
			}).done(function(){
				console.log("success");
				$(".photo[data-id='" + id + "']").remove();
			}).fail(function(){
				console.log("fail")
			}).always(function(){
				$("#links").justifiedGallery({
					rowHeight: 75,
					lastRow: 'nojustify',
					margins: 2,
					refreshSensitivity: 50
				});
				$("#loader").css("display", "none");
				$("#fade").css("display", "none");
			})
		})
	})
}) (jQuery);