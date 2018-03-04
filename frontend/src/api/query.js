import axios from 'axios'
import { API_BASE } from './constants'

export default {

  query (payload) {
    return axios.post(`${API_BASE}/app/query`, payload)
  }

}
