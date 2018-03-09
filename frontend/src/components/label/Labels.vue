<template lang="pug">
.labels
  div(
    id="e3"
    style="max-width: 600px; margin: auto;"
    class="lighten-3"
  )
    v-container(fluid='', style='min-height: 0;', grid-list-lg='')
      v-layout(row='', wrap='')
        v-flex(xs12='' v-for='(label, i) in labels' v-bind:key="label.id")
          v-card
            v-card-title(:style="'background-color:'+ label.color" class="white--text")
              .headline {{label.name}}
            v-card-text
              div
                span
                  | {{label.description}}
            v-card-actions
              v-btn(route :to="'/label/' + label.id") Details
              v-spacer
              v-btn(color="error", @click="onRemove(label.id)") Delete
    v-btn(fixed='', dark='', fab='', bottom='', right='', color='pink' route, to="/label")
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
    ...mapGetters('labels', [
      'labels'
    ])
  },
  methods: {
    ...mapActions('labels', [
      'remove'
    ]),
    onRemove (id) {
    }
  }
}
</script>


<style scoped>
  .labels {
    /* height: 100%; */
    display: flex;
    overflow-y: auto;
    width: 100%;
  }
</style>
