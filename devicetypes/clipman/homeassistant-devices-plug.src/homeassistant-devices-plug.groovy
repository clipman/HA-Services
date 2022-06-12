/**
 *  HomeAssistant Devices (Plug) v2022-04-23
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
	definition (name: "HomeAssistant Devices (Plug)", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.smartplug") {
		capability "Switch"
		capability "Power Meter"
		capability "Energy Meter"
		capability "Refresh"
	}
}

def setEntityStatus(state) {
	sendEvent(name: "switch", value: state)
}

def setEntityStatus(state, attributes) {
	log.debug "setEntityStatus(state, attributes) : ${state}, ${attributes}"
	if(attributes["power"] != null){
		sendEvent(name: "power", value:  attributes["power"] as double, unit: "W", displayed: true)
	}else if(attributes["Power"] != null){	// Tasmota Power
		sendEvent(name: "power", value:  attributes["Power"] as double, unit: "W", displayed: true)
	}
	if(attributes["energy"] != null){
		sendEvent(name: "energy", value: attributes["energy"] as double, unit: "kWh", displayed: true)
	}else if(attributes["Total"] != null){	// Tasmota Energy
		sendEvent(name: "energy", value: attributes["Total"] as double, unit: "kWh", displayed: true)
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