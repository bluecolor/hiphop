
import api from '../../api/app'
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
  create ({commit}, payload) {
    api.create(payload).then(response => {
      commit(CREATE, response.data)
      this.fireEvent('notifications/snackSuccess', 'Created label')
    },
    error => {
      console.log(error.response.data.message)
      this.fireEvent('notifications/snackError', 'Failed to create label')
    })
  },

  update ({commit}, payload) {
    api.update(payload.id, payload).then(response => {
      commit(UPDATE, response.data)
      this.fireEvent('notifications/snackSuccess', 'Updated label')
    },
    error => {
      console.log(error.response.data.message)
      this.fireEvent('notifications/snackError', 'Failed to update label')
    })
  },

  remove ({commit}, id) {
    api.remove(id).then(response => {
      commit(REMOVE, id)
      this.fireEvent('notifications/snackSuccess', 'Deleted label')
    },
    error => {
      console.log(error.response.data.message)
      this.fireEvent('notifications/snackError', 'Failed to delete label')
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
    const i = _.findIndex(state.all, {id})
    state.all.splice(i, 1)
    state.all.push(label)
  },
  [CREATE] (state, label) {
    state.all.push(label)
  },
  [REMOVE] (state, id) {
    const i = _.findIndex(state.all, {id})
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
