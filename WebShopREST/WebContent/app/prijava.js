Vue.component("prijava", {
    data: function () {
        return {
            username: "",
            password: "",
            allFilled: "OK",
        };
    },
    template: `
   <div class="centriraj">
            <div class="LoginDeo">
                <h1>Prijavite se</h1>

             
                <form >
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
                    <input
                    
                        class="checkbox"
                        type="checkbox"
                        id="showPassword"
                        onclick="myFunction() "
                    />
                
                    <label for="checkbox"> Prikaz lozinke </label>
                        <br />
                        <br />
                       
                                   
                                
                        <button v-on:click="Login">PREKO login FUNKCIJE</button>
                </form>
              
            </div>
        </div>`,

    methods: {
        Login: function () {
            const params = new URLSearchParams();
            params.append("username", this.username);
            params.append("password", this.password);
            axios
                .post("rest/login/token", params, {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                })
                .then(() => {
                    alert("uspeo");
                })
                .catch(function () {
                    alert("greska");
                });
            preventDefault();
        },
    },
});
