Vue.component("korisnici", {
    data: function () {
        return {
            Users: {},

            searchName: "",
            searchSurname: "",
            searchUsername: "",

            filterRole: "all",
            filterTypeOfCustomer: "all",

            sortName: "noSort",
            sortSurname: "noSort",
            sortUsername: "noSort",
            sortPoints: "noSort",

            nameString: "Ime ↕",
            surnameString: "Prezime ↕",
            usernameString: "Korisničko ime ↕",
            pointsString: "Broj bodova ↕",
        };
    },
    template: `
         <div class="WholeScreen">
            <h1>Korisnici</h1>
            <table class="tableForKorisnici">
                <td>
                    <div class="SearchSortFilterWrapper">
                        <p>Pretraga:</p>

                        <input
                            v-model="searchName"
                            type="text"
                            name="searchName"
                            id="searchName"
                            placeholder="Ime"
                        />

                        <br />
                        <br />
                        <input
                            v-model="searchSurname"
                            type="text"
                            name="searchSurname"
                            id="searchSurname"
                            placeholder="Prezime"
                        />
                        <br />
                        <br />
                        <input
                            v-model="searchUsername"
                            type="text"
                            name="searchUsername"
                            id="searchUsername"
                            placeholder="Korisničko ime"
                        />
                        <br />
                        <br />

                        <button v-on:click="searchUser">Pretraži</button>

                        <p>Filtriranje:</p>

                        <label for="Uloga">Izabrati ulogu:</label>
                        <select name="Uloga" id="Uloga" v-model="filterRole">
                            <option value="all">Svi</option>
                            <option value="customer">Kupac</option>
                            <option value="admin">Administrator</option>
                            <option value="manager">Menadžer</option>
                            <option value="trainer">Trener</option>
                        </select>
                        <br />
                        <br />
                        <label for="CustomerType">Izabrati tip kupca</label>
                        <select
                            name="CustomerType"
                            id="CustomerType"
                            v-model="filterTypeOfCustomer"
                        >
                            <option value="all">Svi</option>
                            <option value="Gold">Zlatni</option>
                            <option value="Silver">Srebrni</option>
                            <option value="Bronze">Bronzani</option>
                        </select>

                        <p>Sortiranje:</p>
                        <p>
                            <button v-on:click="ImeSortFunction">
                                {{nameString}}
                            </button>
                            <button v-on:click="PrezimeSortFunction">
                                {{surnameString}}
                            </button>
                        </p>

                        <p>
                            <button v-on:click="KorisnickoImeSortFunction">
                                {{usernameString}}
                            </button>
                            <button v-on:click="BodoviSortFunction">
                                {{pointsString}}
                            </button>
                        </p>

                        <button v-on:click="CreateManagerTrainer">
                            Registruj novog menadžera ili trenera
                        </button>
                    </div>
                </td>
                <td>
                    <div class="TabelaKorisnika">
                        <table>
                            <th>Korisničko ime</th>
                            <th>Šifra</th>
                            <th>Ime</th>
                            <th>Prezime</th>
                            <th>Pol</th>
                            <th>Datum rođenja</th>
                            <th>Uloga</th>
                            <th>Tip korisnika</th>
                            <th>Broj bodova</th>
                            <th>Obriši</th>

                            <tbody>
                                <tr v-for="u in searchFilterSortUsers">
                                    <td>{{u.username}}</td>
                                    <td>{{u.password}}</td>
                                    <td>{{u.name}}</td>
                                    <td>{{u.surname}}</td>
                                    <td>{{translateGender(u.gender)}}</td>
                                    <td>{{getDate(u.birthDate)}}</td>
                                    <td>{{getRole(u.role)}}</td>
                                    <td>{{getCustomerType(u)}}</td>
                                    <td>{{customPoints(u.points)}}</td>
                                    <td><button class="ObrisiDugme"  v-on:click="DeleteUser(u)">Obriši</button></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </td>
            </table>
        </div>
  `,

    mounted() {
        //
        yourConfig = {
            headers: {
                Authorization: localStorage.getItem("token"),
            },
        };

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

        ///
        axios.get("rest/customer/all", yourConfig).then((response) => {
            this.Users = response.data;
        });
    },
    computed: {
        searchFilterSortUsers() {
            if (!this.Users) return null;
            tempSFSU = this.Users;

            if (this.filterTypeOfCustomer != "all") {
                tempSFSU = tempSFSU.filter((temp) => {
                    if (temp.customerType === null) return;
                    return (
                        temp.customerType.type
                            .toLowerCase()
                            .indexOf(this.filterTypeOfCustomer.toLowerCase()) >
                        -1
                    );
                });
            }

            if (this.filterRole != "all") {
                tempSFSU = tempSFSU.filter((temp) => {
                    return (
                        temp.role
                            .toLowerCase()
                            .indexOf(this.filterRole.toLowerCase()) > -1
                    );
                });
            }

            if (this.sortName === "DSC") {
                tempSFSU = tempSFSU.sort((a, b) => {
                    return a.name.localeCompare(b.name);
                });
            }

            if (this.sortName === "ASC") {
                tempSFSU = tempSFSU.sort((a, b) => {
                    return b.name.localeCompare(a.name);
                });
            }

            if (this.sortSurname === "DSC") {
                tempSFSU = tempSFSU.sort((a, b) => {
                    return a.surname.localeCompare(b.surname);
                });
            }

            if (this.sortSurname === "ASC") {
                tempSFSU = tempSFSU.sort((a, b) => {
                    return b.surname.localeCompare(a.surname);
                });
            }

            if (this.sortUsername === "DSC") {
                tempSFSU = tempSFSU.sort((a, b) => {
                    return a.username.localeCompare(b.username);
                });
            }

            if (this.sortUsername === "ASC") {
                tempSFSU = tempSFSU.sort((a, b) => {
                    return b.username.localeCompare(a.username);
                });
            }

            if (this.sortPoints === "DSC") {
                tempSFSU = tempSFSU.sort((a, b) => {
                    return a.points - b.points;
                });
            }

            if (this.sortPoints === "ASC") {
                tempSFSU = tempSFSU.sort((a, b) => {
                    return b.points - a.points;
                });
            }

            return tempSFSU;
        },
    },
    methods: {
        DeleteUser: function (user) {
            if (user.role == "manager") {
                console.log("Provera za managera");
                window.location.href = "#/zamenaMenagera/" + user.id;
            }
        },

        CreateManagerTrainer: function () {
            window.location.href = "#/registruj";
        },
        ImeSortFunction() {
            this.sortSurname = "noSort";
            this.sortUsername = "noSort";
            this.sortPoints = "noSort";

            this.nameString = "Ime ↕";
            this.surnameString = "Prezime ↕";
            this.usernameString = "Korisničko ime ↕";
            this.pointsString = "Broj bodova ↕";

            if (this.sortName == "ASC") {
                this.sortName = "DSC";
                this.nameString = "Ime  ↓";
            } else {
                this.sortName = "ASC";
                this.nameString = "Ime ↑";
            }
        },
        PrezimeSortFunction() {
            this.sortName = "noSort";
            this.sortUsername = "noSort";
            this.sortPoints = "noSort";

            this.nameString = "Ime ↕";
            this.usernameString = "Korisničko ime ↕";
            this.pointsString = "Broj bodova ↕";

            if (this.sortSurname == "ASC") {
                this.sortSurname = "DSC";
                this.surnameString = "Prezime  ↓";
            } else {
                this.sortSurname = "ASC";
                this.surnameString = "Prezime ↑";
            }
        },
        KorisnickoImeSortFunction() {
            this.sortName = "noSort";
            this.sortSurname = "noSort";
            this.sortPoints = "noSort";

            this.nameString = "Ime ↕";
            this.surnameString = "Prezime ↕";
            this.pointsString = "Broj bodova ↕";

            if (this.sortUsername == "ASC") {
                this.sortUsername = "DSC";
                this.usernameString = "Korisničko ime  ↓";
            } else {
                this.sortUsername = "ASC";
                this.usernameString = "Korisničko ime ↑";
            }
        },
        BodoviSortFunction() {
            this.sortName = "noSort";
            this.sortSurname = "noSort";
            this.sortUsername = "noSort";

            this.nameString = "Ime ↕";
            this.surnameString = "Prezime ↕";
            this.usernameString = "Korisničko ime ↕";

            if (this.sortPoints == "ASC") {
                this.sortPoints = "DSC";
                this.pointsString = "Broj bodova  ↓";
            } else {
                this.sortPoints = "ASC";
                this.pointsString = "Broj bodova ↑";
            }
        },

        searchUser: function () {
            const params = new URLSearchParams();
            params.append("name", this.searchName);
            params.append("surname", this.searchSurname);
            params.append("username", this.searchUsername);

            axios
                .post("rest/customer/search", params, {
                    headers: {
                        Authorization: localStorage.getItem("token"),
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                })
                .then((response) => {
                    this.Users = response.data;
                })
                .catch((error) => {
                    alert("Greska u search metodi");
                });
        },
        translateGender: function (gender) {
            if (gender == "male") return "Muški";
            else return "Ženski";
        },
        customPoints: function (points) {
            if (points == -1) return "X";
            else return points;
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
