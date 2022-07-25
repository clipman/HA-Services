/**
 *  HomeAssistant Devices (Aircon) v2022-07-22
 *  clipman@naver.com
 *  날자
 *  온도조절 Step: 0.1
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
	definition (name: "HomeAssistant Devices (Aircon)", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.airconditioner", mnmn: "SmartThingsCommunity", vid: "cf6a895f-409a-3a6f-b48d-8ba77a617190") {
		capability "Switch"								//on, off
		capability "Air Conditioner Mode"				//airConditionerMode, supportedAcModes=[auto, wind, cool, dry, coolClean, dryClean], setAirConditionerMode(mode)
		capability "Temperature Measurement"			//temperature
		capability "Thermostat Cooling Setpoint"		//coolingSetpoint, setCoolingSetpoint(setpoint)
		capability "Air Conditioner Fan Mode"			//fanMode, supportedAcFanModes=[auto, medium, high, turbo], setFanMode(fanMode)
		capability "Fan Speed"							//fanSpeed
		capability "Refresh"
	}
}

def setEntityStatus(state) {
	if(state=="off") {
		sendEvent(name: "switch", value: "off")
	} else {
		sendEvent(name: "switch", value: "on")
	}
}

def setEntityStatus(state, attributes) {
	//log.debug "setEntityStatus(state, attributes) : ${state}, ${attributes}"
	def airConditionerMode = modeHA2ST(state)
	def fanMode = attributes.fan_mode
	//def temperature = attributes.current_temperature as int
	def temperature = attributes.current_temperature as Double
	//def coolingSetpoint = attributes.temperature as int
	def coolingSetpoint = attributes.temperature as Double

	sendEvent(name: "temperature", value: temperature, unit: "C", displayed: true)
	sendEvent(name: "coolingSetpoint", value: coolingSetpoint, unit: "C", displayed: true)

	def supportedHVACmodes = []
    def supportedAcModes = []
	supportedHVACmodes.addAll(attributes.hvac_modes)
	for (hvac_mode in supportedHVACmodes) {
		def st_mode = modeHA2ST(hvac_mode)
		if(st_mode) supportedAcModes << st_mode
	}
	sendEvent(name: "supportedAcModes", value: supportedAcModes, displayed: false)

	def supportedAcFanModes = []
	supportedAcFanModes.addAll(attributes.fan_modes)
	sendEvent(name: "supportedAcFanModes", value: supportedAcFanModes, displayed: false)

	sendEvent(name: "airConditionerMode", value: airConditionerMode, displayed: true)
	sendEvent(name: "fanMode", value: fanMode, displayed: true)

	sendEvent(name: "fanSpeed", value: speedFanMode2FanSpeed(fanMode))
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

def setCoolingSetpoint(coolingSetpoint){
	parent.services("/api/services/climate/set_temperature", ["entity_id": device.deviceNetworkId, "temperature": coolingSetpoint])
}

def setAirConditionerMode(airConditionerMode){
	//log.info "setAirConditionerMode(airConditionerMode) : ${airConditionerMode}"
	def hvac_mode =	modeST2HA(airConditionerMode)
	if(hvac_mode) parent.services("/api/services/climate/set_hvac_mode", ["entity_id": device.deviceNetworkId, "hvac_mode": hvac_mode])
}

def setFanMode(fan_mode) {
	parent.services("/api/services/climate/set_fan_mode", ["entity_id": device.deviceNetworkId, "fan_mode": fan_mode])
}

def setFanSpeed(fanSpeed){
	setFanMode(speedFanSpeed2FanMode(fanSpeed))
	sendEvent(name: "fanSpeed", value: speedFanMode2FanSpeed(speedFanSpeed2FanMode(fanSpeed)))
}

def modeHA2ST(ha_mode) {
	//Map mode = ["heat_cool": "auto", "cool": "cool", "dry": "dry", "fan_only": "fanOnly"]
	Map mode = ["auto": "auto", "heat_cool": "auto", "cool": "cool", "dry": "dry", "fan_only": "fanOnly"]
	return mode[ha_mode]
}

def modeST2HA(st_mode) {
	//Map mode = ["auto": "heat_cool", "cool": "cool", "dry": "dry", "fanOnly": "fan_only", "coolClean": "cool", "dryClean": "dry", "heatClean": "fan_only", "notSupported": "heat_cool"]
	Map mode = ["auto": "auto", "cool": "cool", "dry": "dry", "fanOnly": "fan_only", "coolClean": "cool", "dryClean": "dry", "heatClean": "fan_only", "notSupported": "auto"]
	return mode[st_mode]
}

def speedFanMode2FanSpeed(ha_speed) {
	Map speed = ["auto": 0, "medium": 1, "high": 2, "turbo": 4]
	//Map speed = ["low": 1, "medium": 2, "high": 3]
	return speed[ha_speed]
}

def speedFanSpeed2FanMode(st_speed) {
	Map speed = [0: "auto", 1: "medium", 2: "high", 3: "turbo", 4: "turbo"]
	//Map speed = [0: "low", 1: "low", 2: "medium", 3: "high", 4: "high"]
	return speed[st_speed]
}