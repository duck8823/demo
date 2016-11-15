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

		$("form[name='place-add']").addPlace({
			selector : "button.add"
		});

		$("form[name='place-delete']").deletePlace({
			selector : "button.delete"
		})

		$("form[name='manage-photo']").each(function(){
			$(this).update();
		})

		$("#links .link").each(function(){
			$(this).modalOpen();
		})

	})

	$.fn.update = function(options) {
		var settings = $.extend({
			"selector" : "button.submit"
		}, options)

		var this$ = this;

		this.find(settings.selector).on("click", function(){
			var data = this$.serialize();
			$.ajax({
				url : app.CONTEXT_ROOT + "/manage/photo/place/addInfo",
				data : data,
				type : "POST",
				cache : false,
			}).done(function(){
				console.log("success");
				this$.parents(".modal").modal("hide");
			}).fail(function(){
				console.log("fail")
			}).always(function(data){

			})
		})
	}

	$.fn.modalOpen = function(options) {
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