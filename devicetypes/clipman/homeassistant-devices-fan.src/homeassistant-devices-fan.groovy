/**
 *  HomeAssistant Devices (Fan) v2022-04-18
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
	definition (name: "HomeAssistant Devices (Fan)", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.fan") {
		capability "Switch"
		capability "Refresh"
	}
	preferences {
		input type: "paragraph", element: "paragraph", title: "만든이", description: "김민수 clipman@naver.com [날자]<br>네이버카페: Smartthings & IoT home Community", displayDuringSetup: false
		input type: "paragraph", element: "paragraph", title: "HomeAssistant Devices (Fan) v2022-04-18", description: "", displayDuringSetup: false
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
	refresh()
}

def refresh() {
	parent.updateEntity(device.deviceNetworkId)
}

def control(onOff) {
	def entity_id = device.deviceNetworkId
	parent.services("/api/services/homeassistant/turn_" + onOff, ["entity_id": entity_id])
	setStatus(onOff)
}