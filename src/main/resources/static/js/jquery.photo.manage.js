/**
 * Created by maeda on 2015/12/13.
 */
(function($) {

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});

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

		$("form[name='place-add']").addPlace({
			selector : "button.add"
		});

		$("form[name='place-delete']").deletePlace({
			selector : "button.delete"
		})

		$("#links .link").updateSelectPlace();

	})

	$.fn.updateSelectPlace = function(options) {
		var settings = $.extend({
			"selector" : "select.selectPlace"
		}, options)

		var this$ = this;
		this$.on("click", function() {
			$.ajax({
				url : app.CONTEXT_ROOT + "/manage/photo/place/items/?photoId=" + this$.data("photo-id"),
				type : "GET",
				cache : false,
			}).done(function(data){
				console.log("success");
				$(settings.selector).html(data);
			}).fail(function(){
				console.log("fail")
			}).always(function(data){

			})
		})
	}

	$.fn.addPlace = function(options) {
		var settings = $.extend({
			"selector" : "button.submit"
		}, options)

		var this$ = this;

		this.find(settings.selector).on("click", function(){
			var data = this$.serialize();
			$.ajax({
				url : app.CONTEXT_ROOT + "/manage/photo/place/add",
				data : data,
				type : "POST",
				cache : false,
			}).done(function(data){
				console.log("success");
				$("table.places").html(data).find("form[name='place-delete']").deletePlace({selector:"button.delete"});
			}).fail(function(){
				console.log("fail")
			}).always(function(data){

			})
		})
	}

	$.fn.deletePlace = function(options) {
		var settings = $.extend({
			"selector" : "button.submit"
		}, options)

		this.find(settings.selector).on("click", function() {
			var id = $(this).data('id')
			console.log(id);
			$.ajax({
				url : app.CONTEXT_ROOT + "/manage/photo/place/delete/" + id,
				type : "DELETE",
				cache : false,
			}).done(function(data){
				console.log("success");
				$("table.places").html(data).find("form[name='place-delete']").deletePlace({selector:"button.delete"});
			}).fail(function(){
				console.log("fail")
			}).always(function(data){

			})
		})
	}
}) (jQuery);