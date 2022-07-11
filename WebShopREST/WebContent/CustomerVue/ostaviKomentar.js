Vue.component("ostaviKomentar", {
    data: function () {
        return {
            facilityID: null,
            rating: 1,
            commentText: "",
            fillCommentText: "",
            Customer: null,
        };
    },
    template: `
    <div>
                <h1>Ostavi komentar</h1>
           
    </div>
    `,
    mounted() {
        this.facilityID = this.$route.params.id;
        if (JSON.parse(localStorage.getItem("loggedInUser")) === null) {
            alert("Nemate pristup ovom sadržaju");
            window.location.href = "#/pocetna";
            return;
        }
        if (
            JSON.parse(localStorage.getItem("loggedInUser")).role != "customer"
        ) {
            alert("Nemate pristup ovom sadržaju");
            window.location.href = "#/pocetna";
            return;
        }
        this.Customer = JSON.parse(localStorage.getItem("loggedInUser"));

        yourConfig = {
            headers: {
                Authorization: localStorage.getItem("token"),
            },
        };
    },

    methods: {
        SendComment: function () {},
    },
});
