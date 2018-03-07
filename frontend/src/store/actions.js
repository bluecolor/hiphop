
export default {
  init () {
    this.dispatch('connections/findAll')
    this.dispatch('users/findAll')
    this.dispatch('users/findMe')
    this.dispatch('app/findVersion')
    this.dispatch('labels/findAll')
  }
}
