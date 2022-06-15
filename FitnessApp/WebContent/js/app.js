const Product = { template: '<edit-product></edit-product>' }
const Products = { template: '<products></products>' }

const router = new VueRouter({
	mode: 'hash',
	  routes: [
		{ path: '/', name: 'home', component: Products},
	    { path: '/products/:id', component: Product}
	  ]
});

var app = new Vue({
	router,
	el: '#products'
});