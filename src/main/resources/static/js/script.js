/*
	Author       : html.design
	Template Name: Wave Login Form Responsive Widget Template
	Version      : 1.0
*/


(function ($) {
	"use strict";

	// function formLogin() {
	// 	$('#formLogin').on('submit', function (e) {
	// 		$.ajax({
	// 			type: "POST",
	// 			url: 'form-process.php',
	// 			data: $(this).serialize(),
	// 			success: function () {
	// 				$('#msgSubmit').fadeIn(100).show();
	// 			}
	// 		});
	// 		e.preventDefault();
	// 	});
	// }


	$('.form-group input').focus(function () {
		$(this).parent().addClass('addcolor');
	}).blur(function () {
		$(this).parent().removeClass('addcolor');
	});

	$(document).ready(function () {
		if ($.fn.rippler) {
			$(".rippler").rippler({
				effectClass: 'rippler-effect',
				effectSize: 0,
				addElement: 'div',
				duration: 400
			});
		}

		// Ensure mobile navigation hamburger toggle works reliably across all pages
		$(document).on('click', '.navbar-toggler', function (e) {
			var target = $(this).attr('data-target') || '#navbarSupportedContent';
			$(target).collapse('toggle');
		});
	});

})(jQuery);