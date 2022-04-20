/**
 *  HomeAssistant Sensors (Custom) v2022-04-20
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
	definition (name: "HomeAssistant Sensors (Custom)", namespace: "clipman", author: "clipman", mnmn: "SmartThingsCommunity", vid: "545f5098-37ee-312f-a5c1-f282fc7d6733") {
		capability "circlecircle06391.statusBar"
		capability "circlecircle06391.string"
		capability "circlecircle06391.number"
		capability "circlecircle06391.unit"
		capability "Temperature Measurement"				//temperature
		capability "Relative Humidity Measurement"			//humidity
		capability "Dust Sensor"							//dustLevel, fineDustLevel
		capability "Very Fine Dust Sensor"					//veryFineDustLevel
		capability "Carbon Dioxide Measurement"				//carbonDioxide
		capability "Tvoc Measurement"						//tvocLevel
		capability "Illuminance Measurement"				//illuminance
		capability "Energy Meter"							//energy
		capability "Power Meter"							//power
		capability "Battery"								//battery
		capability "Refresh"
	}
	preferences {
		input type: "paragraph", element: "paragraph", title: "만든이", description: "김민수 clipman@naver.com [날자]<br>네이버카페: Smartthings & IoT home Community", displayDuringSetup: false
		input type: "paragraph", element: "paragraph", title: "HomeAssistant Sensors (Custom) v2022-04-18", description: "", displayDuringSetup: false
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

def setString(string) {
   	sendEvent(name: "string", value: string)
}

def setNumber(number) {
	sendEvent(name: "temperature", value: number, unit: "C")
	sendEvent(name: "humidity", value: number, unit: "%")
	sendEvent(name: "battery", value: number, unit: "%")
	sendEvent(name: "carbonDioxide", value: number, unit: "ppm")
	sendEvent(name: "tvocLevel", value: number, unit: "ppm")
	sendEvent(name: "dustLevel", value: number, unit: "㎍/㎥")
	sendEvent(name: "fineDustLevel", value: number, unit: "㎍/㎥")
	sendEvent(name: "veryFineDustLevel", value: number, unit: "㎍/㎥")
	sendEvent(name: "illuminance", value: number, unit: "lx")
	sendEvent(name: "power", value: number, unit: "W")
	sendEvent(name: "energy", value: number, unit: "kWh")
	sendEvent(name: "number", value: number)
}

def setUnit(unit) {
   	sendEvent(name: "unit", value: unit)
}