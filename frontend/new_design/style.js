$(document).ready(function() {
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