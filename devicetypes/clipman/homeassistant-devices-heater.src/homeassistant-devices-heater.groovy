/**
 *  HomeAssistant Devices (Heater) v2022-04-23
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
	definition (name: "HomeAssistant Devices (Heater)", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.thermostat") {
		capability "Switch"
		capability "Thermostat Mode"
		capability "Thermostat Heating Setpoint"
		capability "Temperature Measurement"
		capability "Thermostat Operating State"
		capability "Refresh"
	}
}

def setEntityStatus(state) {
	sendEvent(name: "switch", value: state)
}

def setEntityStatus(state, attributes) {
	//log.debug "setEntityStatus(state, attributes) : ${state}, ${attributes}"
	def heatingSetpoint = attributes.temperature as int
	def temperature = attributes.current_temperature as int

	sendEvent(name: "thermostatMode", value: state, displayed: true)
	sendEvent(name: "heatingSetpoint", value: heatingSetpoint, unit: "C", displayed: true)
	sendEvent(name: "temperature", value: temperature, unit: "C", displayed: true)
	sendEvent(name: "thermostatOperatingState", value: ((state=="heat")&&(temperature<heatingSetpoint))? "heating" : "idle", displayed: true)

    def supportedModes = []
	supportedModes.addAll(attributes.hvac_modes)
	sendEvent(name: "supportedThermostatModes", value: supportedModes, displayed: false)
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

def setHeatingSetpoint(heatingSetpoint){
	parent.services("/api/services/climate/set_temperature", ["entity_id": device.deviceNetworkId, "temperature": heatingSetpoint])
}

def setThermostatMode(mode){
	parent.services("/api/services/climate/set_hvac_mode", ["entity_id": device.deviceNetworkId, "hvac_mode": mode])
}