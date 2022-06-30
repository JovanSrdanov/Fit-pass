Vue.component("pregledProfila", {
    data: function () {
        return {
            username: "",
            password: "",
            name: "",
            surname: "",
            gender: null,
            birthDate: null,
            role: "CUSTOMER",
            loggedInUser: {},
            userNameUnique: "OK",
            allDataEntered: true,
            userExist: false,
            changeCheck: false,
        };
    },
    template: `
  <div>
                <h1>Pregled profila - {{username}}</h1>

                  <p>Korisničko ime:</p>
                <input disabled
                v-model="username"
                    type="text"
                    name="username"
                    id="username"
                    placeholder="Korisničko ime"
                />
 <p>Lozinka:</p>
                <input :disabled=!changeCheck
                  v-model="password"
                    type="password"
                    name="password"
                    id="password"
                    placeholder="Lozinka"
                />

                <br />
                  <div style=" display: flex;
                    align-items: center;
                    justify-content: center;">
                <input 
               
                    class="checkbox"
                    type="checkbox"
                    id="showPassword"
                    onclick="myFunction() "
                />

                <label for="checkbox"> Prikaz lozinke </label>
</div>
             
                <p>Ime:</p>

                <input :disabled=!changeCheck
                v-model="name"
                type="text" name="name" id="name" placeholder="Ime"  />

                <p>Prezime:</p>
                <input :disabled=!changeCheck
                
                v-model="surname"
                    type="text"
                    name="surname"
                    id="surname"
                    placeholder="Prezime"
                />

                <p>Pol:</p>
                <select name="gender" id="gender"  v-model="gender" :disabled=!changeCheck>
                    <option   value="male">Muški</option>
                    <option value="female">Ženski</option>
                </select>

                <p>Datum rođenja:</p>
                <input :disabled=!changeCheck type="date" v-model="birthDate"  name="birthDate" id="birthDate" 
                 required/>



                 <br />
                 <br />
     <button v-on:click="EnableUpdate" :hidden=changeCheck>Izmeni </button>
      <button v-on:click="ConfrimUpdate" :hidden=!changeCheck>Potvrdi izmenu </button>
    <button v-on:click="CancelUpdate" :hidden=!changeCheck>Odustani od izmene </button>

                        <p v-if="!allDataEntered" >Niste uneli sve podatke</p>
                        <p v-if="userExist" >Već postoji korisnik sa ovim korisničkim imenom</p>
              

             
          </div>    
                `,

    mounted() {
        this.username = this.$route.params.username;

        if (JSON.parse(localStorage.getItem("loggedInUser")) === null) {
            alert(
                "Nije vam dozvoljeno da vidite ovu stranicu jer niste ulogovani kao odgovarajući korisnik!"
            );
            window.location.href = "#/pocetna";
            return;
        }

        if (
            JSON.parse(localStorage.getItem("loggedInUser")).username !==
            this.username
        ) {
            alert(
                "Nije vam dozvoljeno da vidite ovu stranicu jer niste ulogovani kao odgovarajući korisnik!"
            );
            window.location.href = "#/pocetna";
            return;
        }

        let user = JSON.parse(localStorage.getItem("loggedInUser"));

        this.password = user.password;
        this.name = user.name;
        this.surname = user.surname;
        this.gender = user.gender;
        let d = new Date(user.birthDate);
        this.birthDate = d.toISOString().split("T")[0];
    },
    methods: {
        EnableUpdate: function () {
            this.changeCheck = !this.changeCheck;
        },
        CancelUpdate: function () {
            this.changeCheck = false;
            this.username = this.$route.params.username;

            if (JSON.parse(localStorage.getItem("loggedInUser")) === null) {
                alert(
                    "Nije vam dozvoljeno da vidite ovu stranicu jer niste ulogovani kao odgovarajući korisnik!"
                );
                window.location.href = "#/pocetna";
                return;
            }

            if (
                JSON.parse(localStorage.getItem("loggedInUser")).username !==
                this.username
            ) {
                alert(
                    "Nije vam dozvoljeno da vidite ovu stranicu jer niste ulogovani kao odgovarajući korisnik!"
                );
                window.location.href = "#/pocetna";
                return;
            }

            let user = JSON.parse(localStorage.getItem("loggedInUser"));

            this.password = user.password;
            this.name = user.name;
            this.surname = user.surname;
            this.gender = user.gender;
            let d = new Date(user.birthDate);
            this.birthDate = d.toISOString().split("T")[0];
        },

        ConfrimUpdate: function () {
            yourConfig = {
                headers: {
                    Authorization: localStorage.getItem("token"),
                    ContentType: "application/json",
                },
            };

            axios
                .put(
                    "rest/customers/update",
                    {
                        username: this.username,
                        password: this.password,
                        name: this.name,
                        surname: this.surname,
                        gender: this.gender,
                        birthDate: this.birthDate,
                    },
                    yourConfig
                )
                .then((response) => {
                    localStorage.setItem(
                        "loggedInUser",
                        JSON.stringify(response.data)
                    );
                    this.CancelUpdate();
                })
                .catch((error) => {
                    alert(" greska pregled profila");
                });
        },
    },
});
