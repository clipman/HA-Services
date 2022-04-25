/**
 *  HomeAssistant Devices (Vacuum) v2022-04-20
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
	definition (name: "HomeAssistant Devices (Vacuum)", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.robotcleaner",
		mnmn: "SmartThingsCommunity", vid: "c2ffe153-00c5-3728-8310-12a0b3e40907") {
		capability "Switch"
		capability "Battery"
		capability "Refresh"
		capability "circlecircle06391.vacuum"
		capability "circlecircle06391.status"	 	// statusbar
		capability "circlecircle06391.cleanstart"
		capability "circlecircle06391.cleanstop"
		capability "circlecircle06391.cleanedarea"
		capability "circlecircle06391.cleaningtime"
		capability "circlecircle06391.cleaningcount"
		capability "circlecircle06391.cleanfanspeed"
		capability "circlecircle06391.filterleft"
		capability "circlecircle06391.mainbrushleft"
		capability "circlecircle06391.sidebrushleft"
		capability "circlecircle06391.sensordirtyleft"
		capability "circlecircle06391.totalcleanedarea"
		capability "circlecircle06391.totalcleaningtime"

		command "start"
		command "stop"
		command "pause"
		command "locate"
		command "returnToHome"
		command "cleanSpot"
	}
}

def setEntityStatus(state) {
	sendEvent(name: "switch", value: state)
}

def setEntityStatus(state, attributes) {
	//log.debug "setEntityStatus(state, attributes) : ${state}, ${attributes}"
	sendEvent(name: "vacuum", value: state)	// 대시보드 상태바에 출력됨
	sendEvent(name: "statusbar", value: attributes.status+"("+state+")")
	sendEvent(name: "battery", value: attributes.battery_level, unit: "%")
	sendEvent(name: "cleanfanspeed", value: attributes.fan_speed)

	def entity
   	entity = parent.getEntityStatus("sensor.rockrobo_vacuum_v1_last_clean_start")
	sendEvent(name: "cleanstart", value: entity.state, unit: entity.unit)
   	entity = parent.getEntityStatus("sensor.rockrobo_vacuum_v1_last_clean_end")
	sendEvent(name: "cleanstop", value: entity.state, unit: entity.unit)
   	entity = parent.getEntityStatus("sensor.rockrobo_vacuum_v1_current_clean_area")
	sendEvent(name: "cleanedarea", value: entity.state, unit: entity.unit)
   	entity = parent.getEntityStatus("sensor.rockrobo_vacuum_v1_current_clean_duration")
	sendEvent(name: "cleaningtime", value: (entity.state as int)/60, unit: "분")
   	entity = parent.getEntityStatus("sensor.rockrobo_vacuum_v1_total_clean_area")
	sendEvent(name: "totalcleanedarea", value: entity.state, unit: entity.unit)
   	entity = parent.getEntityStatus("sensor.rockrobo_vacuum_v1_total_duration")
	sendEvent(name: "totalcleaningtime", value: (entity.state as int)/60, unit: "분")
   	entity = parent.getEntityStatus("sensor.rockrobo_vacuum_v1_total_clean_count")
	sendEvent(name: "cleaningcount", value: entity.state, unit: entity.unit)
   	entity = parent.getEntityStatus("sensor.rockrobo_vacuum_v1_filter_left")
	sendEvent(name: "filterleft", value: (entity.state as int)/3600, unit: "시간")
   	entity = parent.getEntityStatus("sensor.rockrobo_vacuum_v1_main_brush_left")
	sendEvent(name: "mainbrushleft", value: (entity.state as int)/3600, unit: "시간")
   	entity = parent.getEntityStatus("sensor.rockrobo_vacuum_v1_side_brush_left")
	sendEvent(name: "sidebrushleft", value: (entity.state as int)/3600, unit: "시간")
   	entity = parent.getEntityStatus("sensor.rockrobo_vacuum_v1_sensor_dirty_left")
	sendEvent(name: "sensordirtyleft", value: (entity.state as int)/3600, unit: "시간")
}

def on() {
	parent.services("/api/services/vacuum/start", ["entity_id": device.deviceNetworkId])
}

def off() {
	parent.services("/api/services/vacuum/return_to_base", ["entity_id": device.deviceNetworkId])
}

def installed() {
	refresh()
}

def refresh(){
	parent.updateEntity(device.deviceNetworkId)
}

def setVacuum(mode){
	log.debug "setVacuum '${mode}'"
	switch(mode) {
	case "start":
  		start()
		break;
	case "stop":
  		stop()
		break;
	case "pause":
		pause()
		break;
	case "returnToHome":
  		returnToHome()
		break;
	case "cleanSpot":
  		cleanSpot()
		break;
	case "locate":
  		locate()
		break;
	default:
		returnToHome()
		break;
	}
}

def start(){
	parent.services("/api/services/vacuum/start", ["entity_id": device.deviceNetworkId])
}

def pause(){
	parent.services("/api/services/vacuum/pause", ["entity_id": device.deviceNetworkId])
}

def stop(){
	parent.services("/api/services/vacuum/stop", ["entity_id": device.deviceNetworkId])
}

def locate(){
	parent.services("/api/services/vacuum/locate", ["entity_id": device.deviceNetworkId])
}

def returnToHome(){
	parent.services("/api/services/vacuum/return_to_base", ["entity_id": device.deviceNetworkId])
}

def cleanSpot(){
	parent.services("/api/services/vacuum/clean_spot", ["entity_id": device.deviceNetworkId])
}

def setFanSpeed(speed){
	parent.services("/api/services/vacuum/set_fan_speed", ["entity_id": device.deviceNetworkId, "fan_speed":speed])
}