/**
 *  HomeAssistant Devices (YouTubeMusic) v2022-04-25
 *  clipman@naver.com
 *  날자
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */

metadata {
	definition (name: "HomeAssistant Devices (YouTubeMusic)", namespace: "clipman", author: "clipman", mnmn: "SmartThingsCommunity", vid: "8768bafd-3074-32af-b84d-d711d66779e0") {
		capability "Switch"
		capability "Refresh"
		capability "Music Player"
		capability "circlecircle06391.status"
		capability "circlecircle06391.lyrics"
	}
}

def setStatus(state) {
	log.debug "setStatus(state) : ${state}"
	if(state == "off") {
		sendEvent(name: "switch", value: "off")
	} else {
		sendEvent(name: "switch", value: "on")
	}
}

def setStatus(state, attributes) {
	log.debug "setStatus(state, attributes) : ${state}, ${attributes}"
	if(!attributes)	{
		return
	}

	try {
/*
		sendEvent(name: "volume_level", value: attributes.volume_level)
		sendEvent(name: "is_volume_muted", value: attributes.is_volume_muted)
		sendEvent(name: "media_title", value: attributes.media_title)
		sendEvent(name: "media_artist", value: attributes.media_artist)
		sendEvent(name: "remote_player_state", value: attributes.remote_player_state)
		sendEvent(name: "media_duration", value: attributes.media_duration)
		sendEvent(name: "media_position", value: attributes.media_position)
		sendEvent(name: "shuffle", value: attributes.shuffle)
		sendEvent(name: "repeat", value: attributes.repeat)
		sendEvent(name: "_media_id", value: attributes._media_id)
		sendEvent(name: "shuffle_mode", value: attributes.shuffle_mode)
		sendEvent(name: "likeStatus", value: attributes.likeStatus)
		sendEvent(name: "remote_player_id", value: attributes.remote_player_id)
		sendEvent(name: "entity_picture", value: attributes.entity_picture)
		sendEvent(name: "current_playlist_title", value: attributes.current_playlist_title)
*/
		sendEvent(name: "statusbar", value: (attributes.media_artist + '-' + attributes.media_title))
		sendEvent(name: "lyrics", value: (attributes.media_artist + '-' + attributes.media_title))
		sendEvent(name: "trackDescription", value: (attributes.media_artist + '-' + attributes.media_title))
		sendEvent(name: "level", value: Math.round(attributes.volume_level.toFloat() * 100.0) as int)
		sendEvent(name: "status", value: attributes.remote_player_state)
		if(attributes.remote_player_state == "playing"){
			if(device.currentValue("switch") == "off"){
				sendEvent(name: "switch", value: "on")
			}
		}
		if(attributes.is_volume_muted == true){
			if(device.currentValue("mute") != "muted"){
				sendEvent(name: "mute", value: "muted")
			}
		} else {
			if(device.currentValue("mute") != "unmuted"){
				sendEvent(name: "mute", value: "unmuted")
			}
		}
	} catch (e) {
		log.error "Exception caught while parsing data: "+e;
	}
}

def on() {
	control("on")
}

def off() {
	control("off")
}

def installed() {
	refresh()
}

def refresh() {
	parent.updateEntity(device.deviceNetworkId)
}

def control(onOff) {
	parent.services("/api/services/homeassistant/turn_" + onOff, ["entity_id": device.deviceNetworkId])
}

def setLyrics(lyrics) {
	sendEvent(name: "lyrics", value: lyrics)
}

def play() {
	parent.services("/api/services/media_player/media_play", ["entity_id": device.deviceNetworkId])
}

def stop() {
	parent.services("/api/services/media_player/media_stop", ["entity_id": device.deviceNetworkId])
}

def pause() {
	parent.services("/api/services/media_player/media_pause", ["entity_id": device.deviceNetworkId])
}

def mute()
{
	parent.services("/api/services/media_player/volume_mute", ["entity_id": device.deviceNetworkId, "is_volume_muted": true])
}

def unmute()
{
	parent.services("/api/services/media_player/volume_mute", ["entity_id": device.deviceNetworkId, "is_volume_muted": false])
}

def nextTrack() {
	parent.services("/api/services/media_player/media_next_track", ["entity_id": device.deviceNetworkId])
}

def previousTrack() {
	parent.services("/api/services/media_player/media_previous_track", ["entity_id": device.deviceNetworkId])
}

def setLevel(Number val) {
	setVolume(val)
}

def setVolume(Number vol_) {
	def level = vol_/100.0
	parent.services("/api/services/media_player/volume_set", ["entity_id": device.deviceNetworkId, "volume_level": level])
}

def volumeUp() {
	parent.services("/api/services/media_player/volume_up", ["entity_id": device.deviceNetworkId])
}

def volumeDown() {
	parent.services("/api/services/media_player/volume_down", ["entity_id": device.deviceNetworkId])
}

def playPlaylist(String content_id) {
	parent.services("/api/services/media_player/play_media", ["entity_id": device.deviceNetworkId, "media_content_id": content_id, "media_content_type": "playlist"])
}