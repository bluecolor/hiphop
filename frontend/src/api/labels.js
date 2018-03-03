import axios from 'axios'
import { API_BASE } from './constants'

export default {

  findAll () {
    return axios.get(`${API_BASE}/app/labels`)
  },

  create (label) {
    return axios.post(`${API_BASE}/app/labels`, label)
  },

  update (id, label) {
    return axios.put(`${API_BASE}/app/labels/${id}`, label)
  },

  remove (id) {
    return axios.delete(`${API_BASE}/app/labels/${id}`)
  }

}
