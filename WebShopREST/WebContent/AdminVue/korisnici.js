Vue.component("korisnici", {
    data: function () {
        return {
            Users: {},
        };
    },
    template: `
        <div>
            <h1>Korisnici</h1>

            <div class="pretragaFiltriranjeSortiranje">
                <div class="optionsWrapper">
                    <p>Pretraga:</p>

                    <input
                        type="text"
                        name="name"
                        id="name"
                        placeholder="Ime"
                    />

                    <br />
                    <br />
                    <input
                        type="text"
                        name="facilityType"
                        id="facilityType"
                        placeholder="Prezime"
                    />
                    <br />
                    <br />
                    <input
                        type="text"
                        name="locationString"
                        id="locationString"
                        placeholder="Korisničko ime"
                    />
                    <br />
                    <br />

                    <button>Pretraži</button>
                </div>

                <div class="optionsWrapper">
                    <p>Filtriranje:</p>

                    <label for="Uloga">Izabrati ulogu:</label>
                    <select name="Uloga" id="Uloga">
                        <option value="all">Svi</option>
                        <option value="customer">Kupac</option>
                        <option value="admin">Administrator</option>
                        <option value="manager">Menadžer</option>
                        <option value="trainer">Trener</option>
                    </select>
                    <br />
                    <br />
                    <label for="CustomerType">Izabrati tip kupca</label>
                    <select name="CustomerType" id="CustomerType">
                        <option value="all">Svi</option>
                        <option value="gold">Zlatni</option>
                        <option value="silver">Srebrni</option>
                        <option value="bronze">Bronzani</option>
                    </select>
                </div>

                <div class="optionsWrapper">
                    <p>Sortiranje</p>
                    <thead>
                        <th colspan="2">
                            <button>Ime</button>
                            <button>Prezime</button>
                            <br />
                            <br />
                            <button>Korisnicko ime</button>
                            <button>Broj bodova</button>
                        </th>
                    </thead>
                    <br />
                    <p></p>
                </div>
                <button>Kreiraj novog menadžera ili trenera</button>
            </div>

            <div class="TabelaKorisnika">
                <table>
                    <td>Korisničko ime</td>
                    <td>Šifra</td>
                    <td>Ime</td>
                    <td>Prezime</td>
                    <td>Pol</td>
                    <td>Datum rođenja</td>
                    <td>Uloga</td>
                    <td>Tip korisnika</td>
                    <td>Broj bodova</td>
                    <td>Obriši</td>

                    <tbody>
                        <tr v-for="u in Users">
                            <td>{{u.username}}</td>
                            <td>{{u.password}}</td>
                            <td>{{u.name}}</td>
                            <td>{{u.surname}}</td>
                            <td>{{translateGender(u.gender)}}</td>
                            <td>{{getDate(u.birthDate)}}</td>
                            <td>{{getRole(u.role)}}</td>
                            <td>{{getCustomerType(u)}}</td>
                            <td>{{u.points}}</td>
                            <td><button>Obriši</button></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
  `,

    mounted() {
        yourConfig = {
            headers: {
                Authorization: localStorage.getItem("token"),
            },
        };

        ///
        axios.get("rest/customers/all", yourConfig).then((response) => {
            this.Users = response.data;
        });

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
    methods: {
        translateGender: function (gender) {
            if (gender == "male") return "Muški";
            else return "Ženski";
        },
        getCustomerType: function (user) {
            if (user.customerType === null) return this.getRole(user.role);
            else return this.translateType(user.customerType.type);
        },
        translateType: function (type) {
            switch (type) {
                case "Bronze":
                    return "Bronzani";
                case "Silver":
                    return "Srebrni";
                case "Gold":
                    return "Zlatni";

                default:
                    return type;
            }
        },

        getDate: function (milliseconds) {
            const d = new Date();
            d.setTime(milliseconds);
            return d.toLocaleDateString("sr-RS");
        },
        getRole: function (role) {
            switch (role) {
                case "customer":
                    return "Kupac";
                case "admin":
                    return "Administrator";
                case "manager":
                    return "Menadžer";
                case "trainer":
                    return "Trener";
                default:
                    return role;
            }
        },
    },
});
