Vue.component("registracija", {
    data: function () {
        return {
            userName: "",
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
            <form action="#" method="post">
                <p>Korisničko ime:</p>
                <input required
                v-model="userName"
                    type="text"
                    name="userName"
                    id="userName"
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
                    <option  selected="selected" value="Muški">Muški</option>
                    <option value="Ženski">Ženski</option>
                </select>

                <p>Datum rođenja:</p>
                <input type="date" value="1900-01-01"  v-model="dateOfBirth"  name="dateOfBirth" id="dateOfBirth" 
                 required/>
                 <br />
                 <br />
                <input type="submit" value="Registruj se" v-on:click="RegisterCustomer" >
            </form>
        </div>       
   `,

    mounted() {
        axios
            .get("rest/testlogin")
            .then((response) => {})
            .catch(function (error) {});
    },
    methods: {
        RegisterCustomer: function () {
            axios
                .post("rest/CustomerReg/", {
                    userName: this.userName,
                    password: this.password,
                    name: this.name,
                    surname: this.surname,
                    date: this.dateOfBirth,
                    gender: this.gender,
                })
                .then((response) => {
                    alert("Successful customer registration!");
                    if (this.role === "CUSTOMER") {
                        axios
                            .post("rest/login", {
                                userName: this.userName,
                                password: this.password,
                            })
                            .then((response) => {
                                if (
                                    response.data ==
                                    "YOUR ACCOUNT DOES NOT EXIST IN THE SYSTEM, PLEASE REGISTER!"
                                ) {
                                    alert(
                                        "Err: YOUR ACCOUNT DOES NOT EXIST IN THE SYSTEM, PLEASE REGISTER"
                                    );
                                } else {
                                    alert("Successful user login!");
                                    window.location.href = "/";
                                }
                            })
                            .catch(() => {
                                alert(
                                    "Login for users is temporary unavailable"
                                );
                                window.location.href = "/";
                            });
                    }
                })
                .catch(() => {
                    alert(
                        "Registration for customers is temporary unavailable!"
                    );
                    window.location.href = "/";
                });
        },
    },
});
