/**
 *  HomeAssistant Devices (AirPurifierLG) v2022-05-08
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
	definition (name: "HomeAssistant Devices (AirPurifierLG)", namespace: "clipman", author: "clipman", mnmn:"SmartThingsCommunity", vid: "cca2db22-dae2-3990-808c-3438e351f27a", ocfDeviceType: "oic.d.airpurifier") {
		capability "Switch"								//on, off
		capability "Dust Sensor"						//dustLevel, fineDustLevel
		capability "Very Fine Dust Sensor"				//veryFineDustLevel
		capability "Odor Sensor"						//odorLevel
		capability "circlecircle06391.airPurifierMode"	//airPurifierMode, supportedAirPurifierModes
		capability "circlecircle06391.signalLighting"	//signalLighting
		capability "circlecircle06391.airRemoval"		//airRemoval
		capability "circlecircle06391.circulateDir"		//circulateDir
		capability "Refresh"
	}
}

def setEntityStatus(state) {
	sendEvent(name: "switch", value: state)
}

def setEntityStatus(state, attributes) {
	log.debug "setEntityStatus(state, attributes) : ${state}, ${attributes}"
	def entity
   	entity = parent.getEntityStatus("sensor.anbang_gonggiceongjeonggi_dust_level")
	sendEvent(name: "dustLevel", value: entity.state, unit: entity.unit)
   	entity = parent.getEntityStatus("sensor.anbang_gonggiceongjeonggi_fine_dust_level")
	sendEvent(name: "fineDustLevel", value: entity.state, unit: entity.unit)
   	entity = parent.getEntityStatus("sensor.anbang_gonggiceongjeonggi_very_fine_dust_level")
	sendEvent(name: "veryFineDustLevel", value: entity.state, unit: entity.unit)
   	entity = parent.getEntityStatus("sensor.anbang_gonggiceongjeonggi_aqi")
	sendEvent(name: "odorLevel", value: entity.state, unit: entity.unit)

	//entity = parent.getEntityStatus("sensor.anbang_gonggiceongjeonggi_op_mode")
	//sendEvent(name: "airPurifierMode", value: entity.state)
	if(attributes.preset_mode == "클린부스터") {
		sendEvent(name: "airPurifierMode", value: "13")
	} else if(attributes.preset_mode == "싱글청정") {
		sendEvent(name: "airPurifierMode", value: "14")
	} else if(attributes.preset_mode == "오토모드") {
		sendEvent(name: "airPurifierMode", value: "16")
	} else {
		sendEvent(name: "airPurifierMode", value: "13")
	}

	entity = parent.getEntityStatus("switch.anbang_gonggiceongjeonggi_signal_lighting")
	sendEvent(name: "signalLighting", value: entity.state)
	entity = parent.getEntityStatus("switch.anbang_gonggiceongjeonggi_air_removal")
	sendEvent(name: "airRemoval", value: entity.state)
	entity = parent.getEntityStatus("switch.anbang_gonggiceongjeonggi_circulate_dir")
	sendEvent(name: "circulateDir", value: entity.state)
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

def setAirPurifierMode(airPurifierMode) {
	//클린부스터: 13, 싱글청정: 14, 오토모드: 16
	//log.debug "setAirPurifierMode(airPurifierMode=${airPurifierMode})"
	//makeCommand("SetOpMode", '{"OpMode":"' + airPurifierMode + '"}')
	parent.services("/api/services/fan/set_preset_mode", ["entity_id": "fan.anbang_gonggiceongjeonggi", "preset_mode": airPurifierMode])
}

def setSignalLighting(onOff) {
	parent.services("/api/services/homeassistant/turn_" + onOff, ["entity_id": "switch.anbang_gonggiceongjeonggi_signal_lighting"])
}

def setAirRemoval(onOff) {
	parent.services("/api/services/homeassistant/turn_" + onOff, ["entity_id": "switch.anbang_gonggiceongjeonggi_air_removal"])
}

def setCirculateDir(onOff) {
	parent.services("/api/services/homeassistant/turn_" + onOff, ["entity_id": "switch.anbang_gonggiceongjeonggi_circulate_dir"])
}