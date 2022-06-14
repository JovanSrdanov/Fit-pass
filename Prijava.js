function myFunction() {
    var x = document.getElementById("lozinka");
    if (x.type === "password") {
        x.type = "text";
    } else {
        x.type = "password";
    }
}
