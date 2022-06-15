Vue.component("login", {
    data: function () {
        return {
            userName: "",
            password: "",
            allFilled: "OK",
        };
    },
    template: `
   <div class="centriraj">
            <img src="Images/Logo.png" alt="Slika logoa" height="250" />

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
                    <table>
                        <tr>
                            <td>
                                <input
                                    class="checkbox"
                                    type="checkbox"
                                    id="showPassword"
                                    onclick="myFunction() "
                                />
                            </td>
                            <td>
                                <label for="checkbox"> Prikaz lozinke </label>
                            </td>
                        </tr>
                    </table>

                    <table class="buttons">
                        <tr>
                            <td>
                                <a href="Pocetna.html"
                                    ><button>Početna</button></a
                                >
                            </td>

                            <td class="rightButton">
                                <a href="Registracija.html"
                                    ><button>Registruj se</button></a
                                >
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>`,
});
