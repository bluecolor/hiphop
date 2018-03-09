import api from '../../api/scripts'
import _ from 'lodash'

const LOAD = 'LOAD'
const CREATE = 'CREATE'
const REMOVE = 'REMOVE'

const state = {
  all: []
}

const getters = {
  scripts: state => state.all
}

// actions
const actions = {
  findMyScripts ({commit, dispatch}) {
    return api.findMyScripts().then(response => {
      commit(LOAD, response.data)
    },
    error => {
      console.log(error.response.data.message)
      dispatch('notifications/snackError', 'Failed to get your scripts', {root: true})
    })
  },
  create ({commit, dispatch}, payload) {
    return api.create(payload).then(response => {
      commit(CREATE, response.data)
      dispatch('notifications/snackSuccess', 'Saved to favorite scripts', {root: true})
    },
    error => {
      console.log(error.response.data.message)
      dispatch('notifications/snackError', 'Failed to save script', {root: true})
    })
  },
  remove ({commit, dispatch}, id) {
    return api.remove(id).then(response => {
      commit(REMOVE, response.data.id)
      dispatch('notifications/snackSuccess', 'Removed script', {root: true})
    },
    error => {
      console.log(error.response.data.message)
      dispatch('notifications/snackError', 'Failed to remove script', {root: true})
    })
  }
}

// mutations
const mutations = {
  [LOAD] (state, data) {
    state.all = data
  },
  [CREATE] (state, data) {
    state.all.push(data)
  },
  [REMOVE] (state, id) {
    let i = _.findIndex(state.all, {id})
    state.all.splice(i, 1)
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
