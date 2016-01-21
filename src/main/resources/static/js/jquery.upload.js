/**
 * Created by maeda on 2015/12/13.
 */
(function($) {
	$(function(){
		$("form").submit(function(){
			$("#loader").css("display", "block");
			$("#fade").css("display", "block");
		})

		$(".btn-facebook-login .logout").click(function () {
			$.ajax({
				url : app.CONTEXT_ROOT + "/admin/facebook/logout",
				type : "GET",
				cache : false,
			}).done(function(data){
				console.log("success");
				$(".btn-facebook-login").html(data);
			}).fail(function(){
				console.log("fail")
			}).always(function(){
			})
		})
	})
}) (jQuery);