import Vue from 'vue'
import Vuex from 'vuex'
import state from './state'
import actions from './actions'
import mutations from './mutations'

import users from './modules/users'
import connections from './modules/connections'
import app from './modules/app'
import notifications from './modules/notifications'

Vue.use(Vuex)

export default new Vuex.Store({
  state,
  actions,
  mutations,
  getters: {
    loading: state => state.loading
  },
  modules: {
    users,
    connections,
    app,
    notifications
  }
})
