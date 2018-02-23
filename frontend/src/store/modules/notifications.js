
import _ from 'lodash'

const SNACK = 'SNACK'

const state = {
  snack: {
    text: 'Info',
    display: false,
    timeout: 6000,
    color: ''
  }
}

const getters = {
  snack: state => state.snack
}

const actions = {
  snack ({commit}, payload) {
    commit(SNACK, payload)
  },
  snackSuccess ({commit}, text) {
    commit(SNACK, {text, color: 'success'})
  },
  snackError ({commit}, text) {
    commit(SNACK, {text, color: 'error'})
  },
  snackInfo ({commit}, text) {
    commit(SNACK, {text, color: 'info'})
  },
  snackWarning ({commit}, text) {
    commit(SNACK, {text, color: 'warning'})
  }
}

const mutations = {
  [SNACK] (state, payload) {
    const snack = {
      text: 'Info',
      display: true,
      timeout: 6000,
      color: ''
    }
    state.snack = _.extend(snack, payload)
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
