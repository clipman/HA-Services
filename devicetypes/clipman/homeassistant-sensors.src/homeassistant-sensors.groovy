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
	definition (name: "HomeAssistant Sensors", namespace: "clipman", author: "clipman", mnmn: "SmartThingsCommunity", vid: "1e931a7f-172d-3b8f-8e12-2529b01422e2") {
		capability "Refresh"
		capability "circlecircle06391.status"	// statusbar
		capability "circlecircle06391.string"	// string
		capability "circlecircle06391.number"	// number
		capability "circlecircle06391.unit"		// unit
	}
}

def setStatus(state) {
	state = state.replace("\t", "").replace("\n", "")
	sendEvent(name: "statusbar", value: state)
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
