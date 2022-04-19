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
 */

import groovy.transform.Field

@Field
capabilityMap = [
	"airConditionerFanMode": [
		name: "Air Conditioner Fan Mode",
		capability: "capability.airConditionerFanMode",
		attributes: ["fanMode"]
	],
	"veryFineDustSensor": [
		name: "Very Fine Dust Sensor",
		capability: "capability.veryFineDustSensor",
		attributes: ["veryFineDustLevel"]
	],
	"waterSensor": [
		name: "WaterSensor",
		capability: "capability.borderreason25422.waterSensor",
		attributes: ["water"]
	]
]

@Field
attributesMap = [
	"fanMode": "fan_mode",
	"veryFineDustLevel": "very_fine_dust_level",
	"water": "water"
]

definition(
	name: "HA-Updater",
	namespace: "clipman",
	author: "clipman",
	description: "HomeAssistant의 센서로 등록된 Smartthing의 센서값이 변경되면 갱신합니다.",
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
			label title: "앱의 이름 변경 (선택사항)", description: "앱의 이름을 바꿀 수 있어요", defaultValue: app?.name, required: false
		}
	}
}

def installed() {
	initialize()
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

def initialize() {
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
