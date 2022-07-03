Vue.component("napraviObjekat", {
    data: function () {
        return {
            username: "",
            password: "",
            name: "",
            surname: "",
            gender: "male",
            dateOfBirth: "1900-01-01",
            role: "manager",
            loggedInUser: {},
            userNameUnique: "OK",
            allDataEntered: true,
            userExist: false,
        };
    },
    template: `
      <div>
        <h1>Napravi objekat</h1>
         
        </div>      
  `,
    mounted() {
        let start = JSON.parse(localStorage.getItem("loggedInUser"));

        if (JSON.parse(localStorage.getItem("loggedInUser")) === null) {
            alert(
                "Nije vam dozvoljeno da vidite ovu stranicu jer niste ulogovani kao odgovarajuća uloga!"
            );
            window.location.href = "#/pocetna";
            return;
        }

        if (JSON.parse(localStorage.getItem("loggedInUser")).role !== "admin") {
            alert(
                "Nije vam dozvoljeno da vidite ovu stranicu jer ste ulogovani kao uloga koja nije odgovarajuća!"
            );
            window.location.href = "#/pocetna";
            return;
        }
    },
});
