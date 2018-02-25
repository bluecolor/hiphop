<template lang="pug">

div(
  style="max-width: 700px; margin: auto;"
  class="lighten-3"
)
  v-form
    v-text-field(label='Name', v-model='connection.name', required='')
    v-text-field(label='Jdbc Url', v-model='connection.url',  required='')
    v-text-field(label='Username', v-model='connection.username',  required='')
    v-text-field(label='Password', v-model='connection.password',  required='')
    v-switch(label="Disabled" color="red darken-3" v-model="connection.disabled")
    v-layout(row='')
      v-btn(@click='onClose') close
      v-spacer
      v-btn(@click='onTest') test
      v-btn(@click='onSave') save

</template>

<script>

import _ from 'lodash'
import { mapActions, mapGetters } from 'vuex'

export default {

  name: 'Connection',
  props: ['id'],
  data () {
    return {
      connection: {
        name: undefined,
        disabled: false,
        url: '',
        username: '',
        password: '',
        status: 0,
        lastChecked: undefined
      }
    }
  },
  methods: {
    ...mapActions('connections', {
      doCreate: 'create',
      doUpdate: 'update',
      doTest: 'test'
    }),
    ...mapActions('notifications', [
      'snack', 'snackSuccess', 'snackError'
    ]),
    onSave () {
      if (this.connection.id === undefined) {
        this.create()
      } else {
        this.update()
      }
    },
    onClose () {
      window.history.back()
    },
    onTest () {
      this.doTest(this.connection).then(() => {
        this.snackSuccess('Success. Connection works.')
      }).catch(() => {
        this.snackError('Error! Connection not working')
      })
    },
    create () {
      this.doCreate(this.connection).then(() => {
        this.snackSuccess('Created connection')
        window.history.back()
      }).catch(() => {
        this.snackError('Failed to create connection')
      })
    },
    update () {
      this.doUpdate(this.connection).then(() => {
        this.snackSuccess('Updated connection')
        window.history.back()
      }).catch(() => {
        this.snack('Failed to update connection')
      })
    },
    init () {
      if (!this.id) { return }
      const id = parseInt(this.id)
      this.connection = _.chain(this.connections).find({id}).cloneDeep().value()
      if (this.$route.query.clone === 'true') {
        this.connection.id = undefined
      }
    }
  },
  computed: {
    ...mapGetters('connections', [
      'connections'
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
