Vue.component("loadCreateSP", {
    data: function () {
        return {};
    },
    template: `
    <div >
        <h1 class="loadNaslov">Sačekajte da se ažuriraju podaci</h1>
        

        <div class="CenterLoad">
            <div class="semipolar-spinner":style="spinnerStyle">
                <div class="ring"></div>
                <div class="ring"></div>
                <div class="ring"></div>
                <div class="ring"></div>
                <div class="ring"></div>
            </div>
        </div>
        
    </div>      
  `,
    mounted() {
        setTimeout(function () {
            window.location.href = "#/pocetna";
        }, 3000);
    },
});
