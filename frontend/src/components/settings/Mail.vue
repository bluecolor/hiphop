<template lang="pug">
    v-flex(mt-5)
      v-card.elevation-5
        v-toolbar(color="white")
          v-btn(icon="" @click="onBack")
            v-icon arrow_back
          v-toolbar-title Mail
        v-card-text
          v-form
            v-switch(:label="m.active ? 'Enabled' : 'Disabled'" color="success" v-model="m.active")
            v-flex(layout="row")
              v-text-field(
                label='Host',
                v-model='m.host',
                :rules="[rules.required]"
              )
              v-text-field(
                label='Port',
                v-model='m.port',
                :rules="[rules.required]"
                type="number"
              )
            v-text-field(
              label='Username',
              v-model='m.username',
              :rules="[rules.required]"
            )
            v-text-field(
              label='Password',
              v-model='m.password',
              :rules="[rules.required]"
              hint="At least 8 characters"
              min="8"
              :append-icon="e1 ? 'visibility' : 'visibility_off'"
              :append-icon-cb="() => (e1 = !e1)"
              :type="e1 ? 'password' : 'text'"
              counter
            )
            v-radio-group(
              label="Connection security"
              :column="false",
              v-model="m.connectionSecurity"
            )
              v-radio(
                v-for="n in security"
                :key="n.key"
                :label="n.label"
                :value="n.key"
                color="error"
              )
            v-text-field(
              label='Send from',
              v-model='m.sendFrom'
            )
        v-card-actions
          v-spacer
          v-btn(flat @click="onSave") Save
</template>

<script>
import _ from 'lodash'
import {mapActions, mapGetters} from 'vuex'

export default {
  data () {
    return {
      security: [
        {label: 'SSL', key: 'ssl'},
        {label: 'TLS', key: 'tls'},
        {label: 'None', key: 'none'}
      ],
      m: {
        host: undefined,
        port: 25,
        active: false,
        username: undefined,
        password: undefined,
        sendFrom: 'hiphop@bluecolor.io',
        connectionSecurity: 'none'
      },
      e1: false,
      rules: {
        required: (value) => !!value || ''
      },
      p: {
        current: undefined,
        new: undefined,
        reNew: undefined
      }
    }
  },
  computed: {
    ...mapGetters('settings', [
      'mail'
    ])
  },
  methods: {
    ...mapActions('settings', [
      'save'
    ]),
    onBack () {
      this.$emit('close')
    },
    onSave () {
      const name = 'MAIL'
      const value = JSON.stringify(this.m)
      this.save({name, value})
    }
  },
  mounted () {
    if (this.mail) {
      this.m = _.cloneDeep(this.mail)
    }
  }
}
</script>

