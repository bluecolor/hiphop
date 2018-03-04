import axios from 'axios'
import { API_BASE } from './constants'

export default {

  findAll () {
    return axios.get(`${API_BASE}/labels`)
  },

  create (label) {
    return axios.post(`${API_BASE}/labels`, label)
  },

  update (id, label) {
    return axios.put(`${API_BASE}/labels/${id}`, label)
  },

  remove (id) {
    return axios.delete(`${API_BASE}/labels/${id}`)
  }

}
