<template lang="pug">
div(
  style="max-width: 700px; margin: auto;"
  class="lighten-3"
)
  v-form
    v-text-field(label='Name', v-model='user.name', required='')
    v-text-field(label='Username', v-model='user.username',  required='')
    v-text-field(label='Email', v-model='user.email',  required='')
    v-layout(row='')
      v-btn(@click='onClose') close
      v-spacer
      v-btn(@click='onSave') save

</template>

<script>

import _ from 'lodash'
import { mapActions, mapGetters } from 'vuex'

export default {

  name: 'User',
  props: ['id'],
  data () {
    return {
      user: {
        id: undefined,
        name: undefined,
        username: undefined,
        email: undefined
      }
    }
  },
  methods: {
    ...mapActions('users', {
      doCreate: 'create',
      doUpdate: 'update'
    }),
    ...mapActions('notifications', [
      'snack', 'snackSuccess', 'snackError'
    ]),
    onSave () {
      if (this.user.id === undefined) {
        this.create()
      } else {
        this.update()
      }
    },
    onClose () {
      window.history.back()
    },
    create () {
      this.doCreate(this.user).then(() => {
        this.snackSuccess('Created user')
        window.history.back()
      }).catch(() => {
        this.snackError('Failed to create user')
      })
    },
    update () {
      this.doUpdate(this.user).then(() => {
        this.snackSuccess('Updated user')
        window.history.back()
      }).catch(() => {
        this.snack('Failed to update user')
      })
    },
    init () {
      if (!this.id) { return }
      const id = parseInt(this.id)
      this.user = _.chain(this.users).find({id}).cloneDeep().value()
    }
  },
  computed: {
    ...mapGetters('users', [
      'users'
    ])
  },
  mounted () {
    this.init()
  }

}
</script>

<style>
.content {
  margin: 10px 50px !important;
}
</style>
