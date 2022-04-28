/**
 *  HomeAssistant Devices (Washer) v2022-04-28
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
	definition (name: "HomeAssistant Devices (Washer)", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.washer") {
		capability "Switch"
		capability "Power Meter"
		capability "Energy Meter"
		capability "Refresh"
	}
}

def setEntityStatus(value) {
	state.switch = value
	sendEvent(name: "switch", value: value)
}

def setEntityStatus(state, attributes) {
	log.debug "setEntityStatus(state, attributes) : ${state}, ${attributes}"
	def entity
	if(attributes["power"] != null){
		sendEvent(name: "power", value:  attributes["power"] as double, unit: "W", displayed: true)
	} else {
        entity = parent.getEntityStatus("sensor.setaggi_power")
        sendEvent(name: "power", value: entity.state, unit: entity.unit, displayed: true)
	}
	if(attributes["energy"] != null){
		sendEvent(name: "energy", value: attributes["energy"] as double, unit: "kWh", displayed: true)
	} else {
        entity = parent.getEntityStatus("sensor.setaggi_energy")
        sendEvent(name: "energy", value: entity.state, unit: entity.unit, displayed: true)
	}
}

def on() {
	sendEvent(name: "switch", value: state.switch)
}

def off() {
	sendEvent(name: "switch", value: state.switch)
}

def installed() {
	state.switch = "off"
	sendEvent(name: "power", value: 0, unit: "W", displayed: true)
	sendEvent(name: "energy", value: 0, unit: "kWh", displayed: true)
	refresh()
}

def refresh() {
	parent.updateEntity(device.deviceNetworkId)
}