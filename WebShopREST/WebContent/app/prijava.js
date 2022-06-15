Vue.component("prijava", {
    data: function () {
        return {
            userName: "",
            password: "",
            allFilled: "OK",
        };
    },
    template: `
   <div class="centriraj">
            <div class="LoginDeo">
                <h1>Prijavite se</h1>
                <form action="Pocetna.html" method="post">
                    <p>Korisničko ime:</p>
                    <input
                        type="text"
                        name="korisnickoIme"
                        id="korisnickoIme"
                        placeholder="Korisničko ime"
                    />
                    <p>Lozinka:</p>
                    <input
                        type="password"
                        name="lozinka"
                        id="lozinka"
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
                                      <input type="submit" value="Prijavi se" >
                                
                </form>
            </div>
        </div>`,
});
