Vue.component("registracija", {
    data: function () {
        return {
            username: "",
            password: "",
            name: "",
            surname: "",
            gender: "Muški",
            dateOfBirth: "1900-01-01",
            role: "CUSTOMER",
            loggedInUser: {},
            userNameUnique: "OK",
        };
    },
    template: ` 
  <div class="centriraj">
            <h1>Registrujte se</h1>
        
                <p>Korisničko ime:</p>
                <input required
                v-model="username"
                    type="text"
                    name="username"
                    id="username"
                    placeholder="Korisničko ime"
                />

                <p>Lozinka:</p>
                <input required
                  v-model="password"
                    type="password"
                    name="password"
                    id="password"
                    placeholder="Lozinka"
                />

                <br />
                <input 
                
                    class="checkbox"
                    type="checkbox"
                    id="showPassword"
                    onclick="myFunction() "
                />

                <label for="checkbox"> Prikaz lozinke </label>

                <br />
                <p>Ime:</p>

                <input 
                v-model="name"
                type="text" name="name" id="name" placeholder="Ime" required />

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
                <select name="gender" id="gender"  v-model="gender">
                    <option  selected="selected" value="male">Muški</option>
                    <option value="female">Ženski</option>
                </select>

                <p>Datum rođenja:</p>
                <input type="date" value="1900-01-01"  v-model="dateOfBirth"  name="dateOfBirth" id="dateOfBirth" 
                 required/>
                 <br />
                 <br />
     <button v-on:click="RegisterCustomer">Registruj se </button>

        </div>       
   `,

    mounted() {},
    methods: {
        RegisterCustomer: function () {
            axios
                .post(
                    "rest/customers/reg",
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
                        .catch(function (error) {
                            if (error.response.status === 401) {
                                console.log("unauthorised");
                            }

                            $("#poruka").show();
                        });
                })
                .catch(function (error) {
                    alert(
                        "Vec postoji ovaj korisnik sa ovim korisnickim imenom"
                    );
                });
        },
    },
});
