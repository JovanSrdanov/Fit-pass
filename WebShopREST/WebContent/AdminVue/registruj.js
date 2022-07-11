Vue.component("registruj", {
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
      <div >
            <h1>Registruj novog menadžera ili trenera</h1>

            <p class="white">Korisničko ime:</p>
            <input
                
                v-model="username"
                type="text"
                name="username"
                id="username"
                placeholder="Korisničko ime"
            />

            <p class="white">Lozinka:</p>
            <input
                
                v-model="password"
                type="password"
                name="password"
                id="password"
                placeholder="Lozinka"
            />

        
            <p
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

                <label class="white" for="checkbox"> Prikaz lozinke </label>
            </p>

            <p class="white">Ime:</p>

            <input
                v-model="name"
                type="text"
                name="name"
                id="name"
                placeholder="Ime"
                
            />

            <p class="white">Prezime:</p>
            <input
                
                v-model="surname"
                type="text"
                name="surname"
                id="surname"
                placeholder="Prezime"
            />

            <p class="white">Pol:</p>
            <select name="gender" id="gender" v-model="gender">
                <option selected="selected" value="male">Muški</option>
                <option value="female">Ženski</option>
            </select>

            <p class="white">Datum rođenja:</p>
            <input
                type="date"
                value="1900-01-01"
                v-model="dateOfBirth"
                name="dateOfBirth"
                id="dateOfBirth"
                
            />
               <p class="white">Uloga:</p>
            <select name="role" id="role" v-model="role">
                <option selected="selected" value="manager">Menadžer</option>
                <option value="trainer">Trener</option>
            </select>
            <br />
            <br />

            <button v-on:click="RegisterCustomer">Registruj se</button>

            <p class="white" v-if="!allDataEntered">Niste uneli sve podatke</p>
            <p class="white" v-if="userExist">
                Već postoji korisnik sa ovim korisničkim imenom
            </p>
        </div>      
  `,
    mounted() {
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
        RegisterCustomer: function () {
            this.allDataEntered = true;
            this.userExist = false;
            var now = new Date();
            var checkDate = new Date(this.dateOfBirth);
            if (checkDate >= now) {
                alert("Datum rođenja mora biti u prošlosti");
                return;
            }
            if (
                this.username === "" ||
                this.password === "" ||
                this.name === "" ||
                this.surname === ""
            ) {
                this.allDataEntered = false;
                return;
            }

            if (this.role === "manager") {
                axios
                    .post(
                        "rest/manager/reg",
                        {
                            username: this.username,
                            password: this.password,
                            name: this.name,
                            surname: this.surname,
                            gender: this.gender,
                            birthDate: this.dateOfBirth,
                            role: this.role,
                        },
                        {
                            headers: {
                                "Content-Type": "application/json",
                                Authorization: localStorage.getItem("token"),
                            },
                        }
                    )
                    .then((response) => {
                        window.location.href = "#/pocetna";
                    })
                    .catch((error) => {
                        this.userExist = true;
                    });
            } else {
                axios
                    .post(
                        "rest/trainer/reg",
                        {
                            username: this.username,
                            password: this.password,
                            name: this.name,
                            surname: this.surname,
                            gender: this.gender,
                            birthDate: this.dateOfBirth,
                            role: this.role,
                        },
                        {
                            headers: {
                                "Content-Type": "application/json",
                                Authorization: localStorage.getItem("token"),
                            },
                        }
                    )
                    .then((response) => {
                        window.location.href = "#/pocetna";
                    })
                    .catch((error) => {
                        this.userExist = true;
                    });
            }
        },
    },
});
