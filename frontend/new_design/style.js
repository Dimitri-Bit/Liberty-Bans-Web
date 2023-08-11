$(document).ready(function() {
    $("#search-btn-mobile").hover(
      function() {
        $("#search-btn-mobile").addClass("green-text");
      },
      function() {
        $("#search-btn-mobile").removeClass("green-text");
      }
    );

    $("#navbar-toggler").hover(
      function() {
        $("#navbar-toggler").addClass("green-text");
      },
      function() {
        $("#navbar-toggler").removeClass("green-text");
      }
    );

    $("#green-hover").hover(
      function() {
        $("#green-hover").addClass("green-border");
        $(".form-control").addClass("search-input-hover");
        $(".form-control").addClass("search-input-hover-border-right");
        $(".input-group-text").addClass("search-input-hover");
        console.log("test")
      },
      function() {
        $("#green-hover").removeClass("green-border");
        $(".form-control").removeClass("search-input-hover");
        $(".form-control").removeClass("search-input-hover-border-right");
        $(".input-group-text").removeClass("search-input-hover");
      }
    );
});