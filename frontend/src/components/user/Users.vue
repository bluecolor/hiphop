<template lang="pug">
div(
  id="e3"
  style="max-width: 600px; margin: auto; width:100%; margin-top:10px; overflow-y:auto;"
  class="lighten-3"
)
  v-container(fluid='', style='min-height: 0;', grid-list-lg='')
    v-layout(row='', wrap='')
      v-flex(xs12='' v-for='(u, i) in users',)
        v-card
          v-card-title(:class="cardTitleClass(u)")
            .headline {{u.name}}
          v-card-text
            div
              span
                | {{u.email}}
          v-card-actions
            v-btn(route :to="'/user/' + u.id") Details
            v-spacer
            v-btn(v-show="!u.system" color="error", @click="onRemove(u.id)") Delete
  v-btn(fixed='', dark='', fab='', bottom='', right='', color='pink' route, to="/user")
    v-icon add
</template>

<script>

import {mapGetters, mapActions} from 'vuex'

export default {
  data () {
    return {
    }
  },
  computed: {
    ...mapGetters('users', [
      'users'
    ])
  },
  methods: {
    ...mapActions('users', [
      'remove'
    ]),
    ...mapActions('notifications', [
      'snack', 'snackSuccess', 'snackError', 'snackInfo', 'snackWarning'
    ]),
    cardTitleClass (u) {
      if (u.system) {
        return 'red accent-1 white--text'
      }
      return 'blue white--text'
    },
    onRemove (id) {
      this.remove(id).then(() => {
        this.snackSuccess('Deleted user')
      }).catch(() => {
        this.snackError('Failed to delete user')
      })
    }
  },
  mounted () {}

}
</script>

<style>
  .hidden {
    display: none;
  }
  .url {
    font-size: 14px !important;
  }
</style>
