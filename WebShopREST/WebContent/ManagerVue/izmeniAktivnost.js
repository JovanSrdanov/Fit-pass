Vue.component("izmeniAktivnost", {
    data: function () {
        return {
            customers: {},
            trainers: {},
        };
    },
    template: `
      <div>
        <h1>Izmeni aktivnost</h1>

            

               
        </div>      
  `,
    mounted() {
        this.facId = this.$route.params.id;
        this.activityId = this.$route.params.activityId;

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
    },
    methods: {},
});
