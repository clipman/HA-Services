/**
 *  HomeAssistant Devices v2022-04-18
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
	definition (name: "HomeAssistant Devices", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.switch") {
		capability "Switch"
		capability "Refresh"
	}
	preferences {
		input type: "paragraph", element: "paragraph", title: "만든이", description: "김민수 clipman@naver.com [날자]<br>네이버카페: Smartthings & IoT home Community", displayDuringSetup: false
		input type: "paragraph", element: "paragraph", title: "HomeAssistant Devices v2022-04-18", description: "", displayDuringSetup: false
	}
}

def setStatus(state) {
	sendEvent(name: "switch", value: state)
}

def setStatus(state, attributes) {
	//log.debug "setStatus(state, attributes) : ${state}, ${attributes}"
	/*
	if(attributes["power"] != null){
		sendEvent(name: "power", value:  attributes["power"] as double, unit: "W", displayed: true)
	}
	if(attributes["energy"] != null){
		sendEvent(name: "energy", value: attributes["energy"] as double, unit: "kWh", displayed: true)
	}
	*/
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
	def entity_id = device.deviceNetworkId
	def entity_type = entity_id.split('\\.')[0]
	def entity_name = entity_id.split('\\.')[1]

	switch(entity_type) {
	case "vacuum":
		if(onOff == "on") {
			parent.services("/api/services/vacuum/start", ["entity_id": entity_id])
		} else {
			parent.services("/api/services/vacuum/return_to_base", ["entity_id": entity_id])
		}
		break;
	case "cover":
		if(onOff == "on") {
			parent.services("/api/services/cover/open_cover", ["entity_id": entity_id])
		} else {
			parent.services("/api/services/cover/close_cover", ["entity_id": entity_id])
		}
		break;
	case "lock":
		if(onOff == "on") {
			parent.services("/api/services/lock/unlock", ["entity_id": entity_id])
		} else {
			parent.services("/api/services/lock/lock", ["entity_id": entity_id])
		}
		break;
	case "script":
		if(onOff == "on") {
			parent.services("/api/services/script/" + entity_name, [])
		}
           onOff = "off"
		break;
	case "rest_command":
		if(onOff == "on") {
			parent.services("/api/services/rest_command/" + entity_name, [])
		}
           onOff = "off"
		break;
	case "esphome":
		if(onOff == "on") {
			parent.services("/api/services/esphome/" + entity_name, [])
		}
		onOff = "off"
		break;
	case "button":
		if(onOff == "on") {
			parent.services("/api/services/button/press", ["entity_id": entity_id])
		}
           onOff = "off"
		break;
	case "input_button":
		if(onOff == "on") {
			parent.services("/api/services/input_button/press", ["entity_id": entity_id])
		}
		onOff = "off"
		break;
	default:	//switch, light, climate, fan, input_boolean, ...
		parent.services("/api/services/homeassistant/turn_" + onOff, ["entity_id": entity_id])
		break;
	}
	setStatus(onOff)
}