Vue.component("pocetna", {
    data: function () {
        return {
            OpenFacilitys: false,
            SportFacility: null,
            name: "",
            facilityType: "",
            locationString: "",
            rating: 0,
            loggedInUser: {},
            facilityTypeFilter: "",
            NazivSort: "noSort",
            LokacijaSort: "noSort",
            OcenaSort: "noSort",

            NazivString: "Naziv ↕",
            LokacijaString: "Lokacija ↕",
            OcenaString: "Ocena ↕",
            locationLon: 0,
            locationLat: 0,
        };
    },
    template: ` 
     <div class="WholeScreen">
            <h1 >Sportski objekti</h1>
            <table class="tableForPocetna">
                <td>
                    <div class="SearchSortFilterWrapper">
                        <p>Pretraga:</p>

                        <input
                            v-model="name"
                            type="text"
                            name="name"
                            id="name"
                            placeholder="Naziv objekta"
                        />

                        <br />
                        <br />
                        <input
                            v-model="facilityType"
                            type="text"
                            name="facilityType"
                            id="facilityType"
                            placeholder="Tip objekta"
                        />
                        <br />
                        <br />
                        <input
                            v-model="locationString"
                            type="text"
                            name="locationString"
                            id="locationString"
                            placeholder="Grad/Ulica"
                        />
                        <br />

                        <p>
                            Prosečna ocena:
                            <input
                                min="0"
                                max="5"
                                v-model="rating"
                                type="number"
                                name="rating"
                                id="rating"
                            />
                        </p>

                        <button v-on:click="Search">Pretraži</button>

                        <p>Filtriranje:</p>
                        <input
                            v-model="facilityTypeFilter"
                            type="text"
                            name="facilityTypeFilter"
                            id="facilityTypeFilter"
                            placeholder="Tip objekta"
                        />

                        <p
                            style="
                                display: flex;
                                align-items: center;
                                justify-content: center;
                            "
                        >
                            <input
                                type="checkbox"
                                id="OtvoreniObjekti"
                                name="OtvoreniObjekti"
                                v-model="OpenFacilitys"
                            />
                            <label for="OtvoreniObjekti"
                                >Otvoreni objekti</label
                            >
                        </p>

                        <p>Sortiranje</p>
                        <p>
                            <button v-on:click="NazivSortFunction">
                                {{NazivString}}
                            </button>
                            <button v-on:click="LokacijaSortFunction">
                                {{LokacijaString}}
                            </button>
                            <button v-on:click="OcenaSortFunction">
                                {{OcenaString}}
                            </button>
                        </p>
                        <p>
                            <button
                                v-on:click="CreateNewObject"
                                v-if="loggedInUser.role=='admin'"
                            >
                                Napravi objekat
                            </button>
                        </p>
                    </div>
                </td>
                <td>
                    <div class="facilityOrActivityTableStyle">
                        <table>
                            <tbody>
                                <tr v-for="p in searchFilterSort">
                                    <td>
                                        <img
                                            v-bind:src="getImgUrl(p.facility.logo)"
                                            alt="LOGO"
                                            height="250"
                                            width="250"
                                        />
                                    </td>
                                    <td>
                                        <ul>
                                            <li><strong>{{p.facility.name}}</strong></li>
                                            <li>{{p.facility.facilityType}}</li>
                                            <li>
                                                {{p.location.street}}
                                                {{p.location.streetNumber}}
                                            </li>
                                            <li>
                                                {{p.location.city}}
                                                {{p.location.postCode}}
                                            </li>
                                            <li>
                                                ({{p.location.longitude}},
                                                {{p.location.latitude}})
                                            </li>
                                            <li>
                                                Prosečna ocena:
                                                {{p.facility.rating}}/5
                                            </li>
                                            <li>
                                                Radno vreme:
                                                {{p.facility.workStart}} -
                                                {{p.facility.workEnd}}
                                                {{checkStatus(p.facility.workStart,p.facility.workEnd)}}
                                            </li>

                                            <br />
                                            <li>
                                                <button
                                                    v-on:click="goToFacilityPage(p)"
                                                >
                                                    Detaljnije
                                                </button>
                                                <button
                                                    v-on:click="deleteFacility(p)"
                                                    v-if="loggedInUser.role=='admin'"
                                                >
                                                    Obriši
                                                </button>
                                            </li>
                                        </ul>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </td>
            </table>
        </div>
   
                                                    
    `,

    mounted() {
        //////////////   <div  v-bind:id="'map'+p.facility.id" class="map"></div>
        axios.get("rest/facilitys").then((response) => {
            this.SportFacility = response.data;

            this.loggedInUser = JSON.parse(
                localStorage.getItem("loggedInUser")
            );
            if (
                this.loggedInUser === null ||
                this.loggedInUser.role != "admin"
            ) {
                this.loggedInUser = {};
                this.loggedInUser.role = null;
            }
        });
        this.getLocation();
    },
    computed: {
        searchFilterSort() {
            var today = new Date();

            if (!this.SportFacility) return null;

            let tempSportFacilitys = this.SportFacility;

            if (this.OpenFacilitys) {
                tempSportFacilitys = tempSportFacilitys.filter((sp) => {
                    var workDayStartTime = new Date();
                    var workDayEndTime = new Date();

                    if (
                        parseInt(sp.facility.workStart.split(":")[0]) >
                        parseInt(sp.facility.workEnd.split(":")[0])
                    ) {
                        workDayEndTime.setDate(workDayEndTime.getDate() + 1);
                    }

                    workDayStartTime.setHours(
                        parseInt(sp.facility.workStart.split(":")[0])
                    );
                    workDayStartTime.setMinutes(
                        parseInt(sp.facility.workStart.split(":")[1])
                    );

                    workDayEndTime.setHours(
                        parseInt(sp.facility.workEnd.split(":")[0])
                    );
                    workDayEndTime.setMinutes(
                        parseInt(sp.facility.workEnd.split(":")[1])
                    );

                    return workDayStartTime < today && workDayEndTime > today;
                });
            }
            tempSportFacilitys = tempSportFacilitys.filter((sp) => {
                return (
                    sp.facility.facilityType
                        .toLowerCase()
                        .indexOf(this.facilityTypeFilter.toLowerCase()) > -1
                );
            });

            if (this.NazivSort === "DSC") {
                tempSportFacilitys = tempSportFacilitys.sort((a, b) => {
                    return a.facility.name.localeCompare(b.facility.name);
                });
            }

            if (this.NazivSort === "ASC") {
                tempSportFacilitys = tempSportFacilitys.sort((a, b) => {
                    return b.facility.name.localeCompare(a.facility.name);
                });
            }

            if (this.OcenaSort === "DSC") {
                tempSportFacilitys = tempSportFacilitys.sort((a, b) => {
                    return a.facility.rating - b.facility.rating;
                });
            }

            if (this.OcenaSort === "ASC") {
                tempSportFacilitys = tempSportFacilitys.sort((a, b) => {
                    return b.facility.rating - a.facility.rating;
                });
            }

            if (this.LokacijaSort === "DSC") {
                tempSportFacilitys = tempSportFacilitys.sort((a, b) => {
                    return (
                        this.getDistanceFromLatLonInKm(
                            a.location.latitude,
                            a.location.longitude,
                            this.locationLat,
                            this.locationLon
                        ) -
                        this.getDistanceFromLatLonInKm(
                            b.location.latitude,
                            b.location.longitude,
                            this.locationLat,
                            this.locationLon
                        )
                    );
                });
            }

            if (this.LokacijaSort === "ASC") {
                tempSportFacilitys = tempSportFacilitys.sort((a, b) => {
                    return (
                        this.getDistanceFromLatLonInKm(
                            b.location.latitude,
                            b.location.longitude,
                            this.locationLat,
                            this.locationLon
                        ) -
                        this.getDistanceFromLatLonInKm(
                            a.location.latitude,
                            a.location.longitude,
                            this.locationLat,
                            this.locationLon
                        )
                    );
                });
            }

            return tempSportFacilitys;
        },
    },

    methods: {
        checkStatus(start, end) {
            var today = new Date();

            var workDayStartTime = new Date();
            var workDayEndTime = new Date();

            if (parseInt(start.split(":")[0]) > parseInt(end.split(":")[0])) {
                workDayEndTime.setDate(workDayEndTime.getDate() + 1);
            }

            workDayStartTime.setHours(parseInt(start.split(":")[0]));
            workDayStartTime.setMinutes(parseInt(start.split(":")[1]));

            workDayEndTime.setHours(parseInt(end.split(":")[0]));
            workDayEndTime.setMinutes(parseInt(end.split(":")[1]));

            if (workDayStartTime < today && workDayEndTime > today) {
                return "(Radi trenutno)";
            } else {
                return "(Ne radi trenutno)";
            }
        },
        getDistanceFromLatLonInKm: function (lat1, lon1, lat2, lon2) {
            var R = 6371; // Radius of the earth in km
            var dLat = this.deg2rad(lat2 - lat1); // deg2rad below
            var dLon = this.deg2rad(lon2 - lon1);
            var a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(this.deg2rad(lat1)) *
                    Math.cos(this.deg2rad(lat2)) *
                    Math.sin(dLon / 2) *
                    Math.sin(dLon / 2);
            var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            var d = R * c; // Distance in km
            return d;
        },

        deg2rad: function (deg) {
            return deg * (Math.PI / 180);
        },

        getLocation() {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(this.showPosition);
            }
        },

        showPosition(position) {
            this.locationLat = position.coords.latitude;
            this.locationLon = position.coords.longitude;
        },
        NazivSortFunction() {
            this.LokacijaSort = "noSort";
            this.OcenaSort = "noSort";

            this.LokacijaString = "Lokacija ↕";
            this.OcenaString = "Ocena ↕";

            if (this.NazivSort == "ASC") {
                this.NazivString = "Naziv ↓";
                this.NazivSort = "DSC";
            } else {
                this.NazivSort = "ASC";
                this.NazivString = "Naziv ↑";
            }
        },
        LokacijaSortFunction() {
            this.NazivSort = "noSort";
            this.NazivString = "Naziv ↕";
            this.OcenaSort = "noSort";
            this.OcenaString = "Ocena ↕";
            if (this.LokacijaSort == "ASC") {
                this.LokacijaSort = "DSC";
                this.LokacijaString = "Lokacija ↓";
            } else {
                this.LokacijaSort = "ASC";
                this.LokacijaString = "Lokacija ↑";
            }
        },
        OcenaSortFunction() {
            this.NazivSort = "noSort";
            this.NazivString = "Naziv ↕";
            this.LokacijaSort = "noSort";
            this.LokacijaString = "Lokacija ↕";
            if (this.OcenaSort == "ASC") {
                this.OcenaSort = "DSC";
                this.OcenaString = "Ocena ↓";
            } else {
                this.OcenaString = "Ocena ↑";
                this.OcenaSort = "ASC";
            }
        },

        CreateNewObject: function () {
            window.location.href = "#/napraviObjekat";
        },
        goToFacilityPage: function (facility) {
            router.push(`/pregledObjekta/${facility.facility.id}`);
        },

        deleteFacility: function (facility) {
            window.location.href = "#/pocetna";
        },
        getImgUrl(slika) {
            return "FacilityLogo/" + slika;
        },

        Search() {
            const params = new URLSearchParams();
            params.append("name", this.name);
            params.append("facilityType", this.facilityType);
            params.append("locationString", this.locationString);
            params.append("rating", this.rating);
            axios
                .post("rest/facilitys/search", params, {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                })
                .then((response) => {
                    this.SportFacility = response.data;
                })
                .catch((error) => {
                    alert("Greska u search metodi");
                });
        },
    },
});
