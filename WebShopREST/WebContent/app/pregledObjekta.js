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
                                {{currentFacility.facility.workEnd}}</td>
                            </tr> 
                                <tr><td>
                                Objekat (radi/ne radi)
                               </td>
                            </tr>     
                        </table>     
                    </div>    
                       
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
