
import api from '../../api/deployments'
import _ from 'lodash'

const LOAD = 'LOAD'
const CREATE = 'CREATE'
const REMOVE = 'REMOVE'

const state = {
  all: []
}

const getters = {
  deployments: state => state.all
}

// actions
const actions = {
  findAll ({commit}) {
    return api.findAll().then(response => {
      commit(LOAD, response.data)
    })
  },
  request ({commit}, payload) {
    console.log(payload)
    return new Promise((resolve, reject) => {
      return api.request(payload).then(response => {
        commit(CREATE, response.data)
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
    state.all = _.orderBy(records, ['name'], ['asc'])
  },
  [CREATE] (state, record) {
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
