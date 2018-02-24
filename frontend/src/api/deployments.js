import axios from 'axios'
import { API_BASE } from './constants'

export default {
  findAll () {
    return axios.get(`${API_BASE}/deployments`)
  },
  request (payload) {
    const config = { headers: { 'Content-Type': 'multipart/form-data' } }
    return axios.post(`${API_BASE}/deployments/request`, payload, config)
  }
}
