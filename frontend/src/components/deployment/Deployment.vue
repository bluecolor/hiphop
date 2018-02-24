<template lang="pug">
 div(style="height:100%;")
  v-stepper(v-model='s' style="height:100%;")
    v-stepper-header
      v-stepper-step(step='1', :complete='s > 1') Select files
      v-divider
      v-stepper-step(step='2', :complete='s > 2') Select connections
      v-divider
      v-stepper-step(step='3' :complete='s > 3') Options
      v-divider
      v-stepper-step(step='4' :complete='s > 4') Result
    div
      v-stepper-items
        v-stepper-content(step='1')
          v-card.mb-5.elevation-0(color='green lighten-1')
            v-card-actions.white
              v-btn.btn-file(icon='' :disabled="files.length > 2")
                v-icon(color="grey darken-1", large='') attachment
                input(id="select-file" type="file" v-on:change="onFileSelect")
            v-list(two-line='', subheader='')
              v-list-tile(v-for='file in files', v-bind:key='file.name', avatar='', @click='')
                v-list-tile-avatar
                  v-icon(class='blue white--text') insert_drive_file
                v-list-tile-content
                  v-list-tile-title {{ file.name }}
                  v-list-tile-sub-title {{ file.size }} Bytes
                v-list-tile-action
                  v-btn(icon='', ripple='' @click="removeAttachment(file)")
                    v-icon(color='red lighten-2') delete

        v-stepper-content(step='2')
          v-card.mb-5.elevation-0
            v-card-title
              v-text-field(append-icon='search', label='Search', single-line='', hide-details='', v-model='search')
            v-data-table.elevation-0(
              v-model='selectedConnections',
              v-bind:headers='headers',
              v-bind:items='connections',
              select-all,
              hide-actions,
              item-key='id'
              v-bind:search="search"
            )
              template(slot='headerCell', slot-scope='props')
                v-tooltip(bottom='')
                  span(slot='activator')
                    | {{ props.header.text }}
                  span
                    | {{ props.header.text }}

              template(slot='items', slot-scope='props')
                tr(:active='props.selected', @click='props.selected = !props.selected')
                  td
                    v-checkbox(primary='', hide-details='', :input-value='props.selected')
                  td.text-xs-left {{ props.item.name }}
                  td.text-xs-left {{ props.item.username }}
                  td.text-xs-left {{ props.item.url }}
        v-stepper-content(step='3')
          v-layout(row wrap style="margin:20px;")
            v-flex(xs6)
              v-switch(label="Ignore on failure" color="primary" v-model="options.ignoreOnFail")
        v-stepper-content(step='4')
          v-card.mb-5.elevation-0(height='400px')
            v-card-title(primary-title='')
              v-spacer
              div
                h3.headline.mb-0(color="grey darken-1") Starting deployment ...
                div
                  v-progress-linear(v-bind:indeterminate="true")
              v-spacer
            v-card-actions
              v-layout(row)
                v-spacer
                v-btn(color='primary') Details
                v-spacer
      v-layout(row='')
        v-btn(v-show="s!==4" to="/deployments" color="error") cancel
        v-btn(v-show="s!==1 && s!==4" @click="back" color="warning") back
        v-btn(v-show="s <= 2" @click="next" color="primary" :disabled="isDisabled") continue
        v-spacer
        v-btn(v-show="s==3" @click="deploy" color="success") deploy

</template>

<script>
  import {mapActions, mapGetters} from 'vuex'
  import _ from 'lodash'

  export default {
    data () {
      return {
        s: 1,
        files: [
        ],
        headers: [{
          text: 'Name',
          align: 'left',
          value: 'name'
        }, {
          text: 'Username',
          value: 'username',
          align: 'left'
        }, {
          text: 'Url',
          align: 'left',
          value: 'url'
        }],
        selectedConnections: [],
        options: {
          ignoreOnFail: false
        },
        search: ''
      }
    },
    methods: {
      ...mapActions('deployments', [
        'request'
      ]),
      ...mapActions('notifications', [
        'snackWarning', 'snackSuccess', 'snackError'
      ]),
      next () {
        if (this.s < 4) {
          this.s += 1
        }
      },
      back () {
        if (this.s > 1) {
          this.s -= 1
        }
      },
      deploy () {
        this.next()
        let form = new FormData()
        _.each(this.files, (f) => {
          form.append('files', f._file)
        })
        _.each(this.selectedConnections, (c) => {
          form.append('connections', c.id)
        })

        this.request(form).then(() => {
          this.snackSuccess('Deployment request created')
          window.history.back()
        }).catch(e => {
          console.log(e)
          this.snackError('Failed to create deployment request')
        })
      },
      onFileSelect (e) {
        if (this.files.length > 2) {
          this.snackWarning('You can deploy maximum 3 files!')
          return
        }
        const f = e.target.files[0]
        this.files.push({
          name: f.name,
          size: f.size,
          _file: f
        })
      },
      removeAttachment (f) {
        const i = _.findIndex(this.files, {name: f.name})
        this.files.splice(i, 1)
      }
    },
    computed: {
      ...mapGetters('connections', [
        'connections'
      ]),
      isDisabled () {
        if (this.s === 1 && this.files.length === 0) {
          return true
        } else if (this.s === 2 && this.selectedConnections.length === 0) {
          return true
        }
      }
    }
  }
</script>

<style>
 .btn-file {
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .btn-file input[type=file] {
    position: absolute;
    top: 0;
    right: 0;
    min-width: 100%;
    min-height: 100%;
    font-size: 100px;
    text-align: right;
    filter: alpha(opacity=0);
    opacity: 0;
    outline: none;
    cursor: inherit;
    display: block;
  }
</style>

