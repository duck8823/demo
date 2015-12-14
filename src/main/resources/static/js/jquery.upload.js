/**
 * Created by maeda on 2015/12/13.
 */
(function($) {
	$(function(){
		$("form").submit(function(){
			$("#loader").css("display", "block");
			$("#fade").css("display", "block");
		})
	})
}) (jQuery);