Vue.component("ostaviKomentar", {
    data: function () {
        return {
            facilityID: null,
            ratingSend: 3,
            commentText: "",
            fillCommentText: "",
            Customer: null,
        };
    },
    template: `
     <div>
            <h1>Ostavi komentar i oceni objekat</h1>
            <div class="rating">
                <input  v-model="ratingSend" type="radio" name="rating" value="5" id="5"><label for="5">☆</label>
                <input  v-model="ratingSend" type="radio" name="rating" value="4" id="4"><label for="4">☆</label>
                <input v-model="ratingSend" type="radio" name="rating" value="3" id="3"><label for="3">☆</label>
                <input  v-model="ratingSend" type="radio" name="rating" value="2" id="2"><label for="2">☆</label>
                <input  v-model="ratingSend" type="radio" name="rating" value="1" id="1"><label for="1">☆</label>
            </div>

            <textarea
            v-model="commentText"
                placeholder="Vaš komentar..."
                name="Comm"
                id="Comm"
                cols="70"
                rows="10"
                style="overflow: auto; resize: none"
            ></textarea>



            <p><button v-on:click="SendComment">Pošalji komentar</button></p>
            <p class="white">{{fillCommentText}}</p>
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
        SendComment: function () {
            this.fillCommentText = "";

            if (this.commentText === "") {
                this.fillCommentText = "Molim vas unesite komentar";
                return;
            }
            yourConfig = {
                headers: {
                    Authorization: localStorage.getItem("token"),
                    "Content-Type": "application/json",
                },
            };

            axios
                .post(
                    "rest/comment/new",
                    {
                        customerId: this.Customer.id,
                        facilityId: this.facilityID,
                        rating: this.ratingSend,
                        commentText: this.commentText,
                    },
                    yourConfig
                )
                .then((result) => {
                    alert("Uspešno objavljen komentar");
                    window.location.href =
                        "#/pregledObjekta/" + this.facilityID;
                })
                .catch((err) => {});
        },
    },
});
