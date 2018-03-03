
const TOGGLE_QUERY_RIGHT_DRAWER = 'TOGGLE_QUERY_RIGHT_DRAWER'

const state = {
  queryRightDrawer: false
}

const getters = {
  queryRightDrawer: state => state.queryRightDrawer
}

// actions
const actions = {
  toggleQueryRightDrawer ({commit}) {
    commit(TOGGLE_QUERY_RIGHT_DRAWER)
  }
}

// mutations
const mutations = {
  [TOGGLE_QUERY_RIGHT_DRAWER] (state) {
    state.queryRightDrawer = !state.queryRightDrawer
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
