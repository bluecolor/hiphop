
import api from '../../api/connections'
import _ from 'lodash'

const LOAD = 'LOAD'
const SAVE = 'SAVE'
const CREATE = 'CREATE'
const UPDATE = 'UPDATE'
const REMOVE = 'REMOVE'

const state = {
  all: []
}

const getters = {
  connections: state => state.all,
  connection: state => id => _.find(state.all, {id})
}

// actions
const actions = {
  findAll ({ commit }) {
    return api.findAll().then(response => {
      commit(LOAD, response.data)
    })
  },
  save ({ commit }, payload) {
    return new Promise((resolve, reject) => {
      api.save(payload).then(response => {
        commit(SAVE, response.data)
        resolve(response.data)
      },
      error => {
        console.log(error.response.data.message)
        reject(error.response.data.message)
      })
    })
  },
  create ({commit}, payload) {
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
      return api.update(payload).then((response) => {
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
        commit(REMOVE, response.data.id)
        resolve(response)
      },
      error => {
        console.log(error.response.data.message)
        reject(error.response.data.message)
      })
    })
  },
  test ({ commit }, payload) {
    let test = 'test'
    if (!_.isObject(payload)) {
      test = 'testById'
    }
    return new Promise((resolve, reject) => {
      return api[test](payload).then(response => {
        resolve(response)
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
    state.all = _.orderBy(records, ['name'], ['asc'])
  },
  [SAVE] (state, record) {
    const c = _.find(state.all, {id: record.id})
    if (c) {
      _.remove(state.all, r => r.id === record.id)
    }
    state.all.push(record)
    window.history.back()
  },
  [REMOVE] (state, id) {
    const i = _.findIndex(state.all, {id})
    state.all.splice(i, 1)
  },
  [CREATE] (state, data) {
    state.all.push(data)
  },
  [UPDATE] (state, data) {
    const id = data.id
    const i = _.findIndex(state.all, {id})
    state.all.splice(i, 1)
    state.all.push(data)
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
