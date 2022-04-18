/**
 *  HomeAssistant Sensors v2022-04-18
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
	definition (name: "HomeAssistant Sensors", namespace: "clipman", author: "clipman", mnmn: "SmartThingsCommunity", vid: "4e1ca28e-4993-3471-9de6-d6cadaa05a8c") {
		capability "Sensor"
		capability "Refresh"
		capability "circlecircle06391.statusBar"
	}
	preferences {
		input type: "paragraph", element: "paragraph", title: "만든이", description: "김민수 clipman@naver.com [날자]<br>네이버카페: Smartthings & IoT home Community", displayDuringSetup: false
		input type: "paragraph", element: "paragraph", title: "HomeAssistant Sensors v2022-04-18", description: "", displayDuringSetup: false
	}
}

def setStatus(state) {
	state = state.replace("\t", "").replace("\n", "")
   	sendEvent(name: "status", value: state)
}

def installed() {
	refresh()
}

def refresh() {
	parent.updateEntity(device.deviceNetworkId)
}
