/**
 *  HomeAssistant Sensors (NaverWeather) v2022-04-20
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
	definition (name: "HomeAssistant Sensors (NaverWeather)", namespace: "clipman", author: "clipman", ocfDeviceType: "x.com.st.d.airqualitysensor", mnmn: "SmartThingsCommunity", vid: "f10d8eef-7603-3069-8245-bbdefe8c3414") {
		capability "Temperature Measurement"
		capability "Relative Humidity Measurement"
		capability "Dust Sensor"
		capability "Ultraviolet Index"						//
		capability "circlecircle06391.todaymaxtemp"
		capability "circlecircle06391.todaymintemp"
		capability "circlecircle06391.todayfeeltemp"
		capability "circlecircle06391.finedustgrade"
		capability "circlecircle06391.ultrafinedustgrade"
		capability "circlecircle06391.windspeed"
		capability "circlecircle06391.windbearing"
		capability "circlecircle06391.weatherforecast"
		capability "circlecircle06391.rainystart"
		capability "circlecircle06391.ozon"
		capability "circlecircle06391.ozongrade"
		capability "circlecircle06391.uvgrade"				//
		capability "circlecircle06391.locationinfo"
		capability "Refresh"
		capability "circlecircle06391.status"	// statusbar
		capability "circlecircle06391.string"	// string
		capability "circlecircle06391.number"	// number
		capability "circlecircle06391.unit"		// unit
	}
}

def setEntityStatus(state) {
	//state = state.replace("\t", "").replace("\n", "")
   	//sendEvent(name: "statusbar", value: state)
	state = state.replace(", 어제보다",",")
	sendEvent(name: "weatherForecast", value: state, unit: "")
}

def setEntityStatus(state, attributes) {
	//log.debug "setEntityStatus(state, attributes) : ${state}, ${attributes}"
	def entity
   	entity = parent.getEntityStatus("sensor.naver_weather_nowtemp_1")
	sendEvent(name: "temperature", value: entity.state, unit: "C")
   	entity = parent.getEntityStatus("sensor.naver_weather_todayfeeltemp_1")
	sendEvent(name: "temperatureFeel", value: entity.state, unit: "C")
   	entity = parent.getEntityStatus("sensor.naver_weather_todaymaxtemp_1")
	sendEvent(name: "temperatureMax", value: entity.state, unit: "C")
   	entity = parent.getEntityStatus("sensor.naver_weather_todaymintemp_1")
	sendEvent(name: "temperatureMin", value: entity.state, unit: "C")
   	entity = parent.getEntityStatus("sensor.naver_weather_humidity_1")
	sendEvent(name: "humidity", value: entity.state, unit: "%")
   	entity = parent.getEntityStatus("sensor.naver_weather_finedust_1")
	sendEvent(name: "dustLevel", value: entity.state, unit: "㎍/㎥")
   	entity = parent.getEntityStatus("sensor.naver_weather_ultrafinedust_1")
	sendEvent(name: "fineDustLevel", value: entity.state, unit: "㎍/㎥")
   	entity = parent.getEntityStatus("sensor.naver_weather_finedustgrade_1")
	sendEvent(name: "dustGrade", value: entity.state, unit: entity.unit)
   	entity = parent.getEntityStatus("sensor.naver_weather_ultrafinedustgrade_1")
	sendEvent(name: "fineDustGrade", value: entity.state, unit: entity.unit)
   	entity = parent.getEntityStatus("sensor.naver_weather_ultrafinedustgrade_1")
	sendEvent(name: "fineDustGrade", value: entity.state, unit: entity.unit)
	entity = parent.getEntityStatus("sensor.naver_weather_windspeed_1")
	sendEvent(name: "windSpeed", value: entity.state, unit: entity.unit)
   	entity = parent.getEntityStatus("sensor.naver_weather_windbearing_1")
	sendEvent(name: "windBearing", value: entity.state, unit: "풍")
   	entity = parent.getEntityStatus("sensor.naver_weather_ozon_1")
	sendEvent(name: "ozonLevel", value: entity.state, unit: entity.unit)
   	entity = parent.getEntityStatus("sensor.naver_weather_ozongrade_1")
	sendEvent(name: "ozonGrade", value: entity.state, unit: entity.unit)
   	entity = parent.getEntityStatus("sensor.naver_weather_locationinfo_1")
	sendEvent(name: "locationInfo", value: entity.state, unit: entity.unit)
   	entity = parent.getEntityStatus("sensor.naver_weather_rainystart_1")
	if(entity.state == "비안옴"){
		//sendEvent(name: "rainyStartTime", value: 0, unit: "비안옴")
		sendEvent(name: "rainyStartTime", value: "비안옴", unit: "")
	} else {
		sendEvent(name: "rainyStartTime", value: entity.state.replace("시","") as Integer, unit: "시")
	}

   	entity = parent.getEntityStatus("sensor.naver_weather_ozongrade_1")
	sendEvent(name: "ozonGrade", value: entity.state, unit: entity.unit)

	sendEvent(name: "ultravioletIndex", value: 0, unit: "")
	sendEvent(name: "ultravioletGrade", value: "", unit: "")
}

def installed() {
	refresh()
}

def refresh(){
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