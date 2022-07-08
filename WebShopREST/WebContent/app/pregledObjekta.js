Vue.component("pregledObjekta", {
    data: function () {
        return {
            allActivitys: {},
            myFacility: false,
            facilityID: null,
            currentFacility: {
                facility: { name: "", logo: "", workStart: "", workEnd: "" },
                location: { address: "" },
            },
            SportFacilityList: {},
        };
    },
    template: `
  <div>
            <h1>{{currentFacility.facility.name}} </h1>
            
              <div class="infoAndTranings">
                    <div class="objectInfo">
                        <table>               
                            <tr><td>{{currentFacility.facility.facilityType}}</td></tr>
                            <tr><td>
                            Menadžer: {{currentFacility.managerName}}  {{currentFacility.managerSurname}}
                           </td> </tr>  
                            <tr><td>{{currentFacility.location.street}} {{currentFacility.location.streetNumber}}</td></tr>
                              <tr><td>{{currentFacility.location.city}} {{currentFacility.location.postCode}} </td></tr>
                                <tr><td>({{currentFacility.location.longitude}}, {{currentFacility.location.latitude}}) </td></tr>
                            <tr><td>
                                Prosečna ocena: {{currentFacility.facility.rating}}/5
                           </td> </tr>
                            <tr><td>
                                Radno vreme: {{currentFacility.facility.workStart}} -
                                {{currentFacility.facility.workEnd}}   {{checkStatus(currentFacility.facility.workStart,currentFacility.facility.workEnd)}}
                            </td></tr> 
                            <tr v-if="myFacility"><td>  
                                <button v-on:click="ViewCustomersAndTrainers">Treneri i kupci</button> 
                                <button v-on:click="AddNewActivity" >Dodaj novi sadržaj</button>     
                                <button v-on:click="SeeHistoryOfTrainings" >Pregled zakazanih treninga</button>                          
                            </td></tr> 
                        </table>     
                    </div>    
                       
                    <div class="FacilityActivityTable">
                    <table>
                        <tbody>
                            <tr v-for="A in allActivitys">
                                <td>
                                    <img
                                        v-bind:src="getWorkoutImgUrl(A.workout.picture)"
                                        alt="LOGO"
                                        height="200"
                                        width="200"
                                    />
                                </td>
                                <td>
                                    <ul>
                                        <li>{{A.workout.name}}</li>
                                     
                              
                                    </ul>
                                </td>
                            </tr>
                        </tbody> 
                    </table>    
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

        if (JSON.parse(localStorage.getItem("loggedInUser")) !== null) {
            if (
                JSON.parse(localStorage.getItem("loggedInUser")).role ===
                "manager"
            ) {
                if (
                    JSON.parse(localStorage.getItem("loggedInUser"))
                        .facilityId == this.facilityID
                ) {
                    this.myFacility = true;
                }
            }
        }

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
        });

        yourConfig = {
            headers: {
                Authorization: localStorage.getItem("token"),
            },
        };
        axios
            .get("rest/workout/inFacility/" + this.facilityID, yourConfig)
            .then((response) => {
                this.allActivitys = response.data;
            });
    },
    methods: {
        ViewCustomersAndTrainers: function () {
            router.push(`/pregledObjekta/${this.facilityID}/treneriIkupci`);
        },
        AddNewActivity: function () {
            router.push(`/pregledObjekta/${this.facilityID}/dodajAktivnost`);
        },
        SeeHistoryOfTrainings: function () {},
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
                return "(Radi)";
            } else {
                return "(Ne radi)";
            }
        },
        convertStatus(status) {
            if (status) return "radi";
            else return "ne radi";
        },
        getImgUrl(slika) {
            if (slika == "") return null;
            return "FacilityLogo/" + slika;
        },
        getWorkoutImgUrl(slika) {
            if (slika == "") return null;
            return "ActivityPictures/" + slika;
        },
    },
});
