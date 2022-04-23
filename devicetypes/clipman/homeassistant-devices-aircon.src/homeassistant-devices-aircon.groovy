/**
 *  HomeAssistant Devices (Aircon) v2022-04-23
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
	definition (name: "HomeAssistant Devices (Aircon)", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.airconditioner") {
		capability "Switch"								//on, off
		capability "Air Conditioner Mode"				//airConditionerMode, supportedAcModes=[auto, wind, cool, dry, coolClean, dryClean], setAirConditionerMode(mode)
		capability "Temperature Measurement"			//temperature
		capability "Thermostat Cooling Setpoint"		//coolingSetpoint, setCoolingSetpoint(setpoint)
		capability "Air Conditioner Fan Mode"			//fanMode, supportedAcFanModes=[auto, medium, high, turbo], setFanMode(fanMode)
		capability "Fan Speed"							//fanSpeed
		capability "Refresh"
	}
}

def setStatus(state) {
	sendEvent(name: "switch", value: state)
}

def setStatus(state, attributes) {
	//log.debug "setStatus(state, attributes) : ${state}, ${attributes}"
	def airConditionerMode = mapHA2STmode(state)
	def fanMode = attributes.fan_mode
	def temperature = attributes.current_temperature as int
	def coolingSetpoint = attributes.temperature as int

	sendEvent(name: "temperature", value: temperature, unit: "C", displayed: true)
	sendEvent(name: "coolingSetpoint", value: coolingSetpoint, unit: "C", displayed: true)

	def supportedHVACmodes = []
    def supportedAcModes = []
	supportedHVACmodes.addAll(attributes.hvac_modes)
	for (hvac_mode in supportedHVACmodes) {
		def st_mode = mapHA2STmode(hvac_mode)
		if(st_mode) supportedAcModes << st_mode
	}
	sendEvent(name: "supportedAcModes", value: supportedAcModes, displayed: false)

	def supportedAcFanModes = []
	supportedAcFanModes.addAll(attributes.fan_modes)
	sendEvent(name: "supportedAcFanModes", value: supportedAcFanModes, displayed: false)

	sendEvent(name: "airConditionerMode", value: airConditionerMode, displayed: true)
	sendEvent(name: "fanMode", value: fanMode, displayed: true)

	if(supportedAcFanModes[0] == "auto") {
		if(fanMode == supportedAcFanModes[0]) {
			sendEvent(name: "fanSpeed", value: 0)
		} else if(fanMode == supportedAcFanModes[1]){
			sendEvent(name: "fanSpeed", value: 1)
		} else if(fanMode == supportedAcFanModes[2]){
			sendEvent(name: "fanSpeed", value: 3)
		} else {
			sendEvent(name: "fanSpeed", value: 4)
		}
	} else {
		if(fanMode == supportedAcFanModes[0]) {
			sendEvent(name: "fanSpeed", value: 1)
		} else if(fanMode == supportedAcFanModes[1]){
			sendEvent(name: "fanSpeed", value: 2)
		} else {
			sendEvent(name: "fanSpeed", value: 4)
		}
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
	setStatus(onOff)
}

def setCoolingSetpoint(coolingSetpoint){
	parent.services("/api/services/climate/set_temperature", ["entity_id": device.deviceNetworkId, "temperature": coolingSetpoint])
}

def setAirConditionerMode(airConditionerMode){
	//log.info "setAirConditionerMode(airConditionerMode) : ${airConditionerMode}"
	def hvac_mode =	mapST2HAmode(airConditionerMode)
	if(hvac_mode) parent.services("/api/services/climate/set_hvac_mode", ["entity_id": device.deviceNetworkId, "hvac_mode": hvac_mode])
}

def setFanMode(fan_mode) {
	parent.services("/api/services/climate/set_fan_mode", ["entity_id": device.deviceNetworkId, "fan_mode": fan_mode])
}

def setFanSpeed(fanSpeed){
	try {
		def supportedAcFanModes = evaluate(device.currentValue("supportedAcFanModes"))
		if(supportedAcFanModes[0] == "auto") {
			if(fanSpeed == 0) {
				setFanMode(supportedAcFanModes[0])
			} else if(fanSpeed == 1) {
				setFanMode(supportedAcFanModes[1])
			} else if(fanSpeed == 2) {
				setFanMode(supportedAcFanModes[1])
			} else if(fanSpeed == 3) {
				setFanMode(supportedAcFanModes[2])
			} else{
				setFanMode(supportedAcFanModes[3])
			}
		} else {
			if(fanSpeed == 0) {
				//
			} else if(fanSpeed == 1) {
				setFanMode(supportedAcFanModes[0])
			} else if(fanSpeed == 2) {
				setFanMode(supportedAcFanModes[1])
			} else if(fanSpeed == 3) {
				setFanMode(supportedAcFanModes[1])
			} else{
				setFanMode(supportedAcFanModes[2])
			}
		}
	} catch (Exception e) {
		log.warn "supportedAcFanModes: ${e.message}"
	}
	sendEvent(name: "fanSpeed", value: fanSpeed)
}

def mapHA2STmode(ha_mode) {
	def ret = null
	Map mapping = ["heat_cool": "auto", "cool": "cool", "dry": "dry", "fan_only": "fanOnly"]
	try {
		ret = mapping[ha_mode]
	} catch(e) {
	}
	return ret
}

def mapST2HAmode(st_mode) {
	def ret = null
	Map mapping = ["auto": "heat_cool", "cool": "cool", "dry": "dry", "fanOnly": "fan_only", "coolClean": "cool", "dryClean": "dry"]
	try {
		ret = mapping[st_mode]
	} catch(e) {
	}
	return ret
}