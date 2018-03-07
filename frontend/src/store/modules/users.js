
import api from '../../api/users'
import _ from 'lodash'

const LOAD = 'LOAD'
const CREATE = 'CREATE'
const UPDATE = 'UPDATE'
const REMOVE = 'REMOVE'
const SET_ME = 'SET_ME'

const state = {
  all: [],
  me: undefined
}

const getters = {
  users: state => state.all,
  me: state => state.me
}

// actions
const actions = {
  findAll ({ commit }) {
    api.findAll().then(response => {
      commit(LOAD, response.data)
    })
  },
  create ({ commit }, payload) {
    return new Promise((resolve, reject) => {
      return api.create(payload).then(response => {
        commit(CREATE, response.data)
        resolve(response.data)
      },
      error => {
        console.log(error.response.data.message)
        reject(error.response.data.message)
      })
    })
  },
  update ({commit}, payload) {
    return new Promise((resolve, reject) => {
      return api.update(payload.id, payload).then(response => {
        commit(UPDATE, response.data)
        resolve(response.data)
      },
      error => {
        console.log(error.response.data.message)
        reject(error.response.data.message)
      })
    })
  },
  remove ({ commit }, id) {
    return new Promise((resolve, reject) => {
      return api.remove(id).then(response => {
        commit(REMOVE, response.data)
        resolve(response.data)
      },
      error => {
        console.log(error.response.data.message)
        reject(error.response.data.message)
      })
    })
  },
  changePassword ({commit, dispatch}, payload) {
    return api.changePassword(payload).then(response => {
      dispatch('notifications/snackSuccess', 'Changed password', {root: true})
      window.history.back()
    },
    error => {
      console.log(error.response.data.message)
      dispatch('notifications/snackError', 'Failed to update password', {root: true})
    })
  },
  setOption ({commit, dispatch}, payload) {
    return api.setOption(payload).then(response => {
      commit(SET_ME, response.data)
    },
    error => {
      console.log(error.response.data.message)
      dispatch('notifications/snackError', 'Failed to save option', {root: true})
    })
  },
  findMe ({commit, dispatch}) {
    return api.findMe().then(response => {
      commit(SET_ME, response.data)
    },
    error => {
      console.log(error.response.data.message)
      dispatch('notifications/snackError', 'Failed to get user info', {root: true})
    })
  }
}

// mutations
const mutations = {
  [LOAD] (state, records) {
    state.all = _.orderBy(records, ['username'], ['asc'])
  },
  [CREATE] (state, record) {
    state.all.push(record)
  },
  [UPDATE] (state, record) {
    const id = record.id
    const i = _.findIndex(state.all, {id})
    state.all.splice(i, 1)
    state.all.push(record)
  },
  [REMOVE] (state, id) {
    const i = _.findIndex(state.all, {id})
    state.all.splice(i, 1)
  },
  [SET_ME] (state, payload) {
    state.me = payload
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
