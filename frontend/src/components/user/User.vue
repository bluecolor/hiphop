<template lang="pug">
div(
  style="max-width: 700px; margin: auto;"
  class="lighten-3"
)
  v-form
    v-text-field(label='Name', v-model='user.name', :rules="[rules.required]")
    v-text-field(label='Username', v-model='user.username',  :rules="[rules.required]")
    v-text-field(label='Email', v-model='user.email',  :rules="[rules.required, rules.email]")
    v-layout(row='')
      v-btn(@click='onClose') close
      v-spacer
      v-btn(@click='onSave' :disabled="!isValid") save

</template>

<script>

import _ from 'lodash'
import { mapActions, mapGetters } from 'vuex'

export default {

  name: 'User',
  props: ['id'],
  data () {
    return {
      emailPattern: /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
      rules: {
        required: (value) => !!value || '',
        email: (value) => {
          return this.emailPattern.test(value) || 'Invalid e-mail.'
        }
      },
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
    ]),
    isValid () {
      return (
        this.user.name &&
        this.user.username &&
        this.user.email &&
        this.emailPattern.test(this.user.email)
      )
    }
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
