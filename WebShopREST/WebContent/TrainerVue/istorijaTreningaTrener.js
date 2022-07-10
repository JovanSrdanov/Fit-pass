Vue.component("istorijaTreningaTrener", {
    data: function () {
        return {
            pastActivity: {},
            searchSportFacility: "",
            searchStartDate: "",
            searchEndDate: "",

            searchPriceFree: false,
            searchPriceLow: 0,
            searchPriceHigh: 1000,

            sortPrice: "noSort",
            sortPriceString: "Cena ↕",

            sortSportFacilityName: "noSort",
            sortSportFacilityNameString: "Ime sportskog objekta ↕",

            sortDate: "noSort",
            sortDateString: "Datum ↕",

            filterWokroutType: "all",

            filterSportFacilitType: "",
        };
    },
    template: `
        <div class="WholeScreen">
            <h1>Moja istorija aktivnosti </h1>
            <table class="tableForKorisnici">
                <td>
                    <div class="SearchSortFilterWrapper">
                        <p>Pretraga:</p>

                        <input
                            v-model="searchSportFacility"
                            type="text"
                            name="searchSportFacility"
                            id="searchSportFacility"
                            placeholder="Naziv objekta"
                        />
                        <p>Početni i krajnji datum</p>
                        <input type="date"  v-model="searchStartDate"/>
                        <input type="date"   v-model="searchEndDate" />
                        <p>Cena doplate aktivnosti (od-do):</p>
                        <input type="number" min="0"  v-model="searchPriceLow"/>
                        <input type="number" min="1"   v-model="searchPriceHigh" />
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
                             v-model="searchPriceFree"
                        />

                        <label for="checkbox">Bez doplate za aktivnost</label>
                        </p>


                        <p>
                            <button v-on:click="SearchManagerActivity">Pretraži</button>
                        </p>
                        <p>Filtriranje:</p>
                        <label for="filterWokroutType">Izabrati tip aktivnosti:</label>
                        <select name="filterWokroutType" id="filterWokroutType" v-model="filterWokroutType">
                            <option value="personal">Personalni trening</option>
                            <option value="group">Grupni trening</option>
                            <option value="solo">Aktivnost bez trenera</option>
                            <option value="all">Svi</option>
                        </select>
                          <p>
                          <input
                            v-model="filterSportFacilitType"
                            type="text"
                            name="filterSportFacilitType"
                            id="filterSportFacilitType"
                            placeholder="Vrsta objekta"
                        />
                        </p>
                        <p>Sortiranje:</p>
                        <p>
                            <button v-on:click="PriceSortFunction">{{sortPriceString}}</button>
                            <button  v-on:click="DateSortFunction">{{sortDateString}}</button>
                        </p>
                        <p>
                            <button  v-on:click="SPORTSortFunction">{{sortSportFacilityNameString}}</button>
                        </p>
                    </div>
                </td>
                <td>
                    <div class="TabelaKorisnika">
                        <table>
                            <th>Naziv treninga</th>
                            <th>Naziv objekta</th>
                            <th>Tip objekta</th>
                            <th>Datum</th>
                            <th>Vreme</th>
                            <th>Tip treninga</th>
                            <th>Kupac</th>
                            <th>Cena doplate</th>
                            <tbody>
                                <tr
                                    v-for="u in searchFilterSortMenagerActivity"
                                >
                                    <td>{{u.workoutName}}</td>
                                    <td>{{u.facilityName}}</td>
                                    <td>DODAJ</td>
                                    <td>{{getDate(u.workoutDate)}}</td>
                                    <td>{{getTime(u.workoutDate)}}</td>
                                    <td>{{TypeConvert(u.workoutType)}}</td>
                                    <td>{{u.customerFullName}}</td>
                                    <td>{{getPrice(u.price)}}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </td>
            </table>
        </div>
  `,

    mounted() {
        var today = new Date();
        var start = new Date();
        var end = new Date();
        start.setDate(today.getDate() - 5);
        end.setDate(today.getDate() + 5);

        this.searchStartDate = start.toISOString().split("T")[0];
        this.searchEndDate = end.toISOString().split("T")[0];

        if (JSON.parse(localStorage.getItem("loggedInUser")) === null) {
            alert("Nemate pristup ovom sadržaja");
            window.location.href = "#/pocetna";
            return;
        }
        if (
            JSON.parse(localStorage.getItem("loggedInUser")).role != "trainer"
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

        axios.get("rest/workout/history/trainer", yourConfig).then((result) => {
            this.pastActivity = result.data;
        });
    },
    computed: {
        searchFilterSortMenagerActivity() {
            if (!this.pastActivity) return null;
            let TEMP = this.pastActivity;
            /*
            TEMP = TEMP.filter((PA) => {
                return (
                    PA.facilityType
                        .toLowerCase()
                        .indexOf(this.filterSportFacilitType.toLowerCase()) >
                    -1
                );
            });

            */
            if (this.filterWokroutType != "all") {
                TEMP = TEMP.filter((t) => {
                    return (
                        t.workoutType
                            .toLowerCase()
                            .indexOf(this.filterWokroutType.toLowerCase()) > -1
                    );
                });
            }

            if (this.sortPrice === "DSC") {
                TEMP = TEMP.sort((a, b) => {
                    return a.price - b.price;
                });
            }

            if (this.sortPrice === "ASC") {
                TEMP = TEMP.sort((a, b) => {
                    return b.price - a.price;
                });
            }

            if (this.sortDate === "DSC") {
                TEMP = TEMP.sort((a, b) => {
                    return a.workoutDate - b.workoutDate;
                });
            }

            if (this.sortDate === "ASC") {
                TEMP = TEMP.sort((a, b) => {
                    return b.workoutDate - a.workoutDate;
                });
            }

            if (this.sortSportFacilityName === "DSC") {
                TEMP = TEMP.sort((a, b) => {
                    return a.facilityName.localeCompare(b.facilityName);
                });
            }

            if (this.sortSportFacilityName === "ASC") {
                TEMP = TEMP.sort((a, b) => {
                    return b.facilityName.localeCompare(a.facilityName);
                });
            }

            return TEMP;
        },
    },
    methods: {
        SearchManagerActivity: function () {
            var priceMin = this.searchPriceLow;
            var priceMax = this.searchPriceHigh;

            const params = new URLSearchParams();
            if (this.searchPriceFree) {
                priceMin = -1;
                priceMax = -1;
            }

            var start = new Date(this.searchStartDate);
            var end = new Date(this.searchEndDate);

            var dateStringStart =
                start.getDate() +
                "/" +
                start.getMonth() +
                "/" +
                start.getFullYear();

            var dateStringEnd =
                end.getDate() + "/" + end.getMonth() + "/" + end.getFullYear();

            params.append("priceMin", priceMin);
            params.append("priceMax", priceMax);
            console.log(priceMin, priceMax);

            params.append("startDate", dateStringStart);
            params.append("endDate", dateStringEnd);

            params.append("VIDIkODSTRAHINJE", this.searchSportFacility);

            axios
                .post("rest/workout/history/trainer/s", params, {
                    headers: {
                        Authorization: localStorage.getItem("token"),
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                })
                .then((response) => {
                    this.pastActivity = response.data;
                })
                .catch((error) => {
                    alert("Greska u search metodi");
                });
        },

        SPORTSortFunction() {
            this.sortDate = "noSort";
            this.sortDateString = "Datum ↕";
            this.sortPrice = "noSort";
            this.sortPriceString = "Cena ↕";

            if (this.sortSportFacilityName == "ASC") {
                this.sortSportFacilityName = "DSC";
                this.sortSportFacilityNameString = "Ime sportskog objekta ↓";
            } else {
                this.sortSportFacilityName = "ASC";
                this.sortSportFacilityNameString = "Ime sportskog objekta ↑";
            }
        },

        PriceSortFunction() {
            this.sortDate = "noSort";
            this.sortDateString = "Datum ↕";
            this.sortSportFacilityName == "noSort";
            this.sortSportFacilityNameString = "Ime sportskog objekta  ↕";

            if (this.sortPrice == "ASC") {
                this.sortPrice = "DSC";
                this.sortPriceString = "Cena  ↓";
            } else {
                this.sortPrice = "ASC";
                this.sortPriceString = "Cena ↑";
            }
        },

        DateSortFunction() {
            this.sortPrice = "noSort";
            this.sortPriceString = "Cena ↕";
            this.sortSportFacilityName == "noSort";
            this.sortSportFacilityNameString = "Ime sportskog objekta  ↕";

            if (this.sortDate == "ASC") {
                this.sortDate = "DSC";
                this.sortDateString = "Datum  ↓";
            } else {
                this.sortDate = "ASC";
                this.sortDateString = "Datum ↑";
            }
        },

        getPrice: function (price) {
            if (price == -1) return "Nema doplate";
            return price + " RSD";
        },
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
