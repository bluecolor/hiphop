import axios from 'axios'
import { API_BASE } from './constants'

export default {
  findLogShipping () {
    return axios.get(`${API_BASE}/monitor/log-shipping`)
  }
}
