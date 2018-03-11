
import api from '../../api/queries'
import moment from 'moment'
import _ from 'lodash'

const LOAD = 'LOAD'
const QUERY_RESULT = 'QUERY_RESULT'
const ADD_LOG = 'ADD_LOG'
const CLEAR_RESULT = 'CLEAR_RESULT'
const SET_RUNNING = 'SET_RUNNING'

const state = {
  running: false,
  queries: [],
  result: undefined,
  logs: []
}

setInterval(() => {
  state.logs = _.map(state.logs, log => {
    log.elapsed = moment(log.time).fromNow()
    return log
  })
}, 60 * 1000)

const getters = {
  result: state => state.result,
  logs: state => state.logs,
  queries: state => _.filter(state.queries, q => !q.export),
  exports: state => _.filter(state.queries, q => q.export),
  isRunning: state => state.running
}

// actions
const actions = {
  query ({commit, dispatch}, payload) {
    commit(CLEAR_RESULT)
    commit(SET_RUNNING, true)
    return new Promise((resolve, reject) => {
      return api.query(payload).then(response => {
        commit(QUERY_RESULT, response.data)
        dispatch('notifications/snackSuccess', 'Query finished', {root: true})
        commit(SET_RUNNING, false)
        resolve(response.data)
      },
      error => {
        commit(SET_RUNNING, false)
        console.log(error.response.data.message)
        dispatch('notifications/snackError', 'Failed to get query result', {root: true})
        reject(error.response.data.message)
      })
    })
  },
  download ({commit, dispatch}, id) {
    window.location = `api/v1/queries/export/${id}`
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
  [QUERY_RESULT] (state, payload) {
    const id = payload.query.id
    const query = _.find(state.queries, {id})
    if (!query) {
      state.queries.splice(0, 0, payload.query)
    }
    if (!payload.query.export) {
      state.result = payload
    }
  },
  [ADD_LOG] (state, payload) {
    payload.time = new Date()
    state.logs.splice(0, 0, payload)
  },
  [LOAD] (state, data) {
    state.queries = _.orderBy(data, ['startDate'], ['dsc'])
  },
  [CLEAR_RESULT] (state) {
    state.result = undefined
  },
  [SET_RUNNING] (state, b) {
    state.running = b
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
