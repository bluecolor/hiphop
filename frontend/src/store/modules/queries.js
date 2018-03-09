
import api from '../../api/queries'
import moment from 'moment'
import _ from 'lodash'

const LOAD = 'LOAD'
const QUERY_RESULT = 'QUERY'
const ADD_LOG = 'ADD_LOG'

const state = {
  queries: [],
  results: [],
  logs: []
}

setInterval(() => {
  state.logs = _.map(state.logs, log => {
    log.elapsed = moment(log.time).fromNow()
    return log
  })
}, 60 * 1000)

const getters = {
  results: state => state.results,
  logs: state => state.logs,
  queries: state => state.queries
}

// actions
const actions = {
  query ({commit, dispatch}, payload) {
    api.query(payload).then(response => {
      //
    })
  },
  findAll ({commit, dispatch}) {
    api.findAll().then(response => {
      commit(LOAD, response.data)
    },
    error => {
      console.log(error.response.data.message)
      dispatch('notifications/snackError', 'Failed to load your queries', {root: true})
    })
  },
  log ({commit}, payload) {
    commit(ADD_LOG, payload)
  },
  logInfo ({commit}, message) {
    const l = {
      type: 'INFO',
      iconName: 'info',
      iconColor: 'blue darken-1',
      message
    }
    commit(ADD_LOG, l)
  },
  logError ({commit}, message) {
    const l = {
      type: 'ERROR',
      iconName: 'error',
      iconColor: 'red darken-1',
      message
    }
    commit(ADD_LOG, l)
  },
  logSuccess ({commit}, message) {
    const l = {
      type: 'SUCCESS',
      iconName: 'done',
      iconColor: 'green darken-1',
      message
    }
    commit(ADD_LOG, l)
  }
}

// mutations
const mutations = {
  [QUERY_RESULT] (state, results) {
    state.results = results
  },
  [ADD_LOG] (state, payload) {
    payload.time = new Date()
    state.logs.splice(0, 0, payload)
  },
  [LOAD] (state, data) {
    state.queries = data
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
