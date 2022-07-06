Vue.component("napraviObjekat", {
    data: function () {
        return {
            availableManagers: null,
            selectedManager: null,
            name: "",
            facilityType: "",
            workEnd: null,
            workStart: null,
            logoFile: null,

            lonShow: 20.457273,
            latShow: 44.787197,
            street: "",
            streetNumber: "",
            city: "",
            postCode: "",
            allEntered: "",
        };
    },
    template: `
      <div>
        <h1>Napravi objekat</h1>
    
        <div>
            
            <div class="ChooseLocation">
                   <p>Odabri lokacije:</p>
                <div class="mapShowCreate" id="mapShowCreate"></div>
                    <p>Gegrafska dužina i širina:</p>
                    <p>{{lonShow}},{{latShow}}</p>
                    <p>Ulica i broj: {{street}} {{streetNumber}}</p>
                    <p>Grad i poštanski broj: {{city}} {{postCode}}</p>
            </div>
            <div>
                <div class="ChooseManager">
                    <table>
                    <td>Ime</td>
                    <td>Prezime</td>
                    <td>Korisničko ime</td>
                    <td>Obriši</td>
                   

                    <tbody>
                         
                        <tr v-for="c in codes">
                            <td>{{c.code}}</td>
                            <td>{{getDate(c.validDate)}}</td>
                            <td>{{c.usageCount}}</td>
                            <td>{{c.discountPercentage}}%</td>                                       
                            <td><button>Obriši</button></td>
                        </tr>
                    </tbody>
                </table>

                </div>

                 <div class="FastRegsitration">

                </div>

             </div> 
            <p>Naziv:</p>
            
            <input type="text" name="name" id="name" placeholder="Naziv" />

            <p>Tip objekta:</p>
            <input
                type="text"
                name="facilityType"
                id="facilityType"
                placeholder="Tip objekta"
            />
            <p>Početak radnog vremena:</p>
            <input type="time" name="workStart" id="workStart" />
            <p>Kraj radnog vremena:</p>
            <input type="time" name="workEnd" id="workEnd" />
            <p>Izaberite logo:</p>
        
            <input type="file" name="logoFile" id="logoFile" accept="image/*"  @change="handleFileUpload"  title=" GAS"/>

            <p>
                <button>Kreiraj</button>
            </p>
               <p>
                    <img
                    v-bind:src="logoFile"
                    alt="LOGO"
                    height="200px"
                    width="200px"
                    />
                </p>
          
            <p>{{allEntered}}</p>
        </div>
         
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

        axios.get("rest/manager/availale", yourConfig).then((response) => {
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
        handleFileUpload(e) {
            const file = e.target.files[0];
            this.logoFile = URL.createObjectURL(file);
        },
        getImgUrl(slika) {
            if (slika === null) return "Images/Logo.png";
            return "FacilityLogo/" + slika;
        },
    },
});
