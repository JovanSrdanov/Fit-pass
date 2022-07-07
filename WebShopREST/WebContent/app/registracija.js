Vue.component("registracija", {
    data: function () {
        return {
            username: "",
            password: "",
            name: "",
            surname: "",
            gender: "male",
            dateOfBirth: "1900-01-01",
            role: "CUSTOMER",
            loggedInUser: {},
            userNameUnique: "OK",
            allDataEntered: true,
            userExist: false,
        };
    },
    template: ` 
  <div class="centriraj">
            <h1>Registrujte se</h1>

            <p>Korisničko ime:</p>
            <input
                required
                v-model="username"
                type="text"
                name="username"
                id="username"
                placeholder="Korisničko ime"
            />

            <p>Lozinka:</p>
            <input
                required
                v-model="password"
                type="password"
                name="password"
                id="password"
                placeholder="Lozinka"
            />

            <br />
            <div
                style="
                    display: flex;
                    align-items: center;
                    justify-content: center;
                "
            >
                <input
                    class="checkbox"
                    type="checkbox"
                    id="showPassword"
                    onclick="myFunction() "
                />

                <label for="checkbox"> Prikaz lozinke </label>
            </div>

            <p>Ime:</p>

            <input
                v-model="name"
                type="text"
                name="name"
                id="name"
                placeholder="Ime"
                required
            />

            <p>Prezime:</p>
            <input
                required
                v-model="surname"
                type="text"
                name="surname"
                id="surname"
                placeholder="Prezime"
            />

            <p>Pol:</p>
            <select name="gender" id="gender" v-model="gender">
                <option selected="selected" value="male">Muški</option>
                <option value="female">Ženski</option>
            </select>

            <p>Datum rođenja:</p>
            <input
                type="date"
                value="1900-01-01"
                v-model="dateOfBirth"
                name="dateOfBirth"
                id="dateOfBirth"
                required
            />
            <br />
            <br />
            <button v-on:click="RegisterCustomer">Registruj se</button>

            <p v-if="!allDataEntered">Niste uneli sve podatke</p>
            <p v-if="userExist">
                Već postoji korisnik sa ovim korisničkim imenom
            </p>
        </div>      
   `,

    mounted() {
        this.$root.VarToken();
        if (JSON.parse(localStorage.getItem("loggedInUser")) !== null) {
            alert(
                "Ne može se izvršiti registracija dok ste prijavljeni. Odjavite se i pokušajte ponovo."
            );
            window.location.href = "#/pocetna";
            return;
        }
    },
    methods: {
        RegisterCustomer: function () {
            this.allDataEntered = true;
            this.userExist = false;
            if (
                this.username === "" ||
                this.password === "" ||
                this.name === "" ||
                this.surname === ""
            ) {
                this.allDataEntered = false;
                return;
            }

            axios
                .post(
                    "rest/customer/reg",
                    {
                        id: -1,
                        username: this.username,
                        password: this.password,
                        name: this.name,
                        surname: this.surname,
                        gender: this.gender,
                        birthDate: this.dateOfBirth,
                        role: "customer",
                        membershipId: -1,
                        points: 0,
                        deleted: false,
                    },
                    {
                        headers: {
                            "Content-Type": "application/json",
                        },
                    }
                )
                .then((response) => {
                    const params = new URLSearchParams();
                    params.append("username", this.username);
                    params.append("password", this.password);
                    axios
                        .post("rest/login/token", params, {
                            headers: {
                                "Content-Type":
                                    "application/x-www-form-urlencoded",
                            },
                        })
                        .then((response) => {
                            localStorage.setItem(
                                "token",
                                response.headers.authorization
                            );
                            this.$root.VarToken();
                            window.location.href = "#/pocetna";
                        })
                        .catch((error) => {
                            if (error.response.status === 401) {
                                console.log("unauthorised");
                            }
                        });
                })
                .catch((error) => {
                    this.userExist = true;
                });
        },
    },
});
