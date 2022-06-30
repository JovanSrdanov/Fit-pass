Vue.component("prijava", {
    data: function () {
        return {
            username: "",
            password: "",
            allDataEntered: true,
            userExist: true,
        };
    },
    template: `
   <div class="centriraj">
            <div class="LoginDeo">
                <h1>Prijavite se</h1>

             
                
                    <p>Korisničko ime:</p>
                    <input
                     v-model="username"
                        type="text"
                        name="username"
                        id="username"
                        placeholder="Korisničko ime"
                    />
                    <p>Lozinka:</p>
                    <input
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


                        
                        <br />
                            
                        <button v-on:click="Login" >Prijavite se</button>

                        <p v-if="!allDataEntered" >Niste uneli sve podatke</p>
                        <p v-if="!userExist" >Ne postoji korisnik sa tom sifrom</p>
                        
               
              
            </div>
        </div>`,

    methods: {
        Login: function () {
            this.allDataEntered = true;
            this.userExist = true;
            if (this.username === "" || this.password === "") {
                this.allDataEntered = false;
                return;
            }

            const params = new URLSearchParams();
            params.append("username", this.username);
            params.append("password", this.password);
            axios
                .post("rest/login/token", params, {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
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
                    this.userExist = false;
                });
        },
    },
});
