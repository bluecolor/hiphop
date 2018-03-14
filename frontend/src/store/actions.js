import { setInterval } from 'timers'

export default {
  init () {
    this.dispatch('users/findMe')
    this.dispatch('connections/findAll')
    this.dispatch('users/findAll')
    this.dispatch('app/findVersion')
    this.dispatch('labels/findAll')
    this.dispatch('scripts/findMyScripts')
    this.dispatch('queries/findAll')

    setInterval(() => {
      this.dispatch('queries/findAll')
    }, 10 * 1000)
  }
}
