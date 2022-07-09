Vue.component("napraviObjekat", {
    data: function () {
        return {
            usernameManager: "",
            passwordManager: "",
            nameManager: "",
            surnameManager: "",
            genderManager: "male",
            dateOfBirthManager: "1900-01-01",

            availableManagers: null,
            selectedManager: {},

            logoSend: null,
            logoFile: null,

            name: "",
            facilityType: "",
            workEnd: "21:00",
            workStart: "07:00",
            lonShow: 20.457273,
            latShow: 44.787197,
            street: "",
            streetNumber: "",
            city: "",
            postCode: "",

            allEntered: "",
            existsSP: "",

            managerExists: "",
            managerDataEntered: "",
        };
    },
    template: `
          <div class="WholeScreen">
            <h1>Napravi sportski objekat</h1>
            <table class="tableForNapraviObjekat">
                <td>
                    <div class="ChooseLocation">
                        <p>Odabir lokacije:</p>
                        <div class="mapClass" id="mapShowCreate"></div>
                        <p>Gegrafska dužina i širina:</p>
                        <p>{{lonShow}},{{latShow}}</p>
                        <p>Ulica i broj: {{street}} {{streetNumber}}</p>
                        <p>Grad i poštanski broj: {{city}} {{postCode}}</p>
                    </div>
                </td>
                <td>
                    <p class="white">Naziv:</p>

                    <input
                        type="text"
                        name="name"
                        id="name"
                        placeholder="Naziv"
                        v-model="name"
                    />

                    <p class="white">Tip objekta:</p>
                    <input
                        v-model="facilityType"
                        type="text"
                        name="facilityType"
                        id="facilityType"
                        placeholder="Tip objekta"
                    />
                    <p class="white">Početak i kraj radnog vremena:</p>
                    <input
                        type="time"
                        name="workStart"
                        id="workStart"
                        v-model="workStart"
                    />
                    <input
                        type="time"
                        name="workEnd"
                        id="workEnd"
                        v-model="workEnd"
                    />

                    <p class="white">
                        Izabrani menadžer: {{selectedManager.name}}
                        {{selectedManager.surname}} -
                        {{selectedManager.username}}
                    </p>
                    <p class="white">Izaberite logo:</p>

                    <input
                        type="file"
                        name="logoFile"
                        id="logoFile"
                        accept="image/*"
                        @change="handleFileUpload"
                      class="custom-file-input"
                      
                    />

                    <p>
                        <button v-on:click="CreateSportFacility">
                            Kreiraj
                        </button>
                    </p>
                    <p>
                        <img
                            v-bind:src="logoFile"
                            height="200px"
                            width="200px"
                        />
                    </p>

                    <p class="white">{{allEntered}} {{existsSP}}</p>
                </td>
                <td>
                    <div class="ChooseManager">
                        <table>
                           
                            <th>Ime</th>
                            <th>Prezime</th>
                            <th>Korisničko ime</th>
                            <tbody>
                                <tr
                                    v-for="AM in availableManagers"
                                    v-on:click="selectM(AM)"
                                    v-bind:class="{selectedManagerClass : selectedManager.id===AM.id}"
                                >
                                  
                                    <td>{{AM.name}}</td>
                                    <td>{{AM.surname}}</td>
                                    <td>{{AM.username}}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="FastRegsitration">
                        <p class="white">Brza registracija novog menadžera</p>
                       
                        <input
                            required
                            v-model="usernameManager"
                            type="text"
                            name="usernameManager"
                            id="usernameManager"
                            placeholder="Korisničko ime"
                        />

                       <br />
                        <br />
                        <input
                            
                            v-model="passwordManager"
                            type="password"
                            name="passwordManager"
                            id="password"
                            placeholder="Lozinka"
                        />

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
                                id="showPassword"
                                onclick="myFunction()"
                            />

                            <label class="white" for="checkbox"> Prikaz lozinke </label>
                        </p>


                        <input
                            v-model="nameManager"
                            type="text"
                            name="nameManager"
                            id="nameManager"
                            placeholder="Ime"
                            required
                        />
                        <br />
                        <br />
                     
                        <input
                            required
                            v-model="surnameManager"
                            type="text"
                            name="surnameManager"
                            id="surnameManager"
                            placeholder="Prezime"
                        />

                        <p class="white">Pol:
                        <select
                            name="gender"
                            id="gender"
                            v-model="genderManager"
                        >
                            <option selected="selected" value="male">
                                Muški
                            </option>
                            <option value="female">Ženski</option>
                        </select>
                        </p>
                        <p class="white">Datum rođenja:
                        <input
                            type="date"
                            value="1900-01-01"
                            v-model="dateOfBirthManager"
                            name="dateOfBirthManager"
                            id="dateOfBirthManager"
                        />
                        </p>
            
                        <button v-on:click="RegisterManager">
                            Registruj se
                        </button>

                        <p class="white" > {{managerDataEntered}} {{managerExists}}
                           
                        </p>
                        
                    </div>
                </td>
            </table>
        </div>
  `,
    mounted() {
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
        yourConfig = {
            headers: {
                Authorization: localStorage.getItem("token"),
            },
        };

        axios.get("rest/manager/available", yourConfig).then((response) => {
            this.availableManagers = response.data;
        });

        var map = new ol.Map({
            target: "mapShowCreate",
            layers: [
                new ol.layer.Tile({
                    source: new ol.source.OSM(),
                }),
            ],
            view: new ol.View({
                center: ol.proj.fromLonLat([this.lonShow, this.latShow]),
                zoom: 12,
            }),
        });
        map.on("click", (evt) => {
            var latLong = ol.proj.transform(
                evt.coordinate,
                "EPSG:3857",
                "EPSG:4326"
            );
            var latClick = latLong[1];
            var longClick = latLong[0];

            this.latShow = latClick;
            this.lonShow = longClick;

            var ClickedLocation = new ol.Feature({
                geometry: new ol.geom.Point(
                    ol.proj.fromLonLat([longClick, latClick])
                ),
            });

            ClickedLocation.setStyle(
                new ol.style.Style({
                    image: new ol.style.Icon(
                        /** @type {olx.style.IconOptions} */ ({
                            color: "blue",

                            src: "https://openlayers.org/en/v4.6.5/examples/data/dot.png",
                        })
                    ),
                })
            );

            if (map.dinamicPinLayer !== undefined) {
                map.removeLayer(map.dinamicPinLayer);

                var vectorSource2 = new ol.source.Vector({
                    features: [ClickedLocation],
                });
                map.dinamicPinLayer = new ol.layer.Vector({
                    source: vectorSource2,
                });
                map.addLayer(map.dinamicPinLayer);
                //or create another pin
            } else {
                var vectorSource2 = new ol.source.Vector({
                    features: [ClickedLocation],
                });
                map.dinamicPinLayer = new ol.layer.Vector({
                    source: vectorSource2,
                });
                map.addLayer(map.dinamicPinLayer);
            }

            const coords_click = ol.proj.transform(
                evt.coordinate,
                "EPSG:3857",
                "EPSG:4326"
            );

            const lon = coords_click[0];

            const lat = coords_click[1];

            const data_for_url = {
                lon: lon,
                lat: lat,
                format: "json",
                limit: 1,
            };

            const encoded_data = Object.keys(data_for_url)
                .map(function (k) {
                    return (
                        encodeURIComponent(k) +
                        "=" +
                        encodeURIComponent(data_for_url[k])
                    );
                })
                .join("&");

            const url_nominatim =
                "https://nominatim.openstreetmap.org/reverse?" + encoded_data;

            axios.get(url_nominatim).then((response) => {
                const data_json = response.data;

                const res_lon = data_json.lon;
                const res_lat = data_json.lat;
                this.latShow = res_lat;
                this.lonShow = res_lon;

                const res_address = data_json.address;

                const address_postcode = res_address.postcode;
                this.postCode = address_postcode;

                const address_road = res_address.road;
                this.street = address_road;

                const address_house_number = res_address.house_number;
                this.streetNumber = address_house_number;

                const address_city = res_address.city;
                this.city = address_city.replace("City", "");
            });
        });
    },
    methods: {
        CreateSportFacility: function () {
            this.allEntered = "";
            this.existsSP = "";
            // uradi proveru za end i start
            // uradi proveru za end i start
            // uradi proveru za end i start
            // uradi proveru za end i start

            if (
                this.logoSend === null ||
                this.name === "" ||
                this.facilityType === "" ||
                this.street === "" ||
                this.streetNumber === "" ||
                this.city === "" ||
                this.postCode === "" ||
                this.selectedManager === {}
            ) {
                this.allEntered = "Niste uneli sve podatke";
                return;
            }

            yourConfig = {
                headers: {
                    Authorization: localStorage.getItem("token"),
                    "Content-Type": "application/json",
                },
            };
            const formData = new FormData();
            formData.append("file", this.logoSend);
            axios
                .post("rest/files/upload", formData, {
                    headers: {
                        "Content-Type": "multipart/form-data",
                        Authorization: localStorage.getItem("token"),
                    },
                })
                .then(() => {
                    axios
                        .post(
                            "rest/facilitys/new",
                            {
                                name: this.name,
                                facilityType: this.facilityType,
                                workStart: this.workStart,
                                workEnd: this.workEnd,

                                street: this.street,
                                streetNumber: this.streetNumber,
                                city: this.city,
                                postCode: this.postCode,
                                latitude: this.latShow,
                                longitude: this.lonShow,

                                managerId: this.selectedManager.id,
                            },
                            yourConfig
                        )
                        .then((response) => {
                            setTimeout(function () {
                                window.location.href = "#/pocetna";
                            }, 2500);
                        })
                        .catch((error) => {
                            this.existsSP =
                                "Već postoji objekat sa ovakvim nazivom";
                        });
                })
                .catch((error) => {
                    alert("NIJE USPEO");
                });
        },

        selectM: function (managaer) {
            this.selectedManager = managaer;
        },
        RegisterManager: function () {
            this.managerDataEntered = "";
            this.managerExists = "";

            if (
                this.usernameManager === "" ||
                this.passwordManager === "" ||
                this.nameManager === "" ||
                this.surnameManager === ""
            ) {
                this.managerDataEntered = "Niste uneli sve podatke";
                return;
            }

            axios
                .post(
                    "rest/manager/reg",
                    {
                        username: this.usernameManager,
                        password: this.passwordManager,
                        name: this.nameManager,
                        surname: this.surnameManager,
                        gender: this.genderManager,
                        birthDate: this.dateOfBirthManager,
                        role: "manager",
                    },
                    {
                        headers: {
                            "Content-Type": "application/json",
                            Authorization: localStorage.getItem("token"),
                        },
                    }
                )
                .then((response) => {
                    yourConfig = {
                        headers: {
                            Authorization: localStorage.getItem("token"),
                        },
                    };

                    axios
                        .get("rest/manager/available", yourConfig)
                        .then((response) => {
                            this.availableManagers = response.data;
                            this.username == "";
                            this.password == "";
                            this.nameManager == "";
                            this.surname == "";
                        });
                })
                .catch((error) => {
                    this.managerExists =
                        "Već postoji korisnik sa ovim korisničkim imenom";
                });
        },

        handleFileUpload(e) {
            const file = e.target.files[0];
            this.logoSend = file;
            this.logoFile = URL.createObjectURL(file);
        },
        getImgUrl(slika) {
            if (slika === null) return "Images/Logo.png";
            return "FacilityLogo/" + slika;
        },
    },
});
