/**
 * Created by maeda on 2016/01/11.
 */
(function($) {

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});

	$(function(){

		$("form[name='filter-add']").addFilter();

		$("form[name='filter-delete']").deleteFilter({
			selector : "button.delete"
		})

	})

	$.fn.addFilter = function(options) {
		var settings = $.extend({
			"selector" : "button.submit"
		}, options)

		var this$ = this;

		this.find(settings.selector).on("click", function(){
			var data = this$.serialize();
			$.ajax({
				url : app.CONTEXT_ROOT + "/manage/twitter/filter/add",
				data : data,
				type : "POST",
				cache : false,
			}).done(function(data){
				console.log("success");
				$("table.filters").html(data).find("form[name='filter-delete']").deleteFilter({selector:"button.delete"});
			}).fail(function(){
				console.log("fail")
			}).always(function(data){

			})
		})
	}

	$.fn.deleteFilter = function(options) {
		var settings = $.extend({
			"selector" : "button.submit"
		}, options)

		this.find(settings.selector).on("click", function() {
			var id = $(this).data('id')
			console.log(id);
			$.ajax({
				url : app.CONTEXT_ROOT + "/manage/twitter/filter/delete/" + id,
				type : "DELETE",
				cache : false,
			}).done(function(data){
				console.log("success");
				$("table.filters").html(data);
			}).fail(function(){
				console.log("fail")
			}).always(function(data){

			})
		})
	}
}) (jQuery);