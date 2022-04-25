/**
 *  HomeAssistant Devices (GoogleHome) v2022-04-25
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
	definition (name: "HomeAssistant Devices (GoogleHome)", namespace: "clipman", author: "clipman", mnmn: "SmartThingsCommunity", vid: "9ad7b709-f12a-3e65-bf9c-58e823fcca8d") {
		capability "Music Player"
		capability "Media Playback"
		capability "Audio Notification"
		capability "Speech Synthesis"
		capability "Refresh"
		capability "circlecircle06391.status"
	}
}

def setEntityStatus(state) {
	log.debug "setEntityStatus(state) : ${state}"
	switch(state) {
	case "playing":
		break;
	case "paused":
		setStatusbar("잠시멈춤")
		break;
	default:
		setStatusbar("중지됨")
		break;
	}
}

def setEntityStatus(state, attributes) {
	if(attributes) {
		log.debug "setEntityStatus(state, attributes) : ${state}, ${attributes}"
	} else {
		log.info "변경된 상태의 속성값을 읽어오지 못하여 상태를 다시 읽어옵니다."
		refresh()
		return
	}

	sendEvent(name: "playbackStatus", value: (state == "playing" || state == "paused") ? state : "stopped", displayed: "true")
	sendEvent(name: "status", value: (state == "playing" || state == "paused") ? state : "stopped", displayed: "true")
	if (attributes.volume_level != null) {
		sendEvent(name: "level", value: Math.round(attributes.volume_level*100))
	}
	sendEvent(name: "mute", value: ("${attributes.is_volume_muted}"=="true") ? "muted" : "unmuted", displayed: "true")
	if (attributes.media_title != null) {
		sendEvent(name: "trackDescription", value: (attributes.media_artist + '-' + attributes.media_title), displayed: "true")
		if(state == "playing") {
			setStatusbar(attributes.media_title)
		}
	} else {
		sendEvent(name: "trackDescription", value: "-", displayed: "true")
		if(state == "playing") {
			setStatusbar("재생중")
		}
	}
}

def installed() {
	setStatusbar("설치됨")
	sendEvent(name: "supportedPlaybackCommands", value: ["play","pause","stop"], displayed: false)
	refresh()
}

def refresh() {
	parent.updateEntity(device.deviceNetworkId)
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

def setLevel(level) {
	setVolume(level)
}

def setVolume(volume) {
	parent.services("/api/services/media_player/volume_set", ["entity_id": device.deviceNetworkId, "volume_level": volume/100])
}

def deviceNotification(notification) {
	speak(notification)
}

def speak(text) {
	parent.services("/api/services/tts/google_say", ["entity_id": device.deviceNetworkId, "message": text])
}

def playTrackAndResume(uri, level = -1){
	playTrack(uri, level)
}

def playTrackAndRestore(uri, level = -1){
	playTrack(uri, level)
}

def playTrack(uri, level = -1){
	if (level != -1) setVolume(level)
	parent.services("/api/services/media_player/play_media", ["entity_id": device.deviceNetworkId, "media_content_id": uri, "media_content_type": "music"])
}

def setStatusbar(status) {
	sendEvent(name: "statusbar", value: status, displayed: "true")
}
