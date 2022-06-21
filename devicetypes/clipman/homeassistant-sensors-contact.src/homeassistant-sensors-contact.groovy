/**
 *  HomeAssistant Sensors (Contact) v2022-06-22
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
	definition (name: "HomeAssistant Sensors (Contact)", namespace: "clipman", author: "clipman", ocfDeviceType: "x.com.st.d.sensor.multifunction", mnmn: "SmartThingsCommunity", vid: "bc048a88-3266-3c7a-bd8e-a25d06bf2a0f") {
		capability "Contact Sensor"             // contact
		capability "Refresh"
		capability "circlecircle06391.status"	// statusbar
		capability "circlecircle06391.string"	// string
		capability "circlecircle06391.number"	// number
		capability "circlecircle06391.unit"		// unit
	}
}

def setEntityStatus(state) {
	def st_state = (state == "on" ? "open" : "closed")
	sendEvent(name: "contact", value: st_state)
	state = state.replace("\t", "").replace("\n", "")
	sendEvent(name: "statusbar", value: state)
}

def setEntityStatus(state, attributes) {
	//log.debug "setEntityStatus(state, attributes) : ${state}, ${attributes}"
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