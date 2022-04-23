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
	definition (name: "HomeAssistant Sensors", namespace: "clipman", author: "clipman", mnmn: "SmartThingsCommunity", vid: "124ffa8c-6026-3bae-93ae-045746e2aa07") {
		capability "Refresh"
		capability "circlecircle06391.statusBar"
		capability "circlecircle06391.string"
		capability "circlecircle06391.number"
		capability "circlecircle06391.unit"
	}
}

def setStatus(state) {
	state = state.replace("\t", "").replace("\n", "")
   	sendEvent(name: "status", value: state)
}

def setStatus(state, attributes) {
	//log.debug "setStatus(state, attributes) : ${state}, ${attributes}"
}

def installed() {
	refresh()
}

def refresh() {
	parent.updateEntity(device.deviceNetworkId)
}

def setString(string) {
   	sendEvent(name: "string", value: string)
}

def setNumber(number) {
   	sendEvent(name: "number", value: number)
}

def setUnit(unit) {
   	sendEvent(name: "unit", value: unit)
}
