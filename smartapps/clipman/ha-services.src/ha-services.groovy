/**
 *  HA-Services v2022-04-18
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

import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import groovy.transform.Field

definition(
	name: "HA-Services",
	namespace: "clipman",
	author: "clipman",
	description: "HomeAssistant의 장치/센서들을 Smartthing로 가져오고 서비스를 호출합니다.",
	category: "My Apps",
	iconUrl: "https://brands.home-assistant.io/_/ha_services/icon.png",
	iconX2Url: "https://brands.home-assistant.io/_/ha_services/icon.png",
	iconX3Url: "https://brands.home-assistant.io/_/ha_services/icon.png",
	oauth: true
)

preferences {
	page(name: "mainPage")
	page(name: "entityPage")
	page(name: "devicePage")
	page(name: "sensorPage")
	page(name: "deletePage")
}

def mainPage() {
	dynamicPage(name: "mainPage", title: "", nextPage: null, uninstall: true, install: true) {
		section("HomeAssistant 연결 설정") {
			paragraph "https://xxx.duckdns.org 또는 http://xxx.duckdns.org:8123"
			input "haURL", "text", title: "HomeAssistant URL", required: true
			input "haToken", "text", title: "HomeAssistant Token", required: true
		}
		section("[HA -> ST] 장치/센서 가져오기") {
			href "entityPage", title: "가능한 HA 장치/센서들을 읽어오기"
			input "entityFilter", "text", title: "추가할 장치/센서를 선택하기 쉽게 필터처리 (선택사항)", required: false
			href "devicePage", title: "추가할 장치 선택"
			href "sensorPage", title: "추가할 센서 선택"
		}
		section("[HA -> ST] 장치/센서 삭제") {
			href "deletePage", title: "삭제할 장치/센서 선택"
		}
		section() {
			href url:"${apiServerUrl("/api/smartapps/installations/${app.id}/config?access_token=${state.accessToken}")}", style:"embedded", required:false, title:"HomeAssistant 설정 정보 읽어오기", description:"누르고 선택하고 복사한 후에 \"완료\"를 누르세요."
		}
		section() {
			label title: "앱의 이름 변경 (선택사항)", description: "앱의 이름을 바꿀 수 있어요", defaultValue: app?.name, required: false
		}
	}
}

def entityPage() {
	getEntityList()
	dynamicPage(name: "entityPage", title:"[HA -> ST] 장치/센서 가져오기", refreshInterval:5) {
		section("추가 가능한 HA 장치/센서들을 읽어옵니다.") {
			if(state.latestHttpResponse) {
				if(state.latestHttpResponse == 200) {
					paragraph "Connected \nOK: 200"
				} else {
					paragraph "Connection error \nHTTP response code: " + state.latestHttpResponse
				}
			}
		}
	}
}

def devicePage() {
	def addedDNIList = []
	def childDevices = getAllChildDevices()
	childDevices.each { childDevice->
		addedDNIList.push(childDevice.deviceNetworkId)
	}

	def list = []
	list.push("None")
	state.dataDeviceList.each {
		//def entity_id = "${it.entity_id}"
		def entity_id = "${it.id}"
		//def friendly_name = "${it.friendly_name}"
		//if(friendly_name == null) {
		//	friendly_name = ""
		//}
		def friendly_name = ""
		if(!existEntityInList(addedDNIList, entity_id)) {
			if(!settings.entityFilter) {
				if(!entity_id.contains("_st")) {
					list.push("${entity_id} [${friendly_name}]")
				}
			} else {
				if(entity_id.contains(settings.entityFilter) || friendly_name.contains(settings.entityFilter)) {
					if(!entity_id.contains("_st")) {
						list.push("${entity_id} [${friendly_name}]")
					}
				}
			}
		}
	}
	list.sort()
	dynamicPage(name: "devicePage", nextPage: "mainPage", title:"") {
		section ("[HA -> ST] 장치 가져오기") {
			input(name: "selectedAddDevice", title:"Select" , type: "enum", required: true, options: list, defaultValue: "None")
		}
		section ("장치 이름") {
			input(name: "addDeviceName", title: "이름 (선택사항)", type: "text", required: false, description: "선택한 장치의 이름을 바꿀 수 있어요", value: "")
		}
	}
}

def sensorPage() {
	def addedDNIList = []
	def childDevices = getAllChildDevices()
	childDevices.each { childDevice->
		addedDNIList.push(childDevice.deviceNetworkId)
	}

	def list = []
	list.push("None")
	state.dataSensorList.each {
		//def entity_id = "${it.entity_id}"
		def entity_id = "${it.id}"
		//def friendly_name = "${it.friendly_name}"
		//if(friendly_name == null) {
		//	friendly_name = ""
		//}
		def friendly_name = ""
		if(!existEntityInList(addedDNIList, entity_id)) {
			if(!settings.entityFilter) {
				if(!entity_id.contains("_st")) {
					list.push("${entity_id} [${friendly_name}]")
				}
			} else {
				if(entity_id.contains(settings.entityFilter) || friendly_name.contains(settings.entityFilter)) {
					if(!entity_id.contains("_st")) {
						list.push("${entity_id} [${friendly_name}]")
					}
				}
			}
		}
	}
	list.sort()
	dynamicPage(name: "sensorPage", nextPage: "mainPage", title:"") {
		section ("[HA -> ST] 센서 가져오기") {
			input(name: "selectedAddSensor", title:"Select" , type: "enum", required: true, options: list, defaultValue: "None")
		}
		section ("센서 이름") {
			input(name: "addSensorName", title: "이름 (선택사항)", type: "text", required: false, description: "선택한 센서의 이름을 바꿀 수 있어요", value: "")
		}
	}
}

def deletePage() {
	def list = []
	list.push("None")
	def childDevices = getAllChildDevices()
	childDevices.each { childDevice->
		list.push(childDevice.deviceNetworkId + " [" + childDevice.label + "]")
	}
	dynamicPage(name: "deletePage", nextPage: "mainPage", title:"") {
		section ("[HA -> ST] 장치/센서 삭제") {
			input(name: "selectedDeleteEntity", title:"Select" , type: "enum", required: true, options: list, defaultValue: "None")
		}
	}
}

def installed() {
	initialize()
	if (!state.accessToken) {
		createAccessToken()
	}
	app.updateSetting("selectedAddDevice", "None")
	app.updateSetting("selectedAddSensor", "None")
	app.updateSetting("selectedDeleteEntity", "None")
	app.updateSetting("addDeviceName", "")
	app.updateSetting("addSensorName", "")
}

def updated() {
	log.info "Updated with settings: ${settings}"
	initialize()
	app.updateSetting("selectedAddDevice", "None")
	app.updateSetting("selectedAddSensor", "None")
	app.updateSetting("selectedDeleteEntity", "None")
	app.updateSetting("addDeviceName", "")
	app.updateSetting("addSensorName", "")
}

def initialize() {
	deleteEntity()
	addDevice()
	addSensor()
	refreshEntityList()
}

def deleteEntity() {
	if(settings.selectedDeleteEntity) {
		if(settings.selectedDeleteEntity != "None") {
			//log.debug "DELETE >> " + settings.selectedDeleteEntity
			def nameAndDni = settings.selectedDeleteEntity.split(" \\[")
			try {
				deleteChildEntity(nameAndDni[0])
			} catch(err) {
				//
			}
		}
	}
}

def addDevice() {
	if(settings.selectedAddDevice) {
		if(settings.selectedAddDevice != "None") {
			//log.debug "ADD >> " + settings.selectedAddDevice
			//list.push("${entity_id} [${friendly_name}]")
			def tmp = settings.selectedAddDevice.split(" \\[")
			def entity_id = tmp[0]
			def dni = entity_id
			//def haDevice = getHADeviceByEntityId(entity_id)
			def haDevice = getEntityById(state.dataDeviceList, entity_id)
			if(haDevice) {
				def dth = "HomeAssistant Devices"
				def name = addDeviceName
				if(!name) {
					//name = haDevice.friendly_name
					if(!name) {
						name = entity_id
					}
				}
				try {
					//def childDevice = addChildDevice("clipman", dth, dni, location.hubs[0].id, ["label": name])
					def childDevice = addChildDevice("clipman", dth, dni, "", ["name": entity_id, "label": name])
					childDevice.setStatus(haDevice.state)
				} catch(err) {
					log.error "Add HA Device ERROR >> ${err}"
				}
			}
		}
	}
}

def addSensor() {
	if(settings.selectedAddSensor) {
		if(settings.selectedAddSensor != "None") {
			//log.debug "ADD >> " + settings.selectedAddSensor
			//list.push("${entity_id} [${friendly_name}]")
			def tmp = settings.selectedAddSensor.split(" \\[")
			def entity_id = tmp[0]
			def dni = entity_id
			//def haSensor = getHASensorByEntityId(entity_id)
			def haSensor = getEntityById(state.dataSensorList, entity_id)
			if(haSensor) {
				def dth = "HomeAssistant Sensors"
				def name = addSensorName
				if(!name) {
					//name = haSensor.friendly_name
					if(!name) {
						name = entity_id
					}
				}
				try {
					//def childDevice = addChildDevice("clipman", dth, dni, location.hubs[0].id, ["label": name])
					def childDevice = addChildDevice("clipman", dth, dni, "", ["name": entity_id, "label": name])
					childDevice.setStatus(haSensor.state)
				} catch(err) {
					log.error "Add HA Sensor ERROR >> ${err}"
				}
			}
		}
	}
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

def refreshEntityList() {
	services("/api/services/ha_services/refresh", [])
}
/*
def refreshEntityList() {
	def service = "/api/services/ha_services/refresh"
	def params = [
		uri: settings.haURL,
		path: service,
		headers: ["Authorization": "Bearer " + settings.haToken],
		requestContentType: "application/json"
	]
	try {
		httpPost(params) { resp ->
			return true
		}
	} catch (e) {
		log.error "HomeAssistant Refresh Error: $e"
		return false
	}
}
*/

/*
def getHADeviceByEntityId(entity_id) {
	def target
	state.dataDeviceList.each { haDevice ->
		//if(haDevice.entity_id == entity_id) {
		if(haDevice.id == entity_id) {
			target = haDevice
		}
	}
	return target
}

def getHASensorByEntityId(entity_id) {
	def target
	state.dataSensorList.each { haSensor ->
		//if(haSensor.entity_id == entity_id) {
		if(haSensor.id == entity_id) {
			target = haSensor
		}
	}
	return target
}
*/
// getEntityById(state.dataDeviceList, entity_id)
def getEntityById(list, entity_id) {
	def target
	list.each { item ->
		//if(item.entity_id == entity_id) {
		if(item.id == entity_id) {
			target = item
		}
	}
	return target
}

/*
//addedDNIList.contains(entity_id), 이게 잘 안되어서 만들었는데
//이렇게 하면 안됨: 이유는 모름
def existEntityInList(list, entity_id) {
	list.each { item ->
		if(item == entity_id) {
			return true
		}
	}
	return false
}
*/
def existEntityInList(list, entity_id) {
	for (item in list) {
		if(item == entity_id) {
			return true
		}
	}
	return false
}

def getEntityList() {
	def service = "/api/states"
	def params = [
		uri: settings.haURL,
		path: service,
		headers: ["Authorization": "Bearer " + settings.haToken],
		requestContentType: "application/json"
	]
	def deviceEntity = ["switch", "light", "climate", "fan", "vacuum", "cover", "lock", "script", "rest_command", "esphome",
						"button", "input_button", "automation", "camera", "input_boolean", "media_player"]
	def sensorEntity = ["sensor", "binary_sensor"]
	def jsonDevice = []
	def jsonSensor = []
	try {
		httpGet(params) { resp ->
			resp.headers.each {
				//log.debug "${it.name} : ${it.value}"
			}
			if (resp.status == 200) {
				//log.debug "resp.data: ${resp.data}"
				resp.data.each {
					def entity_type = it.entity_id.split('\\.')[0]
					if(deviceEntity.contains(entity_type)) {
						//def objDevice = [entity_id: "${it.entity_id}", state: "${it.state}", attributes: [friendly_name: "${it.attributes.friendly_name}"]]
						//def objDevice = [entity_id: "${it.entity_id}", state: "${it.state}", friendly_name: "${it.attributes.friendly_name}"]
						//def objDevice = [entity_id: "${it.entity_id}", friendly_name: "${it.attributes.friendly_name}"]
						//def objDevice = [entity_id: "${it.entity_id}", friendly_name: ""]
						def objDevice = [id: "${it.entity_id}"]
						jsonDevice.push(objDevice)
					}
					if(sensorEntity.contains(entity_type)) {
						//def objSensor = [entity_id: "${it.entity_id}", state: "${it.state}", attributes: [friendly_name: "${it.attributes.friendly_name}"]]
						//def objSensor = [entity_id: "${it.entity_id}", state: "${it.state}", friendly_name: "${it.attributes.friendly_name}"]
						//def objSensor = [entity_id: "${it.entity_id}", friendly_name: "${it.attributes.friendly_name}"]
						//def objSensor = [entity_id: "${it.entity_id}", friendly_name: ""]
						def objSensor = [id: "${it.entity_id}"]
						jsonSensor.push(objSensor)
					}
				}
				state.dataDeviceList = jsonDevice
				state.dataSensorList = jsonSensor
			}
			state.latestHttpResponse = resp.status
		}
	} catch (e) {
		state.latestHttpResponse = 401
		log.error "HomeAssistant Services Error: $e"
	}
}

//HA->ST
def updateDevice() {
	def dni = params.entity_id
	def attr = null
	def oldstate = null
	try {
		attr = new groovy.json.JsonSlurper().parseText(new String(params.attr.decodeBase64()))
	} catch(err) {
		//log.debug "${dni} attr decoding error : "+params.attr
	}
	oldstate = params?.old
	try {
		def device = getChildDevice(dni)
		if(device) {
			log.debug "HA->ST >> [${dni}] state:${params.value}  attr:${attr}  oldstate:${oldstate}" + ((params?.unit) ? "  unit:${params.unit}" : "")
			//HA->ST >> [climate.kocom_room2_thermostat] state:heat attr:[current_temperature:24.0, friendly_name:작은방난방, hvac_modes:[off, heat], max_temp:25.0, min_temp:20.0, supported_features:1, target_temp_step:1.0, temperature:23.0] oldstate:off
			if (params.value != oldstate) {
				def entity_type = dni.split('\\.')[0]
				def onOff = params.value
				switch(entity_type) {
					case "vacuum":
						if(params.value == "docked" || params.value == "returning") {
							onOff = "off"
						} else {
							onOff = "on"
						}
						break;
					case "cover":
						if(params.value == "open" || params.value == "opening" || params.value == "partially open") {
							onOff = "on"
						} else {
							onOff = "off"
						}
						break;
					case "lock":
						if(params.value == "locked") {
							onOff = "off"
						} else {
							onOff = "on"
						}
						break;
					case "climate":
						if(params.value == "off") {
							onOff = "off"
						} else {
							onOff = "on"
						}
						break;
					default:	//switch, light, fan, input_boolean, ...
						break;
				}
				device.setStatus(onOff)	// 상태만 반영함(sendEvent())
			}
		}
	} catch(err) {
		log.error "${err}"
	}
	def deviceJson = new groovy.json.JsonOutput().toJson([result: true])
	render contentType: "application/json", data: deviceJson
}

def updateSensor() {
	def dni = params.entity_id
	def attr = null
	def oldstate = null
	try {
		attr = new groovy.json.JsonSlurper().parseText(new String(params.attr.decodeBase64()))
	} catch(err) {
		//log.debug "${dni} attr decoding error : "+params.attr
	}
	oldstate = params?.old
	try {
		def device = getChildDevice(dni)
		if(device) {
			log.debug "HA->ST >> [${dni}] state:${params.value}  attr:${attr}  oldstate:${oldstate}" + ((params?.unit) ? "  unit:${params.unit}" : "")
			if (params.value != oldstate) {
				def entity_type = dni.split('\\.')[0]
				def state = params.value
				def unit = (params?.unit) ? " ${params.unit}" : ""
				switch(entity_type) {
					case "sensor":
						state = params.value
						break;
					case "binary_sensor":
						state = params.value
						break;
					default:
						state = params.value
						break;
				}
				device.setStatus(state + unit)	// 상태만 반영함(sendEvent())
			}
		}
	} catch(err) {
		log.error "${err}"
	}
	def deviceJson = new groovy.json.JsonOutput().toJson([result: true])
	render contentType: "application/json", data: deviceJson
}

/*
//HA->ST HA에서 변경한 Switch상태를 ST에 반영(on, off)
def updateSTDevice() {
	//log.debug "POST >>>> params:${params}"
	//params:[turn:on, attributes:switch, dni:anbang_gonggiceongjeonggi, ...]
	def state = params.turn					//"${params.turn}"	// on, off

	if(settings["switch"]) {				//switch
		settings["switch"].each { device ->
			if(device.name == params.dni) {
				device."$params.turn"()
			}
		}
	}
	render contentType: "text/html", data: state
}
*/

def updateEntity(entity_id) {
	def device = getChildDevice(entity_id)
	def service = "/api/states/${entity_id}"
	def params = [
		uri: settings.haURL,
		path: service,
		headers: ["Authorization": "Bearer " + settings.haToken],
		requestContentType: "application/json"
	]
	if(device) {
		try {
			httpGet(params) { resp ->
				resp.headers.each {
					//log.debug "${it.name} : ${it.value}"
				}
				if (resp.status == 200) {
					log.debug "resp.data : ${resp.data}"
					// resp.data: [attributes:[friendly_name:rockrobo.vacuum.v1 Current Clean Duration, icon:mdi:timer-sand, unit_of_measurement:s], context:[id:854463bda101b4ef98586909bc437f75, parent_id:null, user_id:null], entity_id:sensor.rockrobo_vacuum_v1_current_clean_duration, last_changed:2022-04-16T03:55:17.772340+00:00, last_updated:2022-04-16T03:55:17.772340+00:00, state:3740]
					//def obj = [entity_id: "${resp.data.entity_id}", state: "${resp.data.state}", attributes: "${resp.data.attributes}"]
					//json.push(obj)
					def unit = (resp.data.attributes?.unit_of_measurement) ? " ${resp.data.attributes.unit_of_measurement}" : ""
					device.setStatus(resp.data.state + unit)
				}
			}
		} catch (e) {
			log.error "HomeAssistant Services updateEntity Error: $e"
		}
	}
}

def getHADevices() {
	def haDevices = []
	def childDevices = getAllChildDevices()
	childDevices.each { childDevice->
		haDevices.push(childDevice.deviceNetworkId)
	}
	def deviceJson = new groovy.json.JsonOutput().toJson([list: haDevices])
	render contentType: "application/json", data: deviceJson
}

def authError() {
	[error: "Permission denied"]
}

def renderConfig() {
	def configJson = new groovy.json.JsonOutput().toJson([
		name: "HA-Services",
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
		path("/device")	{ action: [GET: "authError"] }
		path("/sensor")	{ action: [GET: "authError"] }
		path("/getHADevices") { action: [GET: "authError"] }
		//path("/get")	{ action: [POST: "authError"] }
	} else {
		path("/config")	{ action: [GET: "renderConfig"] }
		path("/device")	{ action: [GET: "updateDevice"] }
		path("/sensor")	{ action: [GET: "updateSensor"] }
		path("/getHADevices") { action: [GET: "getHADevices"] }
		//path("/get")	{ action: [POST: "updateSTDevice"] }
	}
}
