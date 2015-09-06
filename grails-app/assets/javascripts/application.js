//This is a JavaScript file with its top level require directives
//= require jquery
//= require holder
//= require bootstrap
//= require_self
//

$(function () {
    $(".clickable-row").click(function () {
        var href = $(this).data("href");
        if (href !== undefined) {
            window.document.location = $(this).data("href");
        } else {
            window.open($(this).data("href-blank"), '_blank');
        }
    });
});