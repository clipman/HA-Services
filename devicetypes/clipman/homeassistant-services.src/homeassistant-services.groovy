/**
 *  HomeAssistant Services v2022-04-17
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
	definition (name: "HomeAssistant Services", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.switch") {
		capability "Switch"
		capability "Refresh"
	}
	preferences {
		input type: "paragraph", element: "paragraph", title: "만든이", description: "김민수 clipman@naver.com [날자]<br>네이버카페: Smartthings & IoT home Community", displayDuringSetup: false
		input type: "paragraph", element: "paragraph", title: "HomeAssistant Services v2022-04-17", description: "", displayDuringSetup: false
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
}

def refresh() {
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
	def params = [
		uri: parent.settings.haURL,
		path: service,
		headers: ["Authorization": "Bearer " + parent.settings.haToken],
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
