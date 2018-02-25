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
            v-toolbar(color="light-blue" dark="")
              v-toolbar-title(class="white--text") Deployments
              v-spacer
              v-text-field.elevation-0(v-on:blur="searchFocus(false)", v-on:focus="searchFocus(true)" :style="searchStyle" light="" solo="" clearable="" prepend-icon="search" placeholder="Type keyword...")
              v-dialog.date-dialog(ref="dialog" persistent="" v-model="modal" lazy="" full-width="" width="310px" :return-value.sync="date")
                v-btn(icon="" slot="activator")
                  v-icon filter_list
                v-date-picker(
                  :event-color="date => date[9] % 2 ? 'red' : 'yellow'"
                  :events="deploymentEvents"
                  v-model="date"
                  scrollable=""
                  style="border:none;width:310px"
                )
                  v-spacer
                  v-btn(flat="" color="primary" @click="modal = false") Cancel
                  v-btn(flat="" color="primary" @click="modal = false") Clear
                  v-btn(flat="" color="primary" @click="onDateFilter") OK
            v-list(two-line="" subheader="")
              v-list-tile(ripple="" avatar="" v-for="item in items" :key="item.title" :to="`deployments/${item.id}/orders`")
                v-list-tile-avatar
                  v-icon(:class="[item.iconClass]") {{ item.icon }}
                v-list-tile-content
                  v-list-tile-title {{ item.title }}
                  v-list-tile-sub-title {{ item.subtitle }}
                v-list-tile-action
                  v-btn(icon="" ripple="")
                    v-icon(color="grey lighten-1") info
            v-card-actions
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
      date: undefined,
      modal: false,
      page: 1,
      searchStyle: 'background-color:#ffffff38;'
    }
  },
  computed: {
    ...mapGetters('deployments', [
      'deployments'
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
    searchFocus (b) {
      this.searchStyle = (b ? '' : 'background-color:#ffffff38;')
    },
    onItemClick () {
    },
    onDateFilter () {
      this.modal = false
    },
    deploymentEvents (date) {
      console.log('deploymentEvents')
      const [,, day] = date.split('-')
      return parseInt(day, 10) % 3 === 0
    }
  },
  mounted () {}

}
</script>

<style>
  .search-bg-color {
    background-color: #03a9f43d
  }
  .dialog .card {
    border-radius: 0;
    -webkit-box-shadow: none;
    box-shadow: 0;
  }
</style>
