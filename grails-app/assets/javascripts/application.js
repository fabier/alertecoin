//This is a JavaScript file with its top level require directives
//= require jquery
//= require holder
//= require bootstrap
//= require_self
//

$(function () {
    $(".clickable-row").click(function () {
        window.document.location = $(this).data("href");
    });
});