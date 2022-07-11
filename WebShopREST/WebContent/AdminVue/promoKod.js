Vue.component("promoKod", {
    data: function () {
        return {
            codes: null,
            code: "",
            validDate: null,
            usageCount: 1,
            discountPercentage: 50,

            codeExists: "",
            enterAll: "",
        };
    },
    template: `
      <div>
    
        <div class="centriraj">
            <h1>Kreiraj novi promo kod</h1>

            <p class="white">Oznaka:</p>
            <input v-model="code" type="text" name="code" id="code" placeholder="Oznaka" />
            <p  class="white">Važi do:</p>
            <input
              v-model="validDate"
                type="date"
                name="validDate"
                id="validDate"
                placeholder="Period važenja"
            />
            <p class="white">Broj korišćenja:
            <input min="1"  max="100" type="number" name="usageCount" id="usageCount"   v-model="usageCount" />
            </p>
            <p class="white">Procenat za koji umanjuje iznos:
            <input
              v-model="discountPercentage"
                min="1"
                max="100"
                type="number"
                name="discountPercentage"
                id="discountPercentage"
            />
            </p>
            <p>
                <button v-on:click="createPromoCode">Kreiraj</button>
            </p>

            <p class="white">{{enterAll}}{{codeExists}}</p>
            <h2  class="white">Postojeći kodovi</h2>
            
             <div class="TabelaKodova">
                <table>
                    <th>Oznaka</th>
                    <th>Važi do</th>
                    <th>Broj preostalih korišćenja</th>
                    <th>Procenat za koji umanjuje iznos</th>
                    <th>Obriši</th>
                   

                    <tbody>
                         
                        <tr v-for="c in codes">
                            <td>{{c.code}}</td>
                            <td>{{getDate(c.validDate)}}</td>
                            <td>{{c.usageCount}}</td>
                            <td>{{c.discountPercentage}}%</td>                                       
                            <td><button class="ObrisiDugme" v-on:click="OBRISI_PROMOKOD(c.id)" >Obriši</button></td>
                        </tr>
                    </tbody>
                </table>
            </div>

           
        </div>
         
        </div>      
  `,
    mounted() {
        this.validDate = new Date().toISOString().split("T")[0];

        yourConfig = {
            headers: {
                Authorization: localStorage.getItem("token"),
            },
        };

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

        axios.get("rest/promoCode", yourConfig).then((r) => {
            this.codes = r.data;
        });
    },
    methods: {
        OBRISI_PROMOKOD: function (ID) {
            yourConfig = {
                headers: {
                    Authorization: localStorage.getItem("token"),
                },
            };
            axios
                .delete("rest/promoCode/delete/" + ID, yourConfig)
                .then((result) => {
                    alert("Obrisan promo code");

                    this.codes = this.codes.filter((data) => data.id != ID);
                })
                .catch((err) => {
                    alert("BAS JAKA GRESKA");
                });
        },
        getDate: function (milliseconds) {
            const d = new Date();
            d.setTime(milliseconds);
            return d.toLocaleDateString("sr-RS");
        },
        createPromoCode: function () {
            this.codeExists = "";
            this.enterAll = "";
            var now = new Date();
            var checkDate = new Date(this.validDate);
            if (checkDate <= now) {
                alert("Datum važenja promo koda mora biti u budučnosti");
                return;
            }

            if (isNaN(this.usageCount)) {
                alert("Unestite brojcanu vrednost u broj upotreba");
                return;
            }
            if (isNaN(this.discountPercentage)) {
                alert("Unestite brojcanu vrednost u procenat popusta");
                return;
            }
            if (this.discountPercentage < 1 || this.discountPercentage > 99) {
                alert("Unestite validni procenat popusta");
                return;
            }
            if (this.usageCount < 1) {
                alert("Unestite validni broj korišćenja (veći od 0)");
                return;
            }

            if (this.code === "") {
                this.enterAll = "Niste uneli sve podatke";
                return;
            }

            yourConfig = {
                headers: {
                    Authorization: localStorage.getItem("token"),
                    "Content-Type": "application/json",
                },
            };
            yourConfig2 = {
                headers: {
                    Authorization: localStorage.getItem("token"),
                },
            };

            axios
                .post(
                    "rest/promoCode/new",
                    {
                        code: this.code,
                        validDate: this.validDate,
                        usageCount: this.usageCount,
                        discountPercentage: this.discountPercentage,
                    },
                    yourConfig
                )
                .then((response) => {
                    axios.get("rest/promoCode", yourConfig2).then((r) => {
                        window.location.href = "#/pocetna";
                    });
                })
                .catch((error) => {
                    this.codeExists = "Postoji već kod pod tom oznakom";
                });
        },
    },
});
