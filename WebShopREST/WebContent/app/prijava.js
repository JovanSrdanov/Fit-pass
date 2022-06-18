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

             

                    <p>Korisničko ime:</p>
                    <input
                        type="text"
                        name="username"
                        id="username"
                        placeholder="Korisničko ime"
                    />
                    <p>Lozinka:</p>
                    <input
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
                                    <button v-on:click="Login">Prijavi se</button>
                                
                </form>
              
            </div>
        </div>`,

    methods: {
        Login: function () {
            axios
                .post(
                    "rest/login/token",
                    { username: this.username, password: this.password },
                    {
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded",
                        },
                    }
                )
                .then((response) => {
                    alert("uspeo");
                    alert(response.data);
                    alert(response.headers);
                })
                .catch(function (error) {
                    alert("greska");
                    alert(error.data);
                });
        },
    },
});
