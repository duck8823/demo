/**
 * Created by maeda on 2015/12/13.
 */
(function($) {
	$(window).on("load", function () {
		$("#links").justifiedGallery({
			rowHeight: 75,
			lastRow: 'nojustify',
			margins: 2,
			refreshSensitivity: 50
		});

	})
}) (jQuery);