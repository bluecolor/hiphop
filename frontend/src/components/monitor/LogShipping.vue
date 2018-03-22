<template lang="pug">
div(
  id="e3"
  style="max-width: 900px; margin: auto; width:100%; margin-top:10px;"
  class="lighten-3"
  margin-top="10px;"
)
  div(
    v-show="false"
    id="e3"
    class="lighten-3"
  )
    v-container(fluid='', style='min-height: 0;', grid-list-lg='')
      v-layout(row="")
        v-flex
          v-card
            v-toolbar(color="light-blue" dark="")
              v-toolbar-title(class="white--text") Log Shipping
              v-spacer
              v-text-field.elevation-0(v-model="filter.search" v-on:blur="searchFocus(false)", v-on:focus="searchFocus(true)" :style="searchStyle" light="" solo="" clearable="" prepend-icon="search" placeholder="Type keyword...")
              v-dialog.filter-dialog(ref="dialog" persistent v-model="filter.modal" lazy full-width width="510px")
                v-btn(icon="" slot="activator")
                  v-icon(:color="(isFiltered ? 'red': 'white')") filter_list
                v-card
                  v-toolbar(color="orange darken-1")
                    v-toolbar-title(class="white--text") Filters
                  v-container(style="height:310px; padding-top:30px;")
                    v-select(
                      solo
                      label='Labels',
                      clearable
                      style="padding:0px 10px 0px 10px;"
                      :items="labels"
                      item-text="name"
                      item-value="id"
                      multiple
                      chips
                      max-height="auto"
                      autocomplete,
                      v-model="filter.labels"
                    )
                      template(slot="selection" slot-scope="data")
                        v-chip.chip--select-multi(
                          small
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
                            v-list-tile-title(v-html="data.item.name" @click="")
                    v-select(
                      solo
                      label='Connections',
                      clearable
                      style="margin-top: 20px;padding:0px 10px 0px 10px;"
                      :items="connectionItems"
                      item-text="name"
                      item-value="id"
                      multiple
                      chips
                      max-height="auto"
                      autocomplete,
                      v-model="filter.connections"
                    )
                      template(slot="selection" slot-scope="data")
                        v-chip.chip--select-multi(
                          small
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
                          v-list-tile-content
                            v-list-tile-title(v-html="data.item.name" @click="")
                    v-text-field(v-model="filter.search" style="margin-top: 20px;" light solo clearable prepend-icon="search" placeholder="Type keyword...")
                    v-switch(style="margin-top: 20px;" :label="filter.error ? 'Error' : 'All'" color="red darken-3" v-model="filter.error")
                  v-card-actions
                    v-layout(row)
                      v-spacer
                      v-btn(flat="" color="primary" @click="onClearFilter") Clear
                      v-btn(flat="" color="primary" @click="filter.modal = false") OK
            v-list(two-line="" subheader="")
              v-list-tile(ripple="" avatar="" v-for="item in items" :key="item.title" @click="")
                v-list-tile-avatar
                  v-icon(:class="[item.iconClass]") {{ item.icon }}
                v-list-tile-content
                  v-list-tile-title {{ item.title }}
                  v-list-tile-sub-title {{ item.subtitle}}
            v-card-actions
              v-spacer
              v-pagination(:length="6" v-model="page")

  div(
    id="e3"
    class="lighten-3"
  )
    div.welcome()
      div.welcome-message
        v-icon.empty-icon(x-large color="grey lighten-1") wallpaper
        h3 No logshipping data collected


</template>

<script>

import _ from 'lodash'
import {mapGetters} from 'vuex'

export default {
  data () {
    return {
      filter: {
        labels: [],
        connections: [],
        search: undefined,
        modal: false
      },
      show: true,
      page: 1,
      searchStyle: 'background-color:#ffffff38;'
    }
  },
  computed: {
    ...mapGetters('connections', [
      'connections'
    ]),
    ...mapGetters('labels', [
      'labels'
    ]),
    isFiltered () {
      return (
        !_.isEmpty(this.filter.connections) ||
        !_.isEmpty(this.filter.labels) ||
        this.filter.search
      )
    },
    connectionItems () {
      return _.filter(this.connections, {vendor: 'MS_SQL'})
    },
    items () {
      return [
        { id: 1, icon: 'done', iconClass: 'green white--text', title: 'Vacation itinerary', subtitle: 'Jan 20, 2014' },
        { id: 2, icon: 'priority_high', iconClass: 'red white--text', title: 'Kitchen remodel', subtitle: 'Jan 10, 2014' },
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
    onClearFilter () {
      this.filter.date = undefined
      this.filter.modal = false
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

  .dialog .input-group--text-field label {
    top: 0px !important;
  }

.welcome {
  display: flex;
  flex-grow: 1;
}

.welcome-message {
  margin: auto;
  margin-top: 40px;
  display: flex;
  flex-direction: column;
}

.welcome-message > h3 {
  font-size: 30px;
  color: silver;
}

.empty-icon {
  font-size: 100px !important;
}

</style>
