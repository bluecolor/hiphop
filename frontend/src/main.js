// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuex from 'vuex'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.css'
import Vuelidate from 'vuelidate'
import store from './store'

import App from './App'
import router from './router'

Vue.use(Vuex)
Vue.use(Vuetify)
Vue.use(Vuelidate)
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store: store,
  created () {
    this.$store.dispatch('init')
  },
  template: '<App/>',
  components: { App }
})
