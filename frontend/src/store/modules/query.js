
import api from '../../api/query'
// import _ from 'lodash'

const QUERY_RESULT = 'QUERY'

const state = {
  results: []
}

const getters = {
  results: state => state.results
}

// actions
const actions = {
  query ({commit, dispatch}, payload) {
    api.query(payload).then(response => {
      //
    })
  }
}

// mutations
const mutations = {
  [QUERY_RESULT] (state, results) {
    state.results = results
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
