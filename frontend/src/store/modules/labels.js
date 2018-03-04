
import api from '../../api/labels'
import _ from 'lodash'

const LOAD = 'LOAD'
const CREATE = 'CREATE'
const UPDATE = 'UPDATE'
const REMOVE = 'REMOVE'

const state = {
  labels: []
}

const getters = {
  labels: state => state.labels
}

// actions
const actions = {
  findAll ({commit}) {
    api.findAll().then(response => {
      commit(LOAD, response.data)
    })
  },
  create ({commit, dispatch}, payload) {
    api.create(payload).then(response => {
      commit(CREATE, response.data)
      dispatch('notifications/snackSuccess', 'Created label', {root: true})
      window.history.back()
    },
    error => {
      console.log(error.response.data.message)
      dispatch('notifications/snackError', 'Failed to create label', {root: true})
    })
  },

  update ({commit, dispatch}, payload) {
    api.update(payload.id, payload).then(response => {
      commit(UPDATE, response.data)
      dispatch('notifications/snackSuccess', 'Updated label', {root: true})
      window.history.back()
    },
    error => {
      console.log(error.response.data.message)
      dispatch('notifications/snackError', 'Failed to update label', {root: true})
    })
  },

  remove ({commit, dispatch}, id) {
    api.remove(id).then(response => {
      commit(REMOVE, id)
      dispatch('notifications/snackSuccess', 'Deleted label', {root: true})
    },
    error => {
      console.log(error.response.data.message)
      dispatch('notifications/snackError', 'Failed to delete label', {root: true})
    })
  }
}

// mutations
const mutations = {
  [LOAD] (state, labels) {
    state.labels = labels
  },
  [UPDATE] (state, label) {
    const id = label.id
    const i = _.findIndex(state.labels, {id})
    state.labels.splice(i, 1)
    state.labels.push(label)
  },
  [CREATE] (state, label) {
    state.labels.push(label)
  },
  [REMOVE] (state, id) {
    const i = _.findIndex(state.labels, {id})
    state.labels.splice(i, 1)
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
