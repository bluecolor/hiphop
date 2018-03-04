<template lang="pug">
div(
  style="max-width: 700px; margin: auto; margin-top:20px;"
  class="lighten-3"
)
  v-card
    v-card-text
      v-form
        v-text-field(label='Name', v-model='label.name', :rules="[rules.required]")
        swatches(v-model="label.color")
          v-text-field(label='Color' slot="trigger" :value="label.color" readonly :rules="[rules.required]")
        v-text-field(label='Description' v-model='label.description' :rules="[rules.required]" counter)
        v-layout(row='')
          v-btn(@click='onClose') close
          v-spacer
          v-btn(@click='onSave' :disabled="!isValid") save

</template>

<script>
import {mapActions} from 'vuex'
import Swatches from 'vue-swatches'
import 'vue-swatches/dist/vue-swatches.min.css'

export default {
  data () {
    return {
      rules: {
        required: (value) => !!value || ''
      },
      label: {
        name: undefined,
        color: undefined,
        description: undefined
      }
    }
  },
  computed: {
    isValid () {
      return (
        this.label.name !== undefined &&
        this.label.color !== undefined &&
        this.label.description !== undefined
      )
    }
  },
  methods: {
    ...mapActions('labels', [
      'create', 'update'
    ]),
    onClose () {
      window.history.back()
    },
    onSave () {
      if (this.label.id) {
        this.update(this.label)
      } else {
        this.create(this.label)
      }
    }
  },
  components: {
    Swatches
  }
}
</script>
