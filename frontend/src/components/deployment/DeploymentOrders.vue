<template lang="pug">
div(
  id="e3"
  style="max-width: 900px; margin: auto;"
  class="lighten-3"
)
  div(
    id="e3"
    style="margin: auto;"
    class="lighten-3"
  )
    v-container(fluid='', style='min-height: 0;', grid-list-lg='')
      v-layout(row="")
        v-flex
          v-card
            v-toolbar(color="light-blue")
              v-toolbar-title(class="white--text") Orders
              v-spacer
              v-select.elevation-0(
                clearable="true"
                :items="connections"
                v-model="filters.connection"
                label="Connection"
                autocomplete=""
                item-text="name"
                item-value="id"
                solo
              )
            v-list(two-line="" subheader="")
              v-list-tile(ripple="" avatar="" v-for="item in items" :key="item.title" @click="")
                v-list-tile-avatar
                  v-icon(:class="[item.iconClass]") {{ item.icon }}
                v-list-tile-content
                  v-list-tile-title {{ item.title }}
                  v-list-tile-sub-title {{ item.subtitle }}
                v-list-tile-action
                  v-btn(icon="" ripple="" :to="`orders/${item.id}/steps`")
                    v-icon(color="grey lighten-1") more_horiz
            v-card-actions
              v-btn(icon='' @click='onBack' style="margin-left:15px")
                v-icon(color="grey darken-1", large='') arrow_back
              v-spacer
              v-pagination(:length="6" v-model="page")

  v-btn(fixed='', dark='', fab='', bottom='', right='', color='pink' route, to="/deployment")
    v-icon add
</template>

<script>

import {mapGetters} from 'vuex'

export default {
  data () {
    return {
      filters: {
        connection: undefined
      },
      page: 1
    }
  },
  computed: {
    ...mapGetters('deployments', [
      'deployments'
    ]),
    ...mapGetters('connections', [
      'connections'
    ]),
    items () {
      return [
        { id: 1, icon: 'flash_on', iconClass: 'blue white--text', title: 'Vacation itinerary', subtitle: 'Jan 20, 2014' },
        { id: 2, icon: 'hourglass_empty', iconClass: 'amber white--text', title: 'Kitchen remodel', subtitle: 'Jan 10, 2014' },
        { id: 3, icon: 'done', iconClass: 'green white--text', title: 'Kithen remodel', subtitle: 'Jan 10, 2014' },
        { id: 4, icon: 'priority_high', iconClass: 'red white--text', title: 'Kitche remodel', subtitle: 'Jan 10, 2014' },
        { id: 5, icon: 'done', iconClass: 'green white--text', title: 'Kitchen remoel', subtitle: 'Jan 10, 2014' }
      ]
    }
  },
  methods: {
    onItemClick () {
    },
    onBack () {
      window.history.back()
    }
  },
  mounted () {}

}
</script>

<style>
  .search-bg-color {
    background-color: #03a9f43d
  }
</style>
