import axios from 'axios'
import { API_BASE } from './constants'

export default {

  findMyScripts () {
    return axios.get(`${API_BASE}/scripts`)
  },
  create (payload) {
    return axios.post(`${API_BASE}/scripts`, payload, {headers: {'Content-Type': 'text/plain'}})
  },
  remove (id) {
    return axios.delete(`${API_BASE}/scripts/${id}`)
  }

}
