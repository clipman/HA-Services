/**
 *  HA-Updater v2022-04-19
 *  clipman@naver.com
 *  날자
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */

import groovy.transform.Field

@Field
capabilityMap = [
	"airConditionerFanMode": [
		name: "Air Conditioner Fan Mode",
		capability: "capability.airConditionerFanMode",
		attributes: ["fanMode"]
	],
	"rainfallProbability": [
		name: "Rainfall Probability",
		capability: "capability.circlecircle06391.precipChance",
		attributes: ["precipChance"]
	],
	"veryFineDustSensor": [
		name: "Very Fine Dust Sensor",
		capability: "capability.veryFineDustSensor",
		attributes: ["veryFineDustLevel"]
	],
	"weatherForecast": [
		name: "Weather Forecast",
		capability: "capability.circlecircle06391.weatherforecast",
		attributes: ["weatherForecast"]
	],
	"waterSensor": [
		name: "Water Sensor",
		capability: "capability.borderreason25422.waterSensor",
		attributes: ["water"]
	]
]

@Field
attributesMap = [
	"fanMode": "fan_mode",
	"precipChance": "rainfall_probability",
	"veryFineDustLevel": "very_fine_dust_level",
	"weatherForecast": "weather_forecast",
	"water": "water"
]

definition(
	name: "HA-Updater",
	namespace: "clipman",
	author: "clipman",
	description: "HomeAssistant의 센서로 등록된 SmartThings의 센서값이 변경되면 갱신합니다.",
	category: "My Apps",
	iconUrl: "https://brands.home-assistant.io/_/ha_services/icon.png",
	iconX2Url: "https://brands.home-assistant.io/_/ha_services/icon.png",
	iconX3Url: "https://brands.home-assistant.io/_/ha_services/icon.png",
	oauth: true
)

preferences {
	page(name: "mainPage")
	page(name: "stSensorPage")
}

def mainPage() {
	dynamicPage(name: "mainPage", title: "", nextPage: null, uninstall: true, install: true) {
		section("HomeAssistant 연결 설정") {
			paragraph "https://xxx.duckdns.org 또는 http://xxx.duckdns.org:8123"
			input "haURL", "text", title: "HomeAssistant URL", required: true
			input "haToken", "text", title: "HomeAssistant Token", required: true
		}
		section("[ST -> HA] 센서 등록하기") {
			href "stSensorPage", title: "등록할 센서 선택", description:"HA의 센서로 등록해 두면 값이 변경시 적용됩니다."
		}
		section() {
			href url:"${apiServerUrl("/api/smartapps/installations/${app.id}/config?access_token=${state.accessToken}")}", style:"embedded", required:false, title:"HomeAssistant 설정 정보 읽어오기", description:"누르고 선택하고 복사한 후에 \"완료\"를 누르세요."
		}
		section() {
			label title: "앱의 이름 변경 (선택사항)", description: "앱의 이름을 바꿀 수 있어요", defaultValue: app?.name, required: false
		}
	}
}

def installed() {
	initialize()
}

def initialize() {
	if (!state.accessToken) {
		createAccessToken()
	}
}

def updated() {
	log.info "Updated with settings: ${settings}"
	initialize()
	unsubscribe()

	capabilityMap.each { key, capabilities ->
		capabilities["attributes"].each { attribute ->
			for (item in settings[key]) {
				subscribe(item, attribute, stateChangeHandler)
			}
		}
	}
}

def stSensorPage() {
	dynamicPage(name: "stSensorPage", title: "") {
		section ("[ST -> HA] 센서 등록하기") {
			capabilityMap.each { key, capability ->
				input key, capability["capability"], title: capability["name"], multiple: true, required: false
			}
		}
	}
}

def stateChangeHandler(evt) {
	//evt.id: Device ID
	//evt.displayName: 서재불,책상불,서재조도,...
	//evt.name: switch,signalLighting,motion,contact,carbonDioxide,...
	def device = evt.getDevice()
	def entity_id = "sensor." + device.name
	// HA의 센서 이름은 sensor.<name>_<attribute>
	def attribute = attributesMap[evt.name]

	if(attribute == null) {
		attribute = evt.name.toLowerCase()
	}
	entity_id += ("_" + attribute)
	log.debug "[stateChangeHandler] Attribute: ${evt.displayName}[${evt.name}] >>>>> ${entity_id}"
	services("/api/services/homeassistant/update_entity", ["entity_id": entity_id])
}

def services(service, data) {
	def params = [
		uri: settings.haURL,
		path: service,
		headers: ["Authorization": "Bearer " + settings.haToken],
		requestContentType: "application/json",
		body: data
	]
	//log.info "Services: $params"
	try {
		httpPost(params) { resp ->
			return true
		}
	} catch (e) {
		log.error "HomeAssistant Services({$service}) Error: $e"
		return false
	}
}

def authError() {
	[error: "Permission denied"]
}

def renderConfig() {
	def configJson = new groovy.json.JsonOutput().toJson([
		name: "HA-Updater",
		app_url: apiServerUrl("/api/smartapps/installations/"),
		app_id: app.id,
		access_token: state.accessToken
	])
	def configString = new groovy.json.JsonOutput().prettyPrint(configJson)
	render contentType: "text/plain", data: configString
}

mappings {
	if (!params.access_token || (params.access_token && params.access_token != state.accessToken)) {
		path("/config")	{ action: [GET: "authError"] }
		path("/get") { action: [GET: "authError"] }
	} else {
		path("/config")	{ action: [GET: "renderConfig"] }
		path("/get") { action: [GET: "getDevice"] }
	}
}

// ST->HA resource url을 호출할 경우에 실행, 갱신시(update_entity), 시작시, 주기적(scan_interval)으로 실행됨, switch 포함
def getDevice() {
	def status = null
	def totalMap = [:]
	def resultMap = [:]
	capabilityMap.each { key, capability ->
		capability["attributes"].each { attribute ->
			if(settings[key]) {
				settings[key].each { device ->
					def dni = device.name		//def dni = device.deviceNetworkId
					if(dni == params.dni) {
						if(params.attributes) {
							//log.debug "ST -> HA >> (" + totalMap["entity_id"] + "." + params.attributes + ": " + device.currentValue(params.attributes)
							status = device.currentValue(params.attributes)
						} else {
							status = device.currentValue("switch")
						}

						//clipman, 수정
						//totalMap["entity_id"] = "sensor." + device.name	//totalMap["entity_id"] = "sensor.st_" + dni.toLowerCase()
						totalMap["name"] = device.name

						def theAtts = device.supportedAttributes
						theAtts.each { att ->
							try {
								//if(attributesMap.contains(att.name)) {	//if(attrList.contains(att.name)) {
								//	if(status == null) {
								//		status = device.currentValue(att.name)
								//	}
								//}

								//def _attr = "${att.name}State"
								//def val = device."$_attr".value
								//if(val != null) {
							   	//	resultMap["${att.name}"] = val
                                //}
								//null 에러가 발생해서 아래와 같이 실행
								def val = device.currentValue(att.name)
								if(val != null) {
							   		resultMap["${att.name}"] = val
								}
							} catch(e) {
								log.error("${e} --> ${att.name}")
							}
						}
						//log.debug "Switch:" + device.currentValue("switch")
					}
				}
			}
		}
	}
	totalMap['state'] = status
	totalMap['attributes'] = resultMap
	def deviceJson = new groovy.json.JsonOutput().toJson(totalMap)
	//log.debug "[ST -> HA] ${params}, status: ${resultMap}"
	if(status == null) {
		log.warn "[ST -> HA] Device is not selected to [Add ST Device]: ${params.dni}"
	} else {
		//log.info "[ST -> HA] " + totalMap["name"] + ": ${status}"
	}
	render contentType: "application/json", data: deviceJson
}