import api from '../../api/monitor'

const LOAD_LOG_SHIPPING = 'LOAD_LOG_SHIPPING'

const state = {
  logShipping: []
}

const getters = {
  logShipping: state => state.logShipping
}

// actions
const actions = {
  findLogShipping ({commit}) {
    api.findAll().then(response => {
      commit(LOAD_LOG_SHIPPING, response.data)
    })
  }
}

// mutations
const mutations = {
  [LOAD_LOG_SHIPPING] (state, labels) {
    state.labels = labels
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
