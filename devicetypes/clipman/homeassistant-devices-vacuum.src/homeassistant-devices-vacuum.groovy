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
	preferences {
		input type: "paragraph", element: "paragraph", title: "만든이", description: "김민수 clipman@naver.com [날자]<br>네이버카페: Smartthings & IoT home Community", displayDuringSetup: false
		input type: "paragraph", element: "paragraph", title: "HomeAssistant Devices (Vacuum) v2022-04-20", description: "", displayDuringSetup: false
	}
}

def setStatus(state) {
	sendEvent(name: "switch", value: state)
}

def on() {
	parent.services("/api/services/vacuum/start", ["entity_id": device.deviceNetworkId])
	setStatus("on")
}

def off() {
	parent.services("/api/services/vacuum/return_to_base", ["entity_id": device.deviceNetworkId])
	setStatus("off")
}

def installed() {
	refresh()
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
	sendEvent(name: "vacuum", value: device.currentValue("vacuum"))
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
	//log.debug "setFanSpeed >> ${speed}"
	//Silent, Standard, Medium, Turbo
	parent.services("/api/services/vacuum/set_fan_speed", ["entity_id": device.deviceNetworkId, "fan_speed":speed])
	sendEvent(name: "cleanfanspeed", value: speed)
}

def refresh(){
	getSensorState("sensor.rockrobo_vacuum_v1_last_clean_start")
	getSensorState("sensor.rockrobo_vacuum_v1_last_clean_end")
	getSensorState("sensor.rockrobo_vacuum_v1_current_clean_area")
	getSensorState("sensor.rockrobo_vacuum_v1_current_clean_duration")
	getSensorState("sensor.rockrobo_vacuum_v1_total_clean_area")
	getSensorState("sensor.rockrobo_vacuum_v1_total_duration")
	getSensorState("sensor.rockrobo_vacuum_v1_total_clean_count")
	getSensorState("sensor.rockrobo_vacuum_v1_filter_left")
	getSensorState("sensor.rockrobo_vacuum_v1_main_brush_left")
	getSensorState("sensor.rockrobo_vacuum_v1_side_brush_left")
	getSensorState("sensor.rockrobo_vacuum_v1_sensor_dirty_left")
	getSensorState(device.deviceNetworkId)
}

def getSensorState(entity_id){
	def service = "/api/states/${entity_id}"
	def params = [
		uri: parent.settings.haURL,
		path: service,
		headers: ["Authorization": "Bearer " + parent.settings.haToken],
		requestContentType: "application/json"
	]
	//def json = []
	try {
		httpGet(params) { resp ->
			resp.headers.each {
				//log.debug "${it.name} : ${it.value}"
			}
			if (resp.status == 200) {
				// resp.data: [attributes:[friendly_name:rockrobo.vacuum.v1 Current Clean Duration, icon:mdi:timer-sand, unit_of_measurement:s], context:[id:854463bda101b4ef98586909bc437f75, parent_id:null, user_id:null], entity_id:sensor.rockrobo_vacuum_v1_current_clean_duration, last_changed:2022-04-16T03:55:17.772340+00:00, last_updated:2022-04-16T03:55:17.772340+00:00, state:3740]
				//def obj = [entity_id: "${resp.data.entity_id}", state: "${resp.data.state}", attributes: "${resp.data.attributes}"]
				//json.push(obj)
				setVacuumStatus(resp.data.entity_id, resp.data.state, resp.data.attributes)
			}
		}
	} catch (e) {
		log.error "HomeAssistant Services Error: $e"
	}
}

def setVacuumStatus(entity_id, state, attributes){
	//log.debug "Status[${entity_id}] >> ${state}"
	switch(entity_id) {
	case "sensor.rockrobo_vacuum_v1_last_clean_start":
		sendEvent(name: "cleanstart", value: state, unit: "")
		break;
	case "sensor.rockrobo_vacuum_v1_last_clean_end":
		sendEvent(name: "cleanstop", value: state, unit: "")
		break;
	case "sensor.rockrobo_vacuum_v1_current_clean_area":
		sendEvent(name: "cleanedarea", value: state, unit: "㎡")
		break;
	case "sensor.rockrobo_vacuum_v1_current_clean_duration":
		sendEvent(name: "cleaningtime", value: (state as int)/60, unit: "분")
		break;
	case "sensor.rockrobo_vacuum_v1_total_clean_area":
		sendEvent(name: "totalcleanedarea", value: state, unit: "㎡")
		break;
	case "sensor.rockrobo_vacuum_v1_total_duration":
		sendEvent(name: "totalcleaningtime", value: (state as int)/60, unit: "분")
		break;
	case "sensor.rockrobo_vacuum_v1_total_clean_count":
		sendEvent(name: "cleaningcount", value: state, unit: "")
		break;
	case "sensor.rockrobo_vacuum_v1_filter_left":
		sendEvent(name: "filterleft", value: (state as int)/3600, unit: "시간")
		break;
	case "sensor.rockrobo_vacuum_v1_main_brush_left":
		sendEvent(name: "mainbrushleft", value: (state as int)/3600, unit: "시간")
		break;
	case "sensor.rockrobo_vacuum_v1_side_brush_left":
		sendEvent(name: "sidebrushleft", value: (state as int)/3600, unit: "시간")
		break;
	case "sensor.rockrobo_vacuum_v1_sensor_dirty_left":
		sendEvent(name: "sensordirtyleft", value: (state as int)/3600, unit: "시간")
		break;
	case device.deviceNetworkId:
		sendEvent(name: "vacuum", value: state)	// 대시보드 상태바에 출력됨
		sendEvent(name: "statusbar", value: attributes.status+"("+state+")")
		sendEvent(name: "battery", value: attributes.battery_level, unit: "%")
		sendEvent(name: "cleanfanspeed", value: attributes.fan_speed)
		if(state == "docked" || state == "returning") {
			state = "off"
		} else {
			state = "on"
		}
		setStatus(state)
		break;
	default:
		break;
	}
}