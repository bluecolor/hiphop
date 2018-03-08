import api from '../../api/settings'
import _ from 'lodash'

const LOAD = 'LOAD'
const SAVE = 'SAVE'

const state = {
  all: []
}

const getters = {
  settings: state => state.all,
  mail: state => {
    const name = 'MAIL'
    const ms = _.find(state.settings, s => s.name.toUpperCase === name)
    if (ms) {
      return ms.value
    }
  }
}

// actions
const actions = {
  findAll ({commit, dispatch}) {
    return api.findAll().then(response => {
      commit(LOAD, response.data)
    },
    error => {
      console.log(error.response.data.message)
      dispatch('notifications/snackError', 'Failed to get settings', {root: true})
    })
  },
  save ({commit, dispatch}, payload) {
    return api.save(payload).then(response => {
      commit(SAVE, response.data)
      dispatch('notifications/snackSuccess', 'Settings saved', {root: true})
    },
    error => {
      console.log(error.response.data.message)
      dispatch('notifications/snackError', 'Failed to save settings', {root: true})
    })
  }
}

// mutations
const mutations = {
  [LOAD] (state, records) {
    state.all = records.map(s => {
      s.value = JSON.parse(s.value)
      return s
    })
  },
  [SAVE] (state, payload) {
    const id = payload.id
    let i = _.findIndex(state.all, {id})
    state.all.splice(i, 1)
    payload.value = JSON.parse(payload.value)
    state.all.push(payload)
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
