/**
 *  HomeAssistant Devices (Ventilation) v2022-04-18
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
	definition (name: "HomeAssistant Devices (Ventilation)", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.fan") {
		capability "Switch"
		capability "Switch Level"
		capability "Refresh"
	}
}

def setEntityStatus(state) {
	sendEvent(name: "switch", value: state)
}

def setEntityStatus(state, attributes) {
	//log.debug "setEntityStatus(state, attributes) : ${state}, ${attributes}"
	if(state == "on" && attributes["preset_mode"]){
		def level = 0
		if(attributes["preset_mode"] == "Off"){
			level = 0
		}else if(attributes["preset_mode"] == "Low"){
			level = 33
		}else if(attributes["preset_mode"] == "Medium"){
			level = 66
		}else if(attributes["preset_mode"] == "High"){
			level = 100
		}
		sendEvent(name: "level", value: level, displayed: true)
	} else {
		sendEvent(name: "level", value: 0, displayed: true)
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

def setLevel(level){
	def oldLevel = device.currentValue("level")
    def newLevel = level
	def preset_mode = "Off"
	if(level == 0){
		preset_mode = "Off"
		newLevel = 0
	}else if(level > 0 && level < 34){
		preset_mode = "Low"
		newLevel = 33
	}else if(level > 33 && level < 67){
		preset_mode = "Medium"
		newLevel = 66
	}else if(level > 66){
		preset_mode = "High"
		newLevel = 100
	}
	if(oldLevel != newLevel) {
		parent.services("/api/services/fan/set_preset_mode", ["entity_id": device.deviceNetworkId, "preset_mode": preset_mode])
	} else {
		sendEvent(name: "level", value: newLevel, displayed: true)
	}
}
