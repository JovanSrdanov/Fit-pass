Vue.component("pocetna", {
    data: function () {
        return {
            SportFacility: {},
        };
    },
    template: `
   <div class="centriraj">
           

        
                <h1>Sportski objekti! </h1>
 <div class="prostorZatabelu">
            <table>
                <tbody>
                    <tr v-for="p in SportFacility" >
                        <td>
                            <img
                              
                                v-bind:src="getImgUrl(p.facility.logo)"
                                alt="LOGO"
                                height="150"
                                width="350"
                            />
                        </td>
                        <td>
                            <ul>
                                <li>{{p.facility.name}}</li>
                                <li>Tip: {{p.facility.facilityType}}</li>
                                <li>{{p.location.address}}</li>
                                <li>Prosečna ocena:{{p.facility.rating}}</li>
                                <li>{{p.facility.workStart.hour}}:{{p.facility.workStart.minute}}</li>
                            </ul>
                        </td>
                    </tr>

                </tbody>
            </table>
        </div>
                
            
        </div>`,

    mounted() {
        axios.get("rest/facilitys").then((response) => {
            this.SportFacility = response.data;
        });
    },
    methods: {
        getImgUrl(slika) {
            return "FacilityLogo/" + slika;
        },
    },
});
