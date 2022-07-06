Vue.component("pregledObjekta", {
    data: function () {
        return {
            facilityID: null,
            currentFacility: {
                facility: { name: "", logo: "" },
                location: { address: "" },
            },
            SportFacilityList: {},
        };
    },
    template: `
  <div>
            <h1>{{currentFacility.facility.name}} </h1>
            
              <div class="infoAndTranings">
               
                        <table>               
                            <tr><td>{{currentFacility.facility.facilityType}}</td></tr>
                            <tr><td>
                            Menadžer: MENJAJMENJAJ
                           </td> </tr>  
                            <tr><td>{{currentFacility.location.address}}</td></tr>
                            <tr><td>
                                Prosečna ocena: {{currentFacility.facility.rating}}/5
                           </td> </tr>
                            <tr><td>
                                Radno vreme: {{currentFacility.facility.workStart}} -
                                {{currentFacility.facility.workEnd}}</td>
                            </tr> 
                                <tr><td>
                                Objekat  {{convertStatus(currentFacility.facility.status)}}
                               </td>
                            </tr>     
                        </table>     
                        
                       
                    <div class="FacilityActivityTable">
                
                    </div>
                    
              
             </div>

        

            <div class="LogoLocation"> 
                  <img
                    v-bind:src="getImgUrl(currentFacility.facility.logo)"
                    alt="LOGO"
                    height="500px"
                    width="500px"
                />
                <div class="mapShow" id="mapShow"></div>
            </div>

          
            


           
        </div>    
                `,

    mounted() {
        this.facilityID = this.$route.params.id;
        axios.get("rest/facilitys/" + this.facilityID).then((response) => {
            this.currentFacility = response.data;

            var Lon = this.currentFacility.location.longitude;
            var Lat = this.currentFacility.location.latitude;

            var CurrLoaction = new ol.Feature({
                geometry: new ol.geom.Point(ol.proj.fromLonLat([Lon, Lat])),
            });
            CurrLoaction.setStyle(
                new ol.style.Style({
                    image: new ol.style.Icon(
                        /** @type {olx.style.IconOptions} */ ({
                            color: "red",

                            src: "https://openlayers.org/en/v4.6.5/examples/data/dot.png",
                        })
                    ),
                })
            );
            var vectorSource = new ol.source.Vector({
                features: [CurrLoaction],
            });

            var vectorLayer = new ol.layer.Vector({
                source: vectorSource,
            });

            var map = new ol.Map({
                target: "mapShow",
                layers: [
                    new ol.layer.Tile({
                        source: new ol.source.OSM(),
                    }),
                    vectorLayer,
                ],
                view: new ol.View({
                    center: ol.proj.fromLonLat([Lon, Lat]),
                    zoom: 13,
                }),
            });

            map.on("click", function (evt) {
                console.log(
                    ol.proj.transform(evt.coordinate, "EPSG:3857", "EPSG:4326")
                );
                var self = this;

                var latLong = ol.proj.transform(
                    evt.coordinate,
                    "EPSG:3857",
                    "EPSG:4326"
                );
                var latClick = latLong[1];
                var longClick = latLong[0];

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

                if (self.dinamicPinLayer !== undefined) {
                    console.log("moove");
                    self.removeLayer(self.dinamicPinLayer);

                    var vectorSource2 = new ol.source.Vector({
                        features: [ClickedLocation],
                    });
                    self.dinamicPinLayer = new ol.layer.Vector({
                        source: vectorSource2,
                    });
                    self.addLayer(self.dinamicPinLayer);
                    //or create another pin
                } else {
                    var vectorSource2 = new ol.source.Vector({
                        features: [ClickedLocation],
                    });
                    self.dinamicPinLayer = new ol.layer.Vector({
                        source: vectorSource2,
                    });
                    self.addLayer(self.dinamicPinLayer);
                }

                // Coords of click is evt.coordinate
                console.log("evt.coordinate: " + evt.coordinate);
                // You must transform the coordinates because evt.coordinate
                // is by default Web Mercator (EPSG:3857)
                // and not "usual coords" (EPSG:4326)
                const coords_click = ol.proj.transform(
                    evt.coordinate,
                    "EPSG:3857",
                    "EPSG:4326"
                );
                console.log("Mouse Click coordinates: " + coords_click);

                // MOUSE CLICK: Longitude
                const lon = coords_click[0];
                // MOUSE CLICK: Latitude
                const lat = coords_click[1];

                // DATA to put in NOMINATIM URL to find address of mouse click location
                const data_for_url = {
                    lon: lon,
                    lat: lat,
                    format: "json",
                    limit: 1,
                };

                // ENCODED DATA for URL
                const encoded_data = Object.keys(data_for_url)
                    .map(function (k) {
                        return (
                            encodeURIComponent(k) +
                            "=" +
                            encodeURIComponent(data_for_url[k])
                        );
                    })
                    .join("&");

                // FULL URL for searching address of mouse click
                const url_nominatim =
                    "https://nominatim.openstreetmap.org/reverse?" +
                    encoded_data;
                console.log("URL Request NOMINATIM-Reverse: " + url_nominatim);

                // GET URL REQUEST for ADDRESS
                axios.get(url_nominatim).then((response) => {
                    // JSON Data of the response to the request Nominatim
                    const data_json = response.data;

                    // Longitude and latitude
                    const res_lon = data_json.lon;
                    const res_lat = data_json.lat;

                    // All the informations of the address are here
                    const res_address = data_json.address;

                    // Details depends on the location, country and places
                    // For example: in the desert, road or pedestrian is
                    // probably set to undefined because of none...

                    const address_display_name = data_json.display_name;
                    const address_country = res_address.country;
                    const address_country_code = res_address.country_code;
                    const address_postcode = res_address.postcode;
                    const address_state = res_address.state;
                    const address_town = res_address.town;
                    const address_city = res_address.city;
                    const address_city_district = res_address.city_district;
                    const address_suburb = res_address.suburb;
                    const address_neighbourhood = res_address.neighbourhood;
                    const address_footway = res_address.footway;
                    const address_house_number = res_address.house_number;
                    const address_pedestrian = res_address.pedestrian;
                    const address_road = res_address.road;

                    console.log("Longitude    : " + res_lon);
                    console.log("Longitude    : " + res_lat);
                    console.log("Name         : " + address_display_name);
                    console.log("Country      : " + address_country);
                    console.log("Count. Code  : " + address_country_code);
                    console.log("Postcode     : " + address_postcode);
                    console.log("State        : " + address_state);
                    console.log("Town         : " + address_town);
                    console.log("City         : " + address_city);
                    console.log("City District: " + address_city_district);
                    console.log("Suburb       : " + address_suburb);
                    console.log("Neighbourhood: " + address_neighbourhood);
                    console.log("Road         : " + address_road);
                    console.log("Footway      : " + address_footway);
                    console.log("Pedestrian   : " + address_pedestrian);
                    console.log("House Number : " + address_house_number);
                });
            });
        });
    },
    methods: {
        convertStatus(status) {
            if (status) return "radi";
            else return "ne radi";
        },
        getImgUrl(slika) {
            if (slika == "") return null;
            return "FacilityLogo/" + slika;
        },
    },
});
