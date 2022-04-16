/**
 *  HomeAssistant Services (Vacuum) v2022-04-16
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
	definition (name: "HomeAssistant Services (Vacuum)", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.robotcleaner",
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
		input name: "haURL", type: "text", title:"HomeAssistant external URL(ex, https://xxx.duckdns.org)", required: false
		input name: "haToken", type: "text", title: "HomeAssistant Token", required: false

		input type: "paragraph", element: "paragraph", title: "만든이", description: "김민수 clipman@naver.com [날자]<br>네이버카페: Smartthings & IoT home Community", displayDuringSetup: false
		input type: "paragraph", element: "paragraph", title: "HomeAssistant Services v2022-04-16", description: "", displayDuringSetup: false
	}
}

def setStatus(state) {
   	sendEvent(name: "switch", value: state)
}

def on() {
	control("on")
}

def off() {
	control("off")
}

def installed() {
	//sendEvent(name: "switch", value: "off")
}

def control(onOff) {
	def entity_id = device.deviceNetworkId
	def entity_type = entity_id.split('\\.')[0]
	def entity_name = entity_id.split('\\.')[1]

	switch(entity_type) {
		case "vacuum":
			if(onOff == "on") {
				services("/api/services/vacuum/start", ["entity_id": entity_id])
			} else {
				services("/api/services/vacuum/return_to_base", ["entity_id": entity_id])
			}
			break;
		case "cover":
			if(onOff == "on") {
				services("/api/services/cover/open_cover", ["entity_id": entity_id])
			} else {
				services("/api/services/cover/close_cover", ["entity_id": entity_id])
			}
			break;
		case "lock":
			if(onOff == "on") {
				services("/api/services/lock/unlock", ["entity_id": entity_id])
			} else {
				services("/api/services/lock/lock", ["entity_id": entity_id])
			}
			break;
		case "script":
			if(onOff == "on") {
				services("/api/services/script/" + entity_name, [])
			}
			onOff = "off"
			break;
		case "rest_command":
			if(onOff == "on") {
				services("/api/services/rest_command/" + entity_name, [])
			}
			onOff = "off"
			break;
		case "esphome":
			if(onOff == "on") {
				services("/api/services/esphome/" + entity_name, [])
			}
			onOff = "off"
			break;
		case "button":
			if(onOff == "on") {
				services("/api/services/button/press", ["entity_id": entity_id])
			}
			onOff = "off"
			break;
		case "input_button":
			if(onOff == "on") {
				services("/api/services/input_button/press", ["entity_id": entity_id])
			}
			onOff = "off"
			break;
		default:	//switch, light, climate, fan, input_boolean, ...
			services("/api/services/homeassistant/turn_" + onOff, ["entity_id": entity_id])
			break;
	}
	sendEvent(name: "switch", value: onOff)
}

def services(service, data) {
	if(!settings.haURL) {
		settings.haURL = parent.settings.haURL
	}
	if(!settings.haToken) {
		settings.haToken = parent.settings.haToken
	}

	def params = [
		uri: settings.haURL,
		path: service,
		headers: ["Authorization": "Bearer " + settings.haToken],
		requestContentType: "application/json",
		body: data
	]
	log.info "Service: $params"
	try {
		httpPost(params) { resp ->
			return true
		}
	} catch (e) {
		log.error "HomeAssistant Services Error: $e"
		return false
	}
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
	services("/api/services/vacuum/start", ["entity_id": device.deviceNetworkId])
}

def pause(){
	services("/api/services/vacuum/pause", ["entity_id": device.deviceNetworkId])
}

def stop(){
	services("/api/services/vacuum/stop", ["entity_id": device.deviceNetworkId])
}

def locate(){
	services("/api/services/vacuum/locate", ["entity_id": device.deviceNetworkId])
}

def returnToHome(){
	services("/api/services/vacuum/return_to_base", ["entity_id": device.deviceNetworkId])
}

def cleanSpot(){
	services("/api/services/vacuum/clean_spot", ["entity_id": device.deviceNetworkId])
}

def setFanSpeed(speed){
	//log.debug "setFanSpeed >> ${speed}"
	//Silent, Standard, Medium, Turbo
	services("/api/services/vacuum/set_fan_speed", ["entity_id": device.deviceNetworkId, "fan_speed":speed])
	sendEvent(name: "cleanfanspeed", value: speed, unit: "")
}

def refresh(){
	getSensorState(device.deviceNetworkId)
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
}

def getSensorState(entity_id){
	if(!settings.haURL) {
		settings.haURL = parent.settings.haURL
	}
	if(!settings.haToken) {
		settings.haToken = parent.settings.haToken
	}

	def service = "/api/states/${entity_id}"
	def params = [
		uri: settings.haURL,
		path: service,
		headers: ["Authorization": "Bearer " + settings.haToken],
		requestContentType: "application/json"
	]
	def json = []
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
			setStatus(state)
			sendEvent(name: "statusbar", value: attributes.status+"("+state+")", unit: "")
			sendEvent(name: "battery_level", value: attributes.battery_level, unit: "%")
			sendEvent(name: "fan_speed", value: attributes.fan_speed, unit: "")
			break;
		default:
			break;
	}
}