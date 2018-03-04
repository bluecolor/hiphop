<template lang="pug">

div(
  style="max-width: 700px; margin: auto; margin-top:20px;"
  class="lighten-3"
)
  v-card
    v-card-text
      v-form
        v-text-field(label='Name', v-model='connection.name', :rules="[rules.required]")
        v-text-field(label='Jdbc Url', v-model='connection.url', :rules="[rules.required]")
        v-text-field(label='Username', v-model='connection.username',  :rules="[rules.required]")
        v-text-field(label='Password', v-model='connection.password',  :rules="[rules.required]")
        v-select(label="Labels" :items="labelMenu" v-model="e11" item-text="name" item-value="id" multiple chips max-height="auto" autocomplete)
          template(slot="selection" slot-scope="data")
            v-chip.chip--select-multi(
              text-color="white"
              :style="`border-color:${data.item.color};background-color:${data.item.color};`"
              close
              @input="data.parent.selectItem(data.item)"
              :selected="data.selected"
              :key="JSON.stringify(data.item)"
            )
              | {{ data.item.name }}
          template(slot="item" slot-scope="data")
            template(v-if="typeof data.item !== 'object'")
              v-list-tile-content(v-text="data.item")
            template(v-else="")
              v-icon(v-if="!data.item.labels" left :style="'margin-right:10px; color:' + data.item.color") label
              v-list-tile-content
                v-list-tile-title(v-html="data.item.name" @click="onLabelClick(data.item.labels)")
        v-switch(label="Disabled" color="red darken-3" v-model="connection.disabled")
        v-layout(row='')
          v-btn(@click='onClose') close
          v-spacer
          v-btn(@click='onTest' :disabled="!isValid") test
          v-btn(@click='onSave' :disabled="!isValid") save

</template>

<script>

import _ from 'lodash'
import { mapActions, mapGetters } from 'vuex'

export default {

  name: 'Connection',
  props: ['id'],
  data () {
    return {
      e11: [],
      rules: {
        required: (value) => !!value || ''
      },
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
    onLabelClick (labels) {
      if (!labels) {
        return
      }
      this.$router.push('/labels')
    },
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
    ]),
    ...mapGetters('labels', [
      'labels'
    ]),
    isValid () {
      return (
        this.connection.name &&
        this.connection.url &&
        this.connection.username &&
        this.connection.password
      )
    },
    labelMenu () {
      let labels = _.cloneDeep(this.labels)
      labels.push({divider: true})
      labels.push({divider: true})
      labels.push({name: 'Manage labels', labels: true})
      return labels
    }
  },
  mounted () {
    this.init()
  }

}
</script>

<style scoped>
.content {
  margin: 10px 50px !important;
}
</style>
