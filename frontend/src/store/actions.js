
export default {
  init () {
    this.dispatch('connections/findAll')
    this.dispatch('users/findAll')
    this.dispatch('app/findVersion')
  }
}
