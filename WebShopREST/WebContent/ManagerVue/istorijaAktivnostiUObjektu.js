Vue.component("istorijaAktivnostiUObjektu", {
    data: function () {
        return {
            pastActivity: {},
            searchStartDate: "",
            searchEndDate: "",

            searchPriceFree: false,
            searchPriceLow: 100,
            searchPriceHih: 1000,

            sortPrice: "noSort",
            sortPriceString: "Cena ↕",

            sortDate: "noSort",
            sortDateString: "Datum ↕",

            filterWokroutType: "All",
        };
    },
    template: `
         <div class="WholeScreen">
            <h1>Istorija aktivnosti u objektu</h1>
               <table class="tableForKorisnici">
                <td>
                    <div class="SearchSortFilterWrapper">
                        <p>Pretraga:</p>

                        <input
                           
                            type="text"
                            name="searchName"
                            id="searchName"
                            placeholder="Ime"
                        />

                        <br />
                        <br />
                        <input
                           
                            type="text"
                            name="searchSurname"
                            id="searchSurname"
                            placeholder="Prezime"
                        />
                        <br />
                        <br />
                        <input
                           
                            type="text"
                            name="searchUsername"
                            id="searchUsername"
                            placeholder="Korisničko ime"
                        />
                        <br />
                        <br />

                        <button >Pretraži</button>

                        <p>Filtriranje:</p>

                        <label for="Uloga">Izabrati ulogu:</label>
                        <select name="Uloga" id="Uloga" >
                            <option value="all">Svi</option>
                            <option value="customer">Kupac</option>
                            <option value="admin">Administrator</option>
                            <option value="manager">Menadžer</option>
                            <option value="trainer">Trener</option>
                        </select>
                        <br />
                        <br />
                        <label for="CustomerType">Izabrati tip kupca</label>
                        <select
                            name="CustomerType"
                            id="CustomerType"
                           
                        >
                            <option value="all">Svi</option>
                            <option value="Gold">Zlatni</option>
                            <option value="Silver">Srebrni</option>
                            <option value="Bronze">Bronzani</option>
                        </select>

                        <p>Sortiranje:</p>
                        <p>
                            <button >
                                {{sortPriceString}}
                            </button>
                            <button>
                                {{sortDateString}}
                            </button>
                        </p>

                       

                      
                    </div>
                </td>
                <td>
                    <div class="TabelaKorisnika">
                        <table>
                            <th>Naziv treninga</th>
                            <th>Datum</th>
                            <th>Vreme</th>
                            <th>Tip treninga</th>
                            <th>Kupac</th>
                            <th>Trener</th>
                            <tbody>
                                <tr v-for="u in searchFilterSortMenagerActivity">
                                    <td>{{u.workoutName}}</td>
                                    <td>{{getDate(u.workoutDate)}}</td>
                                    <td>{{getTime(u.workoutDate)}}</td>
                                    <td>{{TypeConvert(u.workoutType)}}</td>
                                    <td>{{u.customerFullName}}</td>
                                    <td>{{u.trainerFullName}}</td>                        
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </td>
            </table> 
        </div>
  `,

    mounted() {
        this.facId = this.$route.params.id;

        if (JSON.parse(localStorage.getItem("loggedInUser")) === null) {
            alert("Nemate pristup ovom sadržaja");
            window.location.href = "#/pocetna";
            return;
        }
        if (
            JSON.parse(localStorage.getItem("loggedInUser")).role != "manager"
        ) {
            alert("Nemate pristup ovom sadržaja");
            window.location.href = "#/pocetna";
            return;
        }
        if (
            JSON.parse(localStorage.getItem("loggedInUser")).facilityId !=
            this.facId
        ) {
            alert("Nemate pristup ovom sadržaja");
            window.location.href = "#/pocetna";
            return;
        }
        yourConfig = {
            headers: {
                Authorization: localStorage.getItem("token"),
            },
        };

        axios.get("rest/workout/history/manager", yourConfig).then((result) => {
            this.pastActivity = result.data;
            console.log(this.pastActivity);
        });
    },
    computed: {
        searchFilterSortMenagerActivity() {
            if (!this.pastActivity) return null;
            let TEMP = this.pastActivity;
            return TEMP;
        },
    },
    methods: {
        TypeConvert: function (type) {
            if (type === "personal") return "Personalni trening";
            if (type === "group") return "Grupni trening";
            if (type === "solo") return "Aktivnost bez trenera";

            return "Greska";
        },
        getDate: function (milliseconds) {
            const d = new Date();
            d.setTime(milliseconds);
            return d.toLocaleDateString("sr-RS");
        },

        getTime: function (milliseconds) {
            const d = new Date();
            d.setTime(milliseconds);
            return d.toLocaleTimeString("sr-RS");
        },
    },
});
