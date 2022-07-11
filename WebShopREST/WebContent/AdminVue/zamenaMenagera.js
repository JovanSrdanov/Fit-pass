Vue.component("zamenaMenagera", {
    data: function () {
        return {
            availableManagers: {},
            selectedManager: { id: "" },

            usernameManager: "",
            passwordManager: "",
            nameManager: "",
            surnameManager: "",
            genderManager: "male",
            dateOfBirthManager: "1900-01-01",

            managerExists: "",
            managerDataEntered: "",

            errorSelect: "",
        };
    },
    template: `    
         <div>
            <h1>Postavi novog menadžera za sportski objekat</h1>
            <div class="ChooseManager">
                        <table>
                           
                            <th>Ime</th>
                            <th>Prezime</th>
                            <th>Korisničko ime</th>
                            <tbody>
                                <tr
                                    v-for="AM in availableManagers"
                                    v-on:click="selectM(AM)"
                                    v-bind:class="{selectedManagerClass : selectedManager.id===AM.id}"
                                >
                                  
                                    <td>{{AM.name}}</td>
                                    <td>{{AM.surname}}</td>
                                    <td>{{AM.username}}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <p>
                     <button v-on:click="ZameniMen">
                            Postavi novog
                        </button>
                    </p>

                     <p class="white">
                     {{errorSelect}}
                    </p>
                    <div class="FastRegsitration">
                        <p class="white">Brza registracija novog menadžera</p>
                       
                        <input
                            required
                            v-model="usernameManager"
                            type="text"
                            name="usernameManager"
                            id="usernameManager"
                            placeholder="Korisničko ime"
                        />

                       <br />
                        <br />
                        <input
                            
                            v-model="passwordManager"
                            type="password"
                            name="passwordManager"
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
                                onclick="myFunction()"
                            />

                            <label class="white" for="checkbox"> Prikaz lozinke </label>
                        </p>


                        <input
                            v-model="nameManager"
                            type="text"
                            name="nameManager"
                            id="nameManager"
                            placeholder="Ime"
                            required
                        />
                        <br />
                        <br />
                     
                        <input
                            required
                            v-model="surnameManager"
                            type="text"
                            name="surnameManager"
                            id="surnameManager"
                            placeholder="Prezime"
                        />

                        <p class="white">Pol:
                        <select
                            name="gender"
                            id="gender"
                            v-model="genderManager"
                        >
                            <option selected="selected" value="male">
                                Muški
                            </option>
                            <option value="female">Ženski</option>
                        </select>
                        </p>
                        <p class="white">Datum rođenja:
                        <input
                            type="date"
                            value="1900-01-01"
                            v-model="dateOfBirthManager"
                            name="dateOfBirthManager"
                            id="dateOfBirthManager"
                        />
                        </p>
            
                        <button v-on:click="RegisterManager">
                            Registruj se
                        </button>

                        <p class="white" >{{managerDataEntered}}{{managerExists}}                            
                        </p>
                        
                    </div>
           
        </div>
    
    `,
    mounted() {
        this.oldManId = this.$route.params.id;

        console.log("old man id " + this.oldManId);
        if (JSON.parse(localStorage.getItem("loggedInUser")) === null) {
            alert("Nemate pristup ovom sadržaju");
            window.location.href = "#/pocetna";
            return;
        }
        if (JSON.parse(localStorage.getItem("loggedInUser")).role != "admin") {
            alert("Nemate pristup ovom sadržaju");
            window.location.href = "#/pocetna";
            return;
        }
        yourConfig = {
            headers: {
                Authorization: localStorage.getItem("token"),
            },
        };

        axios.get("rest/manager/available", yourConfig).then((response) => {
            this.availableManagers = response.data;
        });
    },

    methods: {
        selectM: function (managaer) {
            this.selectedManager = managaer;
        },
        RegisterManager: function () {
            this.managerDataEntered = "";
            this.managerExists = "";

            if (
                this.usernameManager === "" ||
                this.passwordManager === "" ||
                this.nameManager === "" ||
                this.surnameManager === ""
            ) {
                this.managerDataEntered = "Niste uneli sve podatke";
                return;
            }

            axios
                .post(
                    "rest/manager/reg",
                    {
                        username: this.usernameManager,
                        password: this.passwordManager,
                        name: this.nameManager,
                        surname: this.surnameManager,
                        gender: this.genderManager,
                        birthDate: this.dateOfBirthManager,
                        role: "manager",
                    },
                    {
                        headers: {
                            "Content-Type": "application/json",
                            Authorization: localStorage.getItem("token"),
                        },
                    }
                )
                .then((response) => {
                    yourConfig = {
                        headers: {
                            Authorization: localStorage.getItem("token"),
                        },
                    };

                    axios
                        .get("rest/manager/available", yourConfig)
                        .then((response) => {
                            this.availableManagers = response.data;
                            this.username == "";
                            this.password == "";
                            this.nameManager == "";
                            this.surname == "";
                        });
                })
                .catch((error) => {
                    this.managerExists =
                        "Već postoji korisnik sa ovim korisničkim imenom";
                });
        },
        ZameniMen: function () {
            this.errorSelect = "";
            if (this.selectedManager.id === "") {
                this.errorSelect =
                    "Izaberite postojećeg ili kreirajte novg menadžera";
            }

            yourConfig = {
                headers: {
                    Authorization: localStorage.getItem("token"),
                },
            };
        },
    },
});
