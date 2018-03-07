import axios from 'axios'
import { API_BASE } from './constants'

export default {

  findAll () {
    return axios.get(`${API_BASE}/settings`)
  },
  save (payload) {
    return axios.post(`${API_BASE}/settings`, payload)
  }

}
