/**
 *  HomeAssistant Sensors (Co2) v2022-07-20
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
	definition (name: "HomeAssistant Sensors (Co2)", namespace: "clipman", author: "clipman", ocfDeviceType: "x.com.st.d.airqualitysensor", mnmn: "SmartThingsCommunity", vid: "4a970f6b-88be-3735-bb1c-4682fe097cb2") {
		capability "Temperature Measurement"			//temperature
		capability "Relative Humidity Measurement"		//humidity
		capability "Carbon Dioxide Measurement"			//carbonDioxide
		capability "circlecircle06391.discomfortIndex"	//discomfortIndex
		capability "circlecircle06391.discomfortClass"	//discomfortClass
		capability "Refresh"
		capability "circlecircle06391.status"	// statusbar
		capability "circlecircle06391.string"	// string
		capability "circlecircle06391.number"	// number
		capability "circlecircle06391.unit"		// unit
	}
}

def setEntityStatus(state) {
	//log.debug "setEntityStatus(state) : ${state}"
	state = state.replace("\t", "").replace("\n", "")
	sendEvent(name: "statusbar", value: state)

	state = state.replace(" ppm", "")
	sendEvent(name: "carbonDioxide", value: state as int, unit: "ppm", displayed: true)
}

def setEntityStatus(state, attributes) {
	//log.debug "setEntityStatus(state, attributes) : ${state}, ${attributes}"
	if(attributes["temperature"] != null){
		sendEvent(name: "temperature", value:  attributes["temperature"] as double, unit: "C", displayed: true)
	}
	if(attributes["humidity"] != null){
		sendEvent(name: "humidity", value:  attributes["humidity"] as double, unit: "%", displayed: true)
	}
	if(attributes["discomfort_index"] != null){
		sendEvent(name: "discomfortIndex", value:  attributes["discomfort_index"] as double, unit: "DI℃", displayed: true)
	}
	if(attributes["discomfort_class"] != null){
		sendEvent(name: "discomfortClass", value:  attributes["discomfort_class"], unit: "", displayed: true)
	}
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