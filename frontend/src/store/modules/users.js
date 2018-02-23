
import api from '../../api/users'
import _ from 'lodash'

const LOAD = 'LOAD'
const CREATE = 'CREATE'
const UPDATE = 'UPDATE'
const REMOVE = 'REMOVE'

const state = {
  all: []
}

const getters = {
  users: state => state.all
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
  }
}

// mutations
const mutations = {
  [LOAD] (state, records) {
    state.all = records
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
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
